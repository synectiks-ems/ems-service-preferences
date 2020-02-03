package com.synectiks.pref.dataimport.loader;

import java.util.Optional;

import org.dhatim.fastexcel.reader.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.synectiks.pref.dataimport.AllRepositories;
import com.synectiks.pref.dataimport.DataLoader;
import com.synectiks.pref.domain.Batch;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.Department;
import com.synectiks.pref.domain.enumeration.BatchEnum;
import com.synectiks.pref.exceptions.AdditionalBatchFoundException;
import com.synectiks.pref.exceptions.MandatoryFieldMissingException;
import com.synectiks.pref.service.util.CommonUtil;

public class BatchDataLoader extends DataLoader {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private AllRepositories allRepositories;
    private String sheetName;

    public BatchDataLoader(String sheetName, AllRepositories allRepositories) {
        super(sheetName, allRepositories);
        this.allRepositories = allRepositories;
        this.sheetName = sheetName;
    }

    @Override
    public <T> T getObject(Row row, Class<T> cls) throws InstantiationException, IllegalAccessException, AdditionalBatchFoundException , MandatoryFieldMissingException {
        StringBuilder sb = new StringBuilder();

        Batch obj = CommonUtil.createCopyProperties(cls.newInstance(), Batch.class);
        
        String batchName = row.getCellAsString(0).orElse(null);
        if(!CommonUtil.isNullOrEmpty(batchName)) {
        	BatchEnum be = findBatch(batchName);
            if(be == null) {
            	sb.append("batch, ");
                logger.warn("Mandatory field missing. Field name - batch");
            }else {
            	obj.setBatch(be);
            }
        }else {
        	sb.append("batch, ");
            logger.warn("Mandatory field missing. Field name - batch");
        }
        
        String departmentName = row.getCellAsString(1).orElse(null);
        if(CommonUtil.isNullOrEmpty(departmentName)) {
            sb.append("department_id, ");
            logger.warn("Mandatory field missing. Field name - department_id");
        }else {
        	String branchName = row.getCellAsString(2).orElse(null);
            String branchAddress = row.getCellAsString(3).orElse(null);
            if(CommonUtil.isNullOrEmpty(branchName) || CommonUtil.isNullOrEmpty(branchAddress)) {
            	sb.append("department_id, ");
                logger.warn("Either branch name or branch address not provided, Cannot identify that given department belongs to which branch");
            }else {
                Branch branch = new Branch();
                branch.setBranchName(branchName);
                branch.address(branchAddress);
                Optional<Branch> b = this.allRepositories.findRepository("branch").findOne(Example.of(branch));
                
                if(b.isPresent()) {
                	Department department = new Department();
                    department.setName(departmentName);
                    department.setBranch(b.get());
                    Optional<Department> dp = this.allRepositories.findRepository("department").findOne(Example.of(department));
                    if(dp.isPresent()) {
                        obj.setDepartment(dp.get());
                    }else {
                        sb.append("department_id, ");
                        logger.warn("Department not found. Given department name : " + departmentName);
                    }
                }else {
                	sb.append("department_id, ");
                    logger.warn("Either branch name or branch address not provided, Cannot identify that given department belongs to which branch");
                }
            }
        }
        
        if (this.allRepositories.findRepository(this.sheetName).exists(Example.of(obj))) {
        	String msg = "Batch and department combination already exists";
        	sb.append(msg+",");
            logger.warn(msg);
            if (sb.length() > 0) {
                throw new AdditionalBatchFoundException(msg);
            }
        }
        
        if (sb.length() > 0) {
            String msg = "Field name - ";
            throw new MandatoryFieldMissingException(msg + sb.substring(0, sb.lastIndexOf(",")));
        }

        return (T)obj;
    }
    
    private BatchEnum findBatch(String batchName) {
    	if(BatchEnum.FIRSTYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.FIRSTYEAR;
    	}else if(BatchEnum.SECONDYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.SECONDYEAR;
    	}else if(BatchEnum.THIRDYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.THIRDYEAR;
    	}else if(BatchEnum.FOURTHYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.FOURTHYEAR;
    	}else if(BatchEnum.FIFTHYEAR.toString().equalsIgnoreCase(batchName)) {
    		return BatchEnum.FIFTHYEAR;
    	}
    	return null;
    }
    
}
