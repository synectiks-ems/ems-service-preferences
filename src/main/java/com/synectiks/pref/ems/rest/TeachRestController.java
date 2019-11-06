package com.synectiks.pref.ems.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.business.service.CommonService;
import com.synectiks.pref.domain.Teach;
import com.synectiks.pref.repository.TeachRepository;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing Teach.
 */
@RestController
@RequestMapping("/api")
public class TeachRestController {

    private final Logger logger = LoggerFactory.getLogger(TeachRestController.class);

    private static final String ENTITY_NAME = "teach";
    
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    
    @Autowired
    private TeachRepository teachRepository;
    
    @Autowired
    private CommonService commonService;
    
    @RequestMapping(method = RequestMethod.POST, value = "/cmsteaches")
    public ResponseEntity<Teach> createTeach(@RequestBody Teach teach) throws URISyntaxException {
        logger.debug("REST request to save a Teach : {}", teach);
        if (teach.getId() != null) {
            throw new BadRequestAlertException("A new teach cannot already have an ID", ENTITY_NAME, "idexists");
        }
        teach = this.teachRepository.save(teach);
        return ResponseEntity.created(new URI("/api/teaches/" + teach.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, teach.getId().toString()))
            .body(teach);
    }

    
    @RequestMapping(method = RequestMethod.PUT, value = "/cmsteaches")
    public ResponseEntity<Teach> updateTeach(@RequestBody Teach teach) throws URISyntaxException {
        logger.debug("REST request to update a Teach : {}", teach);
        if (teach.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        teach = teachRepository.save(teach);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, teach.getId().toString()))
            .body(teach);
    }

  
    @RequestMapping(method = RequestMethod.GET, value = "/cmsteaches")
    public List<Teach> getAllTeaches() {
        logger.debug("REST request to get all the Teach records");
        return teachRepository.findAll();
    }

    
    @RequestMapping(method = RequestMethod.GET, value = "/cmsteaches/{id}")
    public ResponseEntity<Teach> getTeach(@PathVariable Long id) {
        logger.debug("REST request to get a Teach record : {}", id);
        Optional<Teach> teach = teachRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(teach);
    }

    
    @RequestMapping(method = RequestMethod.DELETE, value = "/cmsteaches/{id}")
    public ResponseEntity<Void> deleteTeach(@PathVariable Long id) {
        logger.debug("REST request to delete a Teach : {}", id);
        teachRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    
    @RequestMapping(method = RequestMethod.GET, value = "/cmsteaches-bysubjectteacher")
    public  ResponseEntity<Teach> getTeachBySubjectAndTeacherId(@RequestParam Map<String, String> dataMap) {
    	if (!dataMap.containsKey("subjectId")) {
            throw new BadRequestAlertException("Subject id not present", ENTITY_NAME, "subject id null");
        }
    	if (!dataMap.containsKey("teacherId")) {
            throw new BadRequestAlertException("Teacher id not present", ENTITY_NAME, "teacher id null");
        }
    	String subjectId = dataMap.get("subjectId");
    	String teacherId = dataMap.get("teacherId");
    	logger.debug("Getting teach id for subject id : "+subjectId+" and teacher id : "+teacherId);
    	Teach th = this.commonService.getTeachBySubjectAndTeacherId(Long.parseLong(teacherId), Long.parseLong(subjectId));
    	logger.debug("Teach : "+th);
    	return ResponseUtil.wrapOrNotFound(Optional.of(th));
    }
}
