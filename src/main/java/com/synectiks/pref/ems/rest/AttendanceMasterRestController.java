package com.synectiks.pref.ems.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.business.service.CommonService;
import com.synectiks.pref.domain.AttendanceMaster;
import com.synectiks.pref.domain.Batch;
import com.synectiks.pref.domain.Department;
import com.synectiks.pref.domain.Section;
import com.synectiks.pref.domain.Teach;
import com.synectiks.pref.repository.AttendanceMasterRepository;
import com.synectiks.pref.repository.DepartmentRepository;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;
import com.synectiks.pref.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing AttendanceMaster.
 */
@RestController
@RequestMapping("/api")
public class AttendanceMasterRestController {

    private final Logger logger = LoggerFactory.getLogger(AttendanceMasterRestController.class);

    private static final String ENTITY_NAME = "attendanceMaster";

    
    private String applicationName;
    
    @Autowired
    private AttendanceMasterRepository attendanceMasterRepository;

    @Autowired
    private CommonService commonService;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/cmsattendance-masters")
    public ResponseEntity<AttendanceMaster> createAttendanceMaster(@RequestBody AttendanceMaster attendanceMaster) throws URISyntaxException {
        logger.debug("REST request to save an AttendanceMaster : {}", attendanceMaster);
        if (attendanceMaster.getId() != null) {
            throw new BadRequestAlertException("A new attendanceMaster cannot have an ID which already exists", ENTITY_NAME, "idexists");
        }
        attendanceMaster = attendanceMasterRepository.save(attendanceMaster);
        return ResponseEntity.created(new URI("/api/attendance-masters/" + attendanceMaster.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, attendanceMaster.getId().toString()))
            .body(attendanceMaster);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cmsattendance-masters")
    public ResponseEntity<AttendanceMaster> updateAttendanceMaster(@RequestBody AttendanceMaster attendanceMaster) throws URISyntaxException {
        logger.debug("REST request to update an AttendanceMaster : {}", attendanceMaster);
        if (attendanceMaster.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        attendanceMaster = attendanceMasterRepository.save(attendanceMaster);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, attendanceMaster.getId().toString()))
            .body(attendanceMaster);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cmsattendance-masters")
    public List<AttendanceMaster> getAllAttendanceMasters() {
        logger.debug("REST request to get all AttendanceMasters");
        return attendanceMasterRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cmsattendance-masters/{id}")
    public ResponseEntity<AttendanceMaster> getAttendanceMaster(@PathVariable Long id) {
        logger.debug("REST request to get an AttendanceMaster : {}", id);
        Optional<AttendanceMaster> attendanceMaster = attendanceMasterRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(attendanceMaster);
    }


    @RequestMapping(method = RequestMethod.DELETE, value = "/cmsattendance-masters/{id}")
    public ResponseEntity<Void> deleteAttendanceMaster(@PathVariable Long id) {
        logger.debug("REST request to delete AttendanceMaster : {}", id);
        attendanceMasterRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cmsattendance-masters-bybatchsectionteach")
    public ResponseEntity<AttendanceMaster> getAttendanceMasterByBatchSectionTeach(@RequestParam Map<String, String> dataMap) {
    	if (!dataMap.containsKey("batchId")) {
            throw new BadRequestAlertException("Batch id not present", ENTITY_NAME, "batch id null");
        }
    	if (!dataMap.containsKey("sectionId")) {
            throw new BadRequestAlertException("Section id not present", ENTITY_NAME, "section id null");
        }
    	if (!dataMap.containsKey("teachId")) {
            throw new BadRequestAlertException("Teach id not present", ENTITY_NAME, "teach id null");
        }
    	String batchId = dataMap.get("batchId");
    	String sectionId = dataMap.get("sectionId");
    	String teachId = dataMap.get("teachId");
    	logger.debug("Getting attendance master id for batch id : "+batchId+", section id : "+sectionId+" and teach id: "+teachId);
    	Batch bt = this.commonService.getBatchById(Long.parseLong(batchId));
    	Section sc = this.commonService.getSectionById(Long.parseLong(sectionId));
    	Teach th = this.commonService.getTeachById(Long.parseLong(teachId));
    	AttendanceMaster am = this.commonService.getAttendanceMasterByBatchSectionTeach(bt, sc, th);
    	logger.debug("AttendanceMaster : "+am);
    	return ResponseUtil.wrapOrNotFound(Optional.of(am));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cmsattendance-masters-bybatchsection")
    public List<AttendanceMaster> getAllAttendanceMasterByBatchSection(@RequestParam Map<String, String> dataMap) {
    	logger.debug("Getting attendance master");
    	
    	String departmentId = dataMap.get("departmentId");
    	String batchId = dataMap.get("batchId");
    	String sectionId = dataMap.get("sectionId");
    	
    	Long dptId = Long.parseLong(departmentId);
    	
    	Long bId = 0L;
    	if(!CommonUtil.isNullOrEmpty(batchId) && !"null".equalsIgnoreCase(batchId) && !"undefined".equalsIgnoreCase(batchId)) {
    		bId = Long.parseLong(batchId);
    	}
    	
    	Long scId = 0L;
    	if(!CommonUtil.isNullOrEmpty(sectionId) && !"null".equalsIgnoreCase(sectionId) && !"undefined".equalsIgnoreCase(sectionId)) {
    		scId = Long.parseLong(sectionId);
    	}
    	List<AttendanceMaster> list = this.commonService.getAttendanceMastersList(dptId, bId, scId);
    	logger.debug("AttendanceMaster list : "+list);
    	return list;
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/cmsattendance-masters-bydepartmentid")
    public List<AttendanceMaster> getAttendanceMasterByDepartment(@RequestParam Map<String, String> dataMap) {
    	logger.debug("Getting attendance master");
    	
    	String departmentId = dataMap.get("departmentId");
    	Long dptId = Long.parseLong(departmentId);
    	Department dept = new Department();
    	dept.setId(dptId);
    	List<Department> listDept = this.departmentRepository.findAll(Example.of(dept));
    	
    	List<Batch> batchList = this.commonService.getBatchForCriteria(listDept);
    	List<Section> emptyList = new ArrayList<>();
    	List<AttendanceMaster> list = this.commonService.getAttendanceMasterForCriteria(batchList, emptyList);
    	logger.debug("AttendanceMaster list : "+list);
    	return list;
    }
    
}
