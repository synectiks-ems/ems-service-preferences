package com.synectiks.pref.business.service;

import java.time.LocalDate;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.synectiks.pref.config.Constants;
import com.synectiks.pref.constant.CmsConstants;
import com.synectiks.pref.domain.AuthorizedSignatory;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.vo.CmsAuthorizedSignatoryVo;
import com.synectiks.pref.domain.vo.CmsBranchVo;
import com.synectiks.pref.graphql.types.authorizedsignatory.AuthorizedSignatoryInput;
import com.synectiks.pref.repository.AuthorizedSignatoryRepository;
import com.synectiks.pref.repository.BranchRepository;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.service.util.DateFormatUtil;

@Component
public class CmsAuthorizedSignatoryService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    AuthorizedSignatoryRepository authorizedSignatoryRepository;

    public List<AuthorizedSignatory> getAuthorizedSignatoryListOnFilterCriteria(Map<String, String> criteriaMap){
        AuthorizedSignatory obj = new AuthorizedSignatory();
        boolean isFilter = false;
        if(criteriaMap.get("id") != null) {
            obj.setId(Long.parseLong(criteriaMap.get("id")));
            isFilter = true;
        }
        if(criteriaMap.get("name") != null) {
            obj.setName(criteriaMap.get("name"));
            isFilter = true;
        }
        List<AuthorizedSignatory> list = null;
        if(isFilter) {
            list = this.authorizedSignatoryRepository.findAll(Example.of(obj), Sort.by(Sort.Direction.DESC, "id"));
        }else {
            list = this.authorizedSignatoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        }

        Collections.sort(list, (o1, o2) -> o2.getId().compareTo(o1.getId()));
        return list;
    }


    public List<CmsAuthorizedSignatoryVo> getAuthorizedSignatoryList(){
//    	AuthorizedSignatory as = new AuthorizedSignatory();
//    	Optional<Branch> oBranch = branchRepository.findAll();
//    	if(oBranch.isPresent()) {
//    		as.setBranch(oBranch.get());
//    	}else {
//    		logger.warn("Branch not found for the given branch id: "+branchId+". Returning empty authorizedSignatory list.");
//    		return Collections.emptyList();
//    	}

    	List<AuthorizedSignatory> list = this.authorizedSignatoryRepository.findAll();
    	List<CmsAuthorizedSignatoryVo> ls = changeAuthorizedSignatoryToCmsAuthorizedSignatoryList(list);
    	Collections.sort(ls, (o1, o2) -> o2.getId().compareTo(o1.getId()));
    	return ls;
    }

    private List<CmsAuthorizedSignatoryVo> changeAuthorizedSignatoryToCmsAuthorizedSignatoryList(List<AuthorizedSignatory> list){
    	List<CmsAuthorizedSignatoryVo> ls = new ArrayList<>();
    	for(AuthorizedSignatory as: list) {
    		CmsAuthorizedSignatoryVo vo = CommonUtil.createCopyProperties(as, CmsAuthorizedSignatoryVo.class);

    		if(as.getCreatedOn() != null) {
            	vo.setStrCreatedOn(DateFormatUtil.changeLocalDateFormat(as.getCreatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            	vo.setCreatedOn(null);
            }
            if(as.getUpdatedOn() != null) {
            	vo.setStrUpdatedOn(DateFormatUtil.changeLocalDateFormat(as.getUpdatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
            	vo.setUpdatedOn(null);
            }
            if(as.getBranch() != null) {
            	vo.setBranchId(as.getBranch().getId());
            	CmsBranchVo cmsBvo =CommonUtil.createCopyProperties(as.getBranch(), CmsBranchVo.class);
        		vo.setCmsBranchVo(cmsBvo);
            }
            ls.add(vo);
    	}
    	return ls;
    }

    public CmsAuthorizedSignatoryVo saveAuthorizedSignatory(AuthorizedSignatoryInput cmsAuthorizedSignatoryVo) {
    	logger.info("Saving authorizedSignatory");
    	CmsAuthorizedSignatoryVo vo = null;
    	try {
    		AuthorizedSignatory authorizedSignatory = null;
    		if(cmsAuthorizedSignatoryVo.getId() == null) {
    			logger.debug("Adding new authorizedSignatory");
    			authorizedSignatory = CommonUtil.createCopyProperties(cmsAuthorizedSignatoryVo, AuthorizedSignatory.class);
    			authorizedSignatory.setCreatedOn(LocalDate.now());
    		}else {
    			logger.debug("Updating existing authorizedSignatory");
    			authorizedSignatory = this.authorizedSignatoryRepository.findById(cmsAuthorizedSignatoryVo.getId()).get();
    			authorizedSignatory.setUpdatedOn(LocalDate.now());
    			authorizedSignatory.setName(cmsAuthorizedSignatoryVo.getName());
    			authorizedSignatory.setDesignation(cmsAuthorizedSignatoryVo.getDesignation());
    			authorizedSignatory.setAddress(cmsAuthorizedSignatoryVo.getAddress());
    			authorizedSignatory.setEmailId(cmsAuthorizedSignatoryVo.getEmailId());
    			authorizedSignatory.setCellPhoneNumber(cmsAuthorizedSignatoryVo.getCellPhoneNumber());
    			authorizedSignatory.setPanNo(cmsAuthorizedSignatoryVo.getPanNo());
    			authorizedSignatory.setStatus(cmsAuthorizedSignatoryVo.getStatus());
    		}
        	Optional<Branch> oBranch = branchRepository.findById(cmsAuthorizedSignatoryVo.getBranchId());
        	if(oBranch.isPresent()) {
        		authorizedSignatory.setBranch(oBranch.get());
        	}

        	authorizedSignatory = authorizedSignatoryRepository.save(authorizedSignatory);
        	vo = CommonUtil.createCopyProperties(authorizedSignatory, CmsAuthorizedSignatoryVo.class);
        	vo.setStrCreatedOn(authorizedSignatory.getCreatedOn() != null ? DateFormatUtil.changeLocalDateFormat(authorizedSignatory.getCreatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
        	vo.setStrUpdatedOn(authorizedSignatory.getUpdatedOn() != null ? DateFormatUtil.changeLocalDateFormat(authorizedSignatory.getUpdatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
        	vo.setCreatedOn(null);
        	vo.setUpdatedOn(null);
        	if(oBranch.isPresent()) {
        		CmsBranchVo cmsBvo =CommonUtil.createCopyProperties(oBranch.get(), CmsBranchVo.class);
        		vo.setCmsBranchVo(cmsBvo);
        	}

        	vo.setExitCode(0L);
        	if(cmsAuthorizedSignatoryVo.getId() == null) {
        		vo.setExitDescription("AuthorizedSignatory is added successfully");
        		logger.debug("AuthorizedSignatory is added successfully");
        	}else {
        		vo.setExitDescription("AuthorizedSignatory is updated successfully");
        		logger.debug("AuthorizedSignatory is updated successfully");
        	}

        }catch(Exception e) {
        	vo = new CmsAuthorizedSignatoryVo();
        	vo.setExitCode(1L);
        	vo.setExitDescription("Due to some exception, authorizedSignatory data could not be saved");
    		logger.error("AuthorizedSignatory save failed. Exception : ",e);
    	}
    	logger.info("AuthorizedSignatory saved successfully");
    	List ls =  getAuthorizedSignatoryList();
        vo.setDataList(ls);
        return vo;
    }
}
