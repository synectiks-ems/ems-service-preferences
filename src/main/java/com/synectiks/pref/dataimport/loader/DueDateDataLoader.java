package com.synectiks.pref.dataimport.loader;

import com.synectiks.pref.PreferencesApp;
import com.synectiks.pref.config.ApplicationProperties;
import com.synectiks.pref.constant.CmsConstants;
import com.synectiks.pref.dataimport.AllRepositories;
import com.synectiks.pref.dataimport.DataLoader;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.DueDate;
import com.synectiks.pref.domain.ExceptionRecord;
import com.synectiks.pref.domain.enumeration.Frequency;
import com.synectiks.pref.exceptions.DuplicateRecordFoundException;
import com.synectiks.pref.exceptions.MandatoryFieldMissingException;
import com.synectiks.pref.service.util.CommonUtil;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class DueDateDataLoader extends DataLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AllRepositories allRepositories;
    private String sheetName;

    public DueDateDataLoader(String sheetName, AllRepositories allRepositories) {
        super(sheetName, allRepositories);
        this.sheetName = sheetName;
        this.allRepositories = allRepositories;
    }

    @Override
    public <T> T getObject(Row row, Class<T> cls) throws InstantiationException, IllegalAccessException, DuplicateRecordFoundException, MandatoryFieldMissingException {
        StringBuilder sb = new StringBuilder();

        DueDate obj = CommonUtil.createCopyProperties(cls.newInstance(), DueDate.class);


        String paymentMethod = row.getCellAsString(0).orElse(null);
        if (CommonUtil.isNullOrEmpty(paymentMethod)) {
            sb.append("payment_method, ");
            logger.warn("Mandatory field missing. Field name - payment_method");
        } else {
            obj.setPaymentMethod(paymentMethod);
        }

        String installments = row.getCellAsString(1).orElse(null);
        if (CommonUtil.isNullOrEmpty(installments)) {
            sb.append("installments, ");
            logger.warn("Mandatory field missing. Field name - installments");
        } else {
            obj.setInstallments(Long.parseLong(installments));
        }


        String dayDesc = row.getCellAsString(2).orElse(null);
        if (CommonUtil.isNullOrEmpty(dayDesc)) {
            sb.append("day_desc, ");
            logger.warn("Mandatory field missing. Field name - day_desc");
        } else {
            obj.setDayDesc(dayDesc);
        }

        String paymentDay = row.getCellAsString(3).orElse(null);
        if (CommonUtil.isNullOrEmpty(paymentDay)) {
            sb.append("payment_day, ");
            logger.warn("Mandatory field missing. Field name - payment_day");
        } else {
            obj.setPaymentDay(Long.parseLong(paymentDay));
        }

        String frequency = row.getCellAsString(4).orElse(null);
        if (CommonUtil.isNullOrEmpty(frequency)) {
            sb.append("frequency, ");
            logger.warn("Mandatory field missing. Field name - frequency");
        } else {
            obj.setFrequency(frequency);
        }

        String branchName = row.getCellAsString(5).orElse(null);
        if(CommonUtil.isNullOrEmpty(branchName)) {
            sb.append("branch_id, ");
            logger.warn("Mandatory field missing. Field name - branch_id");
        }else {
            Branch branch = new Branch();
            branch.setBranchName(branchName);
            Optional<Branch> b = this.allRepositories.findRepository("branch").findOne(Example.of(branch));
            if(b.isPresent()) {
//                obj.setBranch(b.get());
                obj.setBranchId(b.get().getId());
            }else {
                sb.append("branch_id, ");
                logger.warn("Branch not found. Given branch name : "+branchName);
            }
        }

        return (T) obj;

    }

    public <T> void saveCmsData(ReadableWorkbook wb, Class<T> cls) {
        logger.debug(String.format("Saving %s data started.",this.sheetName));

        Sheet sheet = wb.findSheet(this.sheetName).orElse(null);
        try {
//			T instance = cls.newInstance();
            ApplicationProperties applicationProperties = PreferencesApp.getBean(ApplicationProperties.class);
            RestTemplate restTemplate = PreferencesApp.getBean(RestTemplate.class);

            try (Stream<Row> rows = sheet.openStream()) {
                List<T> list = new ArrayList<>();
                List<ExceptionRecord> exceptionList = new ArrayList<>();
                StringBuilder sb = new StringBuilder(String.format("\nInvalid records found for table - %s: \n", this.sheetName));
                sb.append("Rows having invalid records\n");
                rows.forEach(row -> {

                    if (list.size() == CmsConstants.BATCH_SIZE) {
                        List expList = postDueDateData(list, applicationProperties, restTemplate);
                        exceptionList.addAll(expList);
                        list.clear();
                    }
                    if (exceptionList.size() == CmsConstants.BATCH_SIZE) {
                        allRepositories.findRepository("exception_record").saveAll(exceptionList);
                        exceptionList.clear();
                    }

                    // Skip first header row
                    if (row.getRowNum() > 1) {
                        try {
                            T obj = getObject(row, cls);
                            if(obj != null) {
                                list.add(obj);
                            }
                        } catch (InstantiationException | IllegalAccessException e) {
                            logger.error("Exception in loading data from excel file :"+e.getMessage(), e);
                        } catch(Exception e) {
                            ExceptionRecord expObj = getExceptionObject(row, e);
                            sb.append(String.format("%s : %s  , %s\n", e.getClass().getSimpleName(), e.getMessage(), row.toString()));
                            if(expObj != null) {
                                exceptionList.add(expObj);
                            }
                        }
                    }
                });
                // Save remaining items
                List expList = postDueDateData(list, applicationProperties, restTemplate);
                list.clear();
                exceptionList.addAll(expList);
                if(exceptionList.size() > 0) {
                    logger.warn(sb.toString());
                    logger.info("Saving records having exceptions/errors in exception_record table");
                    allRepositories.findRepository("exception_record").saveAll(exceptionList);
                }
                exceptionList.clear();
            }
        } catch (Exception e) {
            logger.error(String.format("Failed to iterate %s sheet rows ", this.sheetName), e);
        }
        logger.debug(String.format("Saving %s data completed.", this.sheetName));
    }

    private List<DueDate> postDueDateData(List list, ApplicationProperties applicationProperties, RestTemplate restTemplate){
        logger.debug("Posting duedate data to DueDateRestController of cms-fee");
        String url = applicationProperties.getFeeSrvUrl()+"/api/cmsduedate-bulk-load";
        List<DueDate> ls = null;
        try {
            ls = restTemplate.postForObject(url, list, List.class);
            if(ls != null && ls.size() >0 ) {
                logger.debug("List of duedate records could not be saved : ",ls);
            }
        }catch(Exception e) {
            logger.error("duedate could not be saved. Exception : ", e);
        }
        return ls;
    }


}
