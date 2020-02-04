package com.synectiks.pref.dataimport.loader;

import java.util.Optional;

import org.dhatim.fastexcel.reader.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.synectiks.pref.constant.CmsConstants;
import com.synectiks.pref.dataimport.AllRepositories;
import com.synectiks.pref.dataimport.DataLoader;
import com.synectiks.pref.domain.AcademicYear;
import com.synectiks.pref.domain.Term;
import com.synectiks.pref.exceptions.AdditionalTermFoundException;
import com.synectiks.pref.exceptions.MandatoryFieldMissingException;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.service.util.DateFormatUtil;

public class TermDataLoader extends DataLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AllRepositories allRepositories;
    private String sheetName;

    public TermDataLoader(String sheetName, AllRepositories allRepositories) {
        super(sheetName, allRepositories);
        this.allRepositories = allRepositories;
        this.sheetName = sheetName;
    }

    @Override
    public <T> T getObject(Row row, Class<T> cls) throws InstantiationException, IllegalAccessException, MandatoryFieldMissingException, AdditionalTermFoundException {
        StringBuilder sb = new StringBuilder();
        Term obj = CommonUtil.createCopyProperties(cls.newInstance(), Term.class);

        String termDesc = row.getCellAsString(0).orElse(null);
        if (CommonUtil.isNullOrEmpty(termDesc)) {
            sb.append("description, ");
            logger.warn("Mandatory field missing. Field name - description");
        } else {
            obj.setDescription(termDesc);
        }
        
        String academicYear = row.getCellAsString(4).orElse(null);
        if(CommonUtil.isNullOrEmpty(academicYear)) {
            sb.append("academic_year_id, ");
            logger.warn("Mandatory field missing. Field name - academic_year_id");
        } else {
            AcademicYear ay = new AcademicYear();
            ay.setDescription(academicYear);
            Optional<AcademicYear> oay = this.allRepositories.findRepository("academic_year").findOne(Example.of(ay));
            if(oay.isPresent()) {
                obj.setAcademicYear(oay.get());
            }else {
                sb.append("academic_year_id, ");
                logger.warn("AcademicYear not found. Given academicYear name : "+academicYear);
            }
        }
        
        String startDate = row.getCellAsString(1).orElse(null);
        if (CommonUtil.isNullOrEmpty(startDate)) {
            sb.append("start_date, ");
            logger.warn("Mandatory field missing. Field name - start_date");
        } else {
            obj.startDate(DateFormatUtil.convertStringToLocalDate(startDate, CmsConstants.DATE_FORMAT_dd_MM_yyyy));
        }

        String endDate = row.getCellAsString(2).orElse(null);
        if (CommonUtil.isNullOrEmpty(endDate)) {
            sb.append("end_date, ");
            logger.warn("Mandatory field missing. Field name - end_date");
        } else {
            obj.endDate(DateFormatUtil.convertStringToLocalDate(endDate, CmsConstants.DATE_FORMAT_dd_MM_yyyy));
        }

        String termStatus = row.getCellAsString(3).orElse(null);
        if (CommonUtil.isNullOrEmpty(termStatus)) {
            sb.append("status, ");
            logger.warn("Mandatory field missing. Field name - term_status");
        } else {
            obj.setStatus(termStatus);
        }

        if(sb.length() > 0) {
            String msg = "Field name - ";
            throw new MandatoryFieldMissingException(msg+sb.substring(0, sb.lastIndexOf(",")));
        }

        if(this.allRepositories.findRepository(this.sheetName).exists(Example.of(obj))) {
        	  String msg = "Application already have a term with the given given fields in the given academic year : "+academicYear;
              sb.append(msg+", ");
              logger.warn(msg);
              if (sb.length() > 0) {
                  throw new AdditionalTermFoundException(msg);
              }
        }
        
        return  (T) obj;
    }
}
