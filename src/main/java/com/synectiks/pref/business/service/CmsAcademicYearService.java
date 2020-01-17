package com.synectiks.pref.business.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.synectiks.pref.constant.CmsConstants;
import com.synectiks.pref.domain.AcademicYear;
import com.synectiks.pref.domain.vo.CmsAcademicYearVo;
import com.synectiks.pref.graphql.types.academicyear.AcademicYearInput;
import com.synectiks.pref.repository.AcademicYearRepository;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.service.util.DateFormatUtil;

@Component
public class CmsAcademicYearService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
    @Autowired
    AcademicYearRepository academicYearRepository;

    public List<CmsAcademicYearVo> getCmsAcademicYearListOnFilterCriteria(Map<String, String> criteriaMap){
    	AcademicYear ay = new AcademicYear();
    	boolean isFilter = false;
    	if(criteriaMap.get("id") != null) {
    		ay.setId(Long.parseLong(criteriaMap.get("id")));
    		isFilter = true;
    	}
    	if(criteriaMap.get("status") != null) {
    		ay.setStatus(criteriaMap.get("status"));
    		isFilter = true;
    	}
    	if(criteriaMap.get("description") != null) {
    		ay.setDescription(criteriaMap.get("description"));
    		isFilter = true;
    	}
    	if(criteriaMap.get("comments") != null) {
    		ay.setComments(criteriaMap.get("comments"));
    		isFilter = true;
    	}
    	
    	List<AcademicYear> list = null;
    	if(isFilter) {
    		list = this.academicYearRepository.findAll(Example.of(ay), Sort.by(Direction.DESC, "id"));
    	}else {
    		list = this.academicYearRepository.findAll(Sort.by(Direction.DESC, "id"));
    	}
        
    	List<CmsAcademicYearVo> ls = changeAcademicYearToCmsAcademicYearList(list);
    	Collections.sort(ls, (o1, o2) -> o2.getId().compareTo(o1.getId()));
    	return ls;
    }
    
    public List<AcademicYear> getAcademicYearListOnFilterCriteria(Map<String, String> criteriaMap){
    	AcademicYear ay = new AcademicYear();
    	boolean isFilter = false;
    	if(criteriaMap.get("id") != null) {
    		ay.setId(Long.parseLong(criteriaMap.get("id")));
    		isFilter = true;
    	}
    	if(criteriaMap.get("status") != null) {
    		ay.setStatus(criteriaMap.get("status"));
    		isFilter = true;
    	}
    	if(criteriaMap.get("description") != null) {
    		ay.setDescription(criteriaMap.get("description"));
    		isFilter = true;
    	}
    	if(criteriaMap.get("comments") != null) {
    		ay.setComments(criteriaMap.get("comments"));
    		isFilter = true;
    	}
    	
    	List<AcademicYear> list = null;
    	if(isFilter) {
    		list = this.academicYearRepository.findAll(Example.of(ay), Sort.by(Direction.DESC, "id"));
    	}else {
    		list = this.academicYearRepository.findAll(Sort.by(Direction.DESC, "id"));
    	}
        
    	return list;
    }
    
    public List<AcademicYear> getAcademicYearList(String status) {
    	AcademicYear academicYear = new AcademicYear();
        academicYear.setStatus(status);
        List<AcademicYear> list = this.academicYearRepository.findAll(Example.of(academicYear), Sort.by(Direction.DESC, "id"));
        logger.debug("Academic year list for the given status : "+status+". List : "+list);
        return list;
    }
    
    public List<AcademicYear> getAcademicYearList(){
    	List<AcademicYear> list = this.academicYearRepository.findAll(Sort.by(Direction.DESC, "id"));
    	logger.debug("Academic year list : "+list);
        return list;
    }
    
    public AcademicYear getAcademicYear(Long id){
    	Optional<AcademicYear> ay = this.academicYearRepository.findById(id);
    	if(ay.isPresent()) {
    		logger.debug("Academic year object found for given id : "+id+". Academic year object : "+ay.get());
        	return ay.get();
    	}
    	logger.debug("AcademicYear object not found for the given id. "+id+". Returning null");
        return null;
    }
    
    public List<CmsAcademicYearVo> getCmsAcademicYearList(String status) {
    	AcademicYear academicYear = new AcademicYear();
        academicYear.setStatus(status);
        List<AcademicYear> list = this.academicYearRepository.findAll(Example.of(academicYear));
        List<CmsAcademicYearVo> ls = changeAcademicYearToCmsAcademicYearList(list);
    	Collections.sort(ls, (o1, o2) -> o2.getId().compareTo(o1.getId()));
    	logger.debug("CmsAcademic year list for the given status : "+status+". List : "+list);
        return ls;
    }
    
    public List<CmsAcademicYearVo> getCmsAcademicYearList(){
    	List<AcademicYear> list = this.academicYearRepository.findAll();
    	List<CmsAcademicYearVo> ls = changeAcademicYearToCmsAcademicYearList(list);
    	Collections.sort(ls, (o1, o2) -> o2.getId().compareTo(o1.getId()));
    	logger.debug("CmsAcademic year list : "+list);
    	return ls;
    }
    
    public CmsAcademicYearVo getCmsAcademicYear(Long id){
    	Optional<AcademicYear> ay = this.academicYearRepository.findById(id);
    	if(ay.isPresent()) {
    		CmsAcademicYearVo vo = CommonUtil.createCopyProperties(ay.get(), CmsAcademicYearVo.class);
    		convertDatesAndProvideDependencies(ay.get(), vo);
    		logger.debug("CmsAcademic year for given id : "+id+". CmsAcademic year object : "+vo);
        	return vo;
    	}
    	logger.debug("AcademicYear object not found for the given id. "+id+". Returning null object");
        return null;
    }
    
    private List<CmsAcademicYearVo> changeAcademicYearToCmsAcademicYearList(List<AcademicYear> list){
    	List<CmsAcademicYearVo> ls = new ArrayList<>();
    	for(AcademicYear ay: list) {
    		CmsAcademicYearVo vo = CommonUtil.createCopyProperties(ay, CmsAcademicYearVo.class);
    		convertDatesAndProvideDependencies(ay, vo);
            
    		ls.add(vo);
    	}
    	return ls;
    }

	private void convertDatesAndProvideDependencies(AcademicYear ay, CmsAcademicYearVo vo) {
		if(ay.getStartDate() != null) {
			vo.setStrStartDate(DateFormatUtil.changeLocalDateFormat(ay.getStartDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            	vo.setStartDate(null);
		}
		if(ay.getEndDate() != null) {
			vo.setStrEndDate(DateFormatUtil.changeLocalDateFormat(ay.getEndDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            	vo.setEndDate(null);
		}
		if(ay.getCreatedOn() != null) {
			vo.setStrCreatedOn(DateFormatUtil.changeLocalDateFormat(ay.getCreatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            	vo.setCreatedOn(null);
		}
		if(ay.getUpdatedOn() != null) {
			vo.setStrUpdatedOn(DateFormatUtil.changeLocalDateFormat(ay.getUpdatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy));
//            	vo.setUpdatedOn(null);
		}
		vo.setYear(vo.getDescription());
	}
    
    public CmsAcademicYearVo saveAcademicYear(AcademicYearInput input) {
    	logger.info("Saving academicYear");
    	CmsAcademicYearVo vo = null;
    	try {
    		AcademicYear academicYear = null;
    		if(input.getId() == null) {
    			logger.debug("Adding new academicYear");
    			academicYear = CommonUtil.createCopyProperties(input, AcademicYear.class);
    			academicYear.setCreatedOn(LocalDate.now());
    		}else {
    			logger.debug("Updating existing academicYear");
    			academicYear = this.academicYearRepository.findById(input.getId()).get();
    			academicYear.setUpdatedOn(LocalDate.now());
    		}
    		academicYear.setDescription(input.getDescription());
    		academicYear.setComments(input.getComments());
    		academicYear.setStatus(input.getStatus());
    		academicYear.setStartDate(input.getStrStartDate() != null ? DateFormatUtil.convertStringToLocalDate(input.getStrStartDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : null);
			academicYear.setEndDate(input.getStrEndDate() != null ? DateFormatUtil.convertStringToLocalDate(input.getStrEndDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : null);
			
    		academicYear = academicYearRepository.save(academicYear);
        	
        	vo = CommonUtil.createCopyProperties(academicYear, CmsAcademicYearVo.class);
        	vo.setStrCreatedOn(academicYear.getCreatedOn() != null ? DateFormatUtil.changeLocalDateFormat(academicYear.getCreatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
        	vo.setStrUpdatedOn(academicYear.getUpdatedOn() != null ? DateFormatUtil.changeLocalDateFormat(academicYear.getUpdatedOn(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
        	vo.setStrStartDate(academicYear.getStartDate() != null ? DateFormatUtil.changeLocalDateFormat(academicYear.getStartDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
        	vo.setStrUpdatedOn(academicYear.getEndDate() != null ? DateFormatUtil.changeLocalDateFormat(academicYear.getEndDate(), CmsConstants.DATE_FORMAT_dd_MM_yyyy) : "");
        	
        	vo.setCreatedOn(null);
        	vo.setUpdatedOn(null);
        	vo.setExitCode(0L);
        	if(input.getId() == null) {
        		vo.setExitDescription("AcademicYear is added successfully");
        		logger.debug("AcademicYear is added successfully");
        	}else {
        		vo.setExitDescription("AcademicYear is updated successfully");
        		logger.debug("AcademicYear is updated successfully");
        	}
        	
        }catch(Exception e) {
        	vo = new CmsAcademicYearVo();
        	vo.setExitCode(1L);
        	vo.setExitDescription("Due to some exception, academicYear data not be saved");
    		logger.error("AcademicYear save failed. Exception : ",e);
    	}
    	logger.info("AcademicYear saved successfully");
    	List ls =  getCmsAcademicYearList();
        vo.setDataList(ls);
    	return vo;
        
    }
}
