package com.synectiks.pref.ems.rest;


import java.net.URI;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.config.Constants;
import com.synectiks.pref.domain.AcademicYear;
import com.synectiks.pref.domain.Term;
import com.synectiks.pref.domain.enumeration.Status;
import com.synectiks.pref.domain.vo.CmsTermVo;
import com.synectiks.pref.repository.AcademicYearRepository;
import com.synectiks.pref.repository.TermRepository;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.service.util.DateFormatUtil;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;
import com.synectiks.pref.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api")
public class TermRestController {

    private final Logger logger = LoggerFactory.getLogger(TermRestController.class);

    private static final String ENTITY_NAME = "term";

    
    private String applicationName;
    
    @Autowired
    private TermRepository termRepository;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @RequestMapping(method = RequestMethod.POST, value = "/cmsterms")
    public ResponseEntity<CmsTermVo> createTerm(@Valid @RequestBody CmsTermVo cmsTermVo) throws Exception {
        logger.debug("REST request to save an Term : {}", cmsTermVo);
        if (cmsTermVo.getId() != null) {
            throw new BadRequestAlertException("A new term cannot have an ID which already exists.", ENTITY_NAME, "idexists");
        }
        if(cmsTermVo.getTermStatus() == null) {
            cmsTermVo.setTermStatus(Status.DEACTIVE);
        }
        Term tm = CommonUtil.createCopyProperties(cmsTermVo, Term.class);
        tm.setStartDate(DateFormatUtil.getLocalDateFromString(cmsTermVo.getStrStartDate()));
        tm.setEndDate(DateFormatUtil.getLocalDateFromString(cmsTermVo.getStrEndDate()));
        
        tm = termRepository.save(tm);

        cmsTermVo.setId(tm.getId());
        cmsTermVo.setStrStartDate(DateFormatUtil.changeLocalDateFormat(tm.getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        cmsTermVo.setStrEndDate(DateFormatUtil.changeLocalDateFormat(tm.getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        return ResponseEntity.created(new URI("/api/terms/" + cmsTermVo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cmsTermVo.getId().toString()))
            .body(cmsTermVo);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/cmsterms")
    public ResponseEntity<CmsTermVo> updateTerm(@Valid @RequestBody CmsTermVo cmsTermVo) throws Exception {
        logger.debug("REST request to update an Term : {}", cmsTermVo);
        if (cmsTermVo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if(cmsTermVo.getTermStatus() == null) {
            cmsTermVo.setTermStatus(Status.DEACTIVE);
        }
        Term tm = CommonUtil.createCopyProperties(cmsTermVo, Term.class);
        tm.setStartDate(DateFormatUtil.getLocalDateFromString(cmsTermVo.getStrStartDate()));
        tm.setEndDate(DateFormatUtil.getLocalDateFromString(cmsTermVo.getStrEndDate()));
        
        tm = termRepository.save(tm);

        cmsTermVo.setStrStartDate(DateFormatUtil.changeLocalDateFormat(tm.getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        cmsTermVo.setStrEndDate(DateFormatUtil.changeLocalDateFormat(tm.getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cmsTermVo.getId().toString()))
            .body(cmsTermVo);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/cmsterms")
    public List<CmsTermVo> getAllTerms() throws Exception {
        logger.debug("REST request to get all the Terms");
        List<Term> list = termRepository.findAll();
        List<CmsTermVo> ls = new ArrayList<>();
        for(Term tm: list) {
            CmsTermVo ctm = CommonUtil.createCopyProperties(tm, CmsTermVo.class);
            ctm.setStrStartDate(DateFormatUtil.changeLocalDateFormat(tm.getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
            ctm.setStrEndDate(DateFormatUtil.changeLocalDateFormat(tm.getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
            ls.add(ctm);
        }
        return ls;
    }


    @RequestMapping(method = RequestMethod.GET, value = "/cmsterms/{id}")
    public ResponseEntity<CmsTermVo> getTerm(@PathVariable Long id) throws Exception {
        logger.debug("REST request to get an Term : {}", id);
        Optional<Term> tm = termRepository.findById(id);
        CmsTermVo ctm = new CmsTermVo();
        if(tm.isPresent()) {
            ctm = CommonUtil.createCopyProperties(tm.get(), CmsTermVo.class);
            ctm.setStrStartDate(DateFormatUtil.changeLocalDateFormat(tm.get().getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
            ctm.setStrEndDate(DateFormatUtil.changeLocalDateFormat(tm.get().getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
            
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(ctm));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cmsterms-by_academicyearid")
    public List<CmsTermVo> getAllTermsByAcademicYearId(@RequestParam Map<String, String> dataMap) throws Exception{
        if(!dataMap.containsKey("academicYearId")) {
            logger.warn("Academic year id is not provided. Returning empty terms list");
            return Collections.emptyList();
        }
        List<CmsTermVo> ls = new ArrayList<>();
        Long id = Long.valueOf(dataMap.get("academicYearId"));
        Optional<AcademicYear> oay = this.academicYearRepository.findById(id);

        if(oay.isPresent()) {
//    		AcademicYear ay = this.academicYearRepository.findById(id).get();
            logger.debug("Terms based on academic year. AcademicYear :"+oay.get());
            Term term = new Term();
            term.setAcademicyear(oay.get());
            Example<Term> exm = Example.of(term);
            List<Term> list = this.termRepository.findAll(exm);
            for(Term tm: list) {
                CmsTermVo ctm = CommonUtil.createCopyProperties(tm, CmsTermVo.class);
                ctm.setStrStartDate(DateFormatUtil.changeLocalDateFormat(tm.getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
                ctm.setStrEndDate(DateFormatUtil.changeLocalDateFormat(tm.getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
                ls.add(ctm);
            }
        }
        logger.debug("Total terms retrieved: "+ls.size());
        return ls;

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/cmsterms/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable Long id) {
        logger.debug("REST request to delete an Term : {}", id);
        termRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

}

