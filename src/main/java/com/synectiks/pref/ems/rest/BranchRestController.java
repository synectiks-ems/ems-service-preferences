package com.synectiks.pref.ems.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.domain.City;
import com.synectiks.pref.domain.College;
import com.synectiks.pref.domain.State;
import com.synectiks.pref.domain.vo.CmsBranchVo;
import com.synectiks.pref.repository.BranchRepository;
import com.synectiks.pref.repository.CityRepository;
import com.synectiks.pref.repository.CollegeRepository;
import com.synectiks.pref.repository.StateRepository;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;
import com.synectiks.pref.web.rest.util.HeaderUtil;

/**
 * REST controller for managing Branch.
 */
@RestController
@RequestMapping("/api")
public class BranchRestController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String ENTITY_NAME = "branch";
	
	
    private String applicationName;
	
	@Autowired
	private BranchRepository branchRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private CollegeRepository collegeRepository;
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/cmsbranches")
	public ResponseEntity<CmsBranchVo> createBranch(@Valid @RequestBody CmsBranchVo cmsBranchVo) throws URISyntaxException {
		logger.info("REST request to create a new branch.", cmsBranchVo);
        if (cmsBranchVo.getId() != null) {
            throw new BadRequestAlertException("A new branch cannot have an ID which already exits", ENTITY_NAME, "idexists");
        }
        Branch b = new Branch();
        State s = this.stateRepository.findById(cmsBranchVo.getStateId()).get();
        City c = this.cityRepository.findById(cmsBranchVo.getCityId()).get();
        College clg = this.collegeRepository.findById(cmsBranchVo.getCollegeId()).get();
        b.setAddress1(cmsBranchVo.getAddress1());
        b.setAddress2(cmsBranchVo.getAddress2());
        b.setBranchHead(cmsBranchVo.getBranchHead());
        b.setBranchName(cmsBranchVo.getBranchName());
        b.setCity(c);
        b.setState(s);
        b.setCollege(clg);
        Branch result = branchRepository.save(b);
        cmsBranchVo.setId(result.getId());
        return ResponseEntity.created(new URI("/api/cmsbranches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(cmsBranchVo);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value = "/cmsbranches")
	public ResponseEntity<CmsBranchVo> updateBranch(@Valid @RequestBody CmsBranchVo cmsBranchVo) throws URISyntaxException {
		logger.info("REST request to update existing branch.", cmsBranchVo);
		if (cmsBranchVo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
		Branch b = new Branch();
        State s = this.stateRepository.findById(cmsBranchVo.getStateId()).get();
        City c = this.cityRepository.findById(cmsBranchVo.getCityId()).get();
        College clg = this.collegeRepository.findById(cmsBranchVo.getCollegeId()).get();
        b.setAddress1(cmsBranchVo.getAddress1());
        b.setAddress2(cmsBranchVo.getAddress2());
        b.setBranchHead(cmsBranchVo.getBranchHead());
        b.setBranchName(cmsBranchVo.getBranchName());
        b.setCity(c);
        b.setState(s);
        b.setCollege(clg);
        b.setId(cmsBranchVo.getId());
		Branch result = branchRepository.save(b);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmsBranchVo.getId().toString()))
                .body(cmsBranchVo);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/cmsbranches")
    public List<CmsBranchVo> getAllBranches() {
		logger.debug("REST request to get all the branchs.");
		List<Branch> list = branchRepository.findAll();
		List<CmsBranchVo> ls = new ArrayList<>();
		for(Branch br : list) {
			CmsBranchVo vo = new CmsBranchVo();
			vo.setAddress1(br.getAddress1());
	        vo.setAddress2(br.getAddress2());
	        vo.setBranchHead(br.getBranchHead());
	        vo.setBranchName(br.getBranchName());
	        vo.setCity(br.getCity());
	        vo.setState(br.getState());
	        vo.setCollege(br.getCollege());
	        vo.setId(br.getId());
	        ls.add(vo);
		}
        return ls;
    }

	
	@RequestMapping(method = RequestMethod.GET, value = "/cmsbranches-collegeid/{id}")
	public List<CmsBranchVo> getAllBranchesByCollegeId(@PathVariable Long id){
		if(!this.collegeRepository.existsById(id)) {
			return Collections.emptyList();
		}
		College college = this.collegeRepository.findById(id).get();
		Branch branch = new Branch();
		branch.setCollege(college);
		Example<Branch> example = Example.of(branch);
		List<Branch> list = branchRepository.findAll(example);
		List<CmsBranchVo> ls = new ArrayList<>();
		for(Branch br : list) {
			CmsBranchVo vo = CommonUtil.createCopyProperties(br, CmsBranchVo.class);
			vo.setCollegeId(br.getCollege().getId());
			if(br.getState() != null) {
				vo.setStateId(br.getState().getId());
			}
			if(br.getCity() != null) {
				vo.setCityId(br.getCity().getId());
			}
	        ls.add(vo);
		}
        return ls;
	}
	
	
    @RequestMapping(method = RequestMethod.DELETE, value = "/cmsbranches/{id}")
    public Integer deleteBranch(@PathVariable Long id) {
    	try {
    		logger.debug("REST request to delete a Branch : {}", id);
        	branchRepository.deleteById(id);
//            return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    	}catch(Exception e) {
    		return HttpStatus.FAILED_DEPENDENCY.value();
    	}
    	return HttpStatus.OK.value();
        
    }
}
