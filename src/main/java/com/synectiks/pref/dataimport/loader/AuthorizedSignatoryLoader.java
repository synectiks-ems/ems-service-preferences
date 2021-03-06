package com.synectiks.pref.dataimport.loader;

import java.util.Optional;

import org.dhatim.fastexcel.reader.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;

import com.synectiks.pref.dataimport.AllRepositories;
import com.synectiks.pref.dataimport.DataLoader;
import com.synectiks.pref.domain.AuthorizedSignatory;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.service.util.CommonUtil;


public class AuthorizedSignatoryLoader extends DataLoader {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private AllRepositories allRepositories;
	private String sheetName;
	
	public AuthorizedSignatoryLoader(String sheetName, AllRepositories allRepositories) {
		super(sheetName, allRepositories);
		this.allRepositories = allRepositories;
		this.sheetName = sheetName;
	}
	
	@Override
	public <T> T getObject(Row row, Class<T> cls) throws InstantiationException, IllegalAccessException {
		StringBuilder sb = new StringBuilder();
		AuthorizedSignatory obj = CommonUtil.createCopyProperties(cls.newInstance(), AuthorizedSignatory.class);
		
		String name = row.getCellAsString(0).orElse(null);
        if(CommonUtil.isNullOrEmpty(name)) {
            sb.append("name, ");
            logger.warn("Mandatory field missing. Field name - name");
        }else {
            obj.setName(name);
        }
        
        String designation = row.getCellAsString(1).orElse(null);
        if(CommonUtil.isNullOrEmpty(designation)) {
            sb.append("designation, ");
            logger.warn("Mandatory field missing. Field name - designation");
        }else {
        	obj.setDesignation(designation);
        }
        
        String address = row.getCellAsString(2).orElse(null);
        if(CommonUtil.isNullOrEmpty(address)) {
            sb.append("address, ");
            logger.warn("Mandatory field missing. Field name - address");
        }else {
        	obj.setAddress(address);
        }
        
        String emailId = row.getCellAsString(3).orElse(null);
        if(CommonUtil.isNullOrEmpty(emailId)) {
            sb.append("email_id, ");
            logger.warn("Mandatory field missing. Field name - email_id");
        }else {
        	obj.setEmailId(emailId);
        }
        
        String cellPhoneNo = row.getCellAsString(4).orElse(null);
        if(CommonUtil.isNullOrEmpty(cellPhoneNo)) {
            sb.append("cell_phone_number, ");
            logger.warn("Mandatory field missing. Field name - cell_phone_number");
        }else {
        	obj.setCellPhoneNumber(cellPhoneNo);
        }
        
        String panNo = row.getCellAsString(5).orElse(null);
        if(CommonUtil.isNullOrEmpty(panNo)) {
            sb.append("pan_no, ");
            logger.warn("Mandatory field missing. Field name - pan_no");
        }else {
        	obj.setPanNo(panNo);
        }
        
        String branchName = row.getCellAsString(6).orElse(null);
		if(CommonUtil.isNullOrEmpty(branchName)) {
			sb.append("branch_id, ");
            logger.warn("Mandatory field missing. Field name - branch_id");
		}else {
			Branch branch = new Branch();
            branch.setBranchName(branchName);
            Optional<Branch> b = this.allRepositories.findRepository("branch").findOne(Example.of(branch));
            if(b.isPresent()) {
                obj.setBranch(b.get());
            }else {
                sb.append("branch_id, ");
                logger.warn("Branch not found. Given branch name : "+branchName);
            }
		}

		String status = row.getCellAsString(7).orElse(null);
		if(CommonUtil.isNullOrEmpty(status)) {
			sb.append("status, ");
			logger.warn("Mandatory field missing. Field name - status");
		}else {
			obj.setStatus(status);
		}
		
		return (T)obj;
	}
	
}
