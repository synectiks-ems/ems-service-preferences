package com.synectiks.pref.ems.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.business.service.CommonService;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.Department;
import com.synectiks.pref.domain.Teacher;
import com.synectiks.pref.domain.enumeration.Status;
import com.synectiks.pref.domain.vo.CmsTeacherVo;
import com.synectiks.pref.repository.BranchRepository;
import com.synectiks.pref.repository.DepartmentRepository;
import com.synectiks.pref.repository.TeacherRepository;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Teacher.
 */
@RestController
@RequestMapping("/api")
public class TeacherRestController {

    private final Logger log = LoggerFactory.getLogger(TeacherRestController.class);

    private static final String ENTITY_NAME = "teacher";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    
    @Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private BranchRepository branchRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CommonService commonService;
    
    @RequestMapping(method = RequestMethod.POST, value = "/cmsteachers")
    public ResponseEntity<CmsTeacherVo> createTeacher(@Valid @RequestBody CmsTeacherVo cmsTeacherVo) throws URISyntaxException {
        log.debug("REST request to save a teacher : {}", cmsTeacherVo);
        if (cmsTeacherVo.getId() != null) {
            throw new BadRequestAlertException("A new teacher cannot already have an ID which already exists", ENTITY_NAME, "idexists");
        }
        Teacher th = CommonUtil.createCopyProperties(cmsTeacherVo, Teacher.class);
        if(cmsTeacherVo.getDepartmentId() != null) {
        	Optional<Department> dt = this.departmentRepository.findById(cmsTeacherVo.getDepartmentId());
        	if(dt.isPresent()) {
        		th.setDepartment(dt.get());
        	}
        }
        if(cmsTeacherVo.getBranchId() != null) {
        	Optional<Branch> bt = this.branchRepository.findById(cmsTeacherVo.getBranchId());
        	if(bt.isPresent()) {
        		th.setBranch(bt.get());
        	}
        }
        th = this.teacherRepository.save(th);
        cmsTeacherVo = CommonUtil.createCopyProperties(th, CmsTeacherVo.class);
        
        return ResponseEntity.created(new URI("/api/teachers/" + cmsTeacherVo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cmsTeacherVo.getId().toString()))
            .body(cmsTeacherVo);
    }

    
    @RequestMapping(method = RequestMethod.PUT, value = "/cmsteachers")
    public ResponseEntity<CmsTeacherVo> updateTeacher(@Valid @RequestBody CmsTeacherVo cmsTeacherVo) throws URISyntaxException {
        log.debug("REST request to update a teacher : {}", cmsTeacherVo);
        if (cmsTeacherVo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Teacher th = CommonUtil.createCopyProperties(cmsTeacherVo, Teacher.class);
        if(cmsTeacherVo.getDepartmentId() != null) {
        	Optional<Department> dt = this.departmentRepository.findById(cmsTeacherVo.getDepartmentId());
        	if(dt.isPresent()) {
        		th.setDepartment(dt.get());
        	}
        }
        if(cmsTeacherVo.getBranchId() != null) {
        	Optional<Branch> bt = this.branchRepository.findById(cmsTeacherVo.getBranchId());
        	if(bt.isPresent()) {
        		th.setBranch(bt.get());
        	}
        }
        th = this.teacherRepository.save(th);
        cmsTeacherVo = CommonUtil.createCopyProperties(th, CmsTeacherVo.class);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmsTeacherVo.getId().toString()))
            .body(cmsTeacherVo);
    }

    
    @RequestMapping(method = RequestMethod.GET, value = "/cmsteachers")
    public List<CmsTeacherVo> getAllTeachers() {
        log.debug("REST request to get all the Teachers");
        List<Teacher> list =  this.teacherRepository.findAll();
        List<CmsTeacherVo> ls = new ArrayList<>();
        for(Teacher th: list) {
        	CmsTeacherVo vo = CommonUtil.createCopyProperties(th, CmsTeacherVo.class);
        	ls.add(vo);
        }
        return ls;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cmsteachers-qryprms")
    public List<CmsTeacherVo> getAllTeachersByDeptBranchId(@RequestParam Map<String, String> dataMap) {
        log.debug("REST request to get all the Teachers");
        Teacher th = new Teacher();
    	
		if (dataMap.containsKey("deptId")) {
			String deptId = dataMap.get("deptId");
			Department dt = commonService.getDepartmentById(Long.parseLong(deptId));
			th.setDepartment(dt);
		}
		if (dataMap.containsKey("branchId")) {
			String branchId = dataMap.get("branchId");
			Optional<Branch> bh = this.branchRepository.findById(Long.parseLong(branchId));
			if(bh.isPresent()) {
				th.setBranch(bh.get());
			}
		}
		
		List<Teacher> list = null;
		if(dataMap.size() > 0) {
			Example<Teacher> example = Example.of(th);
			list = this.teacherRepository.findAll(example);
		}else {
			list = Collections.emptyList();
		}
		
		List<CmsTeacherVo> ls = new ArrayList<>();
        for(Teacher thr: list) {
        	CmsTeacherVo vo = CommonUtil.createCopyProperties(thr, CmsTeacherVo.class);
        	ls.add(vo);
        }
        return ls;
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = "/cmsteachers/{id}")
    public ResponseEntity<CmsTeacherVo> getTeacher(@PathVariable Long id) {
        log.debug("REST request to get a Teacher : {}", id);
        Optional<Teacher> teacher = this.teacherRepository.findById(id);
        CmsTeacherVo vo = null;
        if(teacher.isPresent()) {
        	Teacher th = teacher.get();
        	vo = CommonUtil.createCopyProperties(th,  CmsTeacherVo.class);
        }else {
        	vo = new  CmsTeacherVo();
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(vo));
    }

    
    @RequestMapping(method = RequestMethod.DELETE, value = "/cmsteachers/{id}")
    public Integer deleteTeacher(@PathVariable Long id) {
        log.debug("REST request to delete a Teacher : {}", id);
        try {
        	Teacher th = new Teacher();
            th.setId(id);
            th.setStatus(Status.DEACTIVE);
            this.teacherRepository.save(th);
        }catch(Exception e) {
    		return HttpStatus.FAILED_DEPENDENCY.value();
    	}
        return HttpStatus.OK.value();
    }

    

}
