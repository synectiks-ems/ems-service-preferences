package com.synectiks.pref.ems.rest;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.config.Constants;
import com.synectiks.pref.domain.AcademicYear;
import com.synectiks.pref.domain.vo.CmsAcademicYearVo;
import com.synectiks.pref.repository.AcademicYearRepository;
import com.synectiks.pref.service.util.CommonUtil;
import com.synectiks.pref.service.util.DateFormatUtil;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;
import com.synectiks.pref.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing AcademicYear.
 */
@RestController
@RequestMapping("/api")
public class AcademicYearRestController {

    private final Logger logger = LoggerFactory.getLogger(AcademicYearRestController.class);

    private static final String ENTITY_NAME = "academicYear";
    
    
    private String applicationName;
    
    @Autowired
    private AcademicYearRepository academicYearRepository;

    /**
     * POST  /academic-years : Create a new academicYear.
     *
     * @param cmsAcademicYearVo the cmsAcademicYearVo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cmsAcademicYearVo, or with status 400 (Bad Request) if the academicYear has already an ID
     * @throws Exception	
     */
    @RequestMapping(method = RequestMethod.POST, value = "/cmsacademic-years")
    public ResponseEntity<CmsAcademicYearVo> createAcademicYear(@Valid @RequestBody CmsAcademicYearVo cmsAcademicYearVo) throws Exception {
        logger.debug("REST request to save an AcademicYear : {}", cmsAcademicYearVo);
        if (cmsAcademicYearVo.getId() != null) {
            throw new BadRequestAlertException("A new academicYear cannot have an ID which already exists.", ENTITY_NAME, "idexists");
        }
        AcademicYear ay = CommonUtil.createCopyProperties(cmsAcademicYearVo, AcademicYear.class);
//        String stDt[] = cmsAcademicYearVo.getStrStartDate().split("/");
//        String ndDt[] = cmsAcademicYearVo.getStrEndDate().split("/");
//        if(stDt[0].length() == 1) {
//        	stDt[0] = "0"+stDt[0];
//        }
//        if(stDt[1].length() == 1) {
//        	stDt[1] = "0"+stDt[1];
//        }
//        if(ndDt[0].length() == 1) {
//        	ndDt[0] = "0"+ndDt[0];
//        }
//        if(ndDt[1].length() == 1) {
//        	ndDt[1] = "0"+ndDt[1];
//        }
        ay.setStartDate(DateFormatUtil.getLocalDateFromString(cmsAcademicYearVo.getStrStartDate()));
        ay.setEndDate(DateFormatUtil.getLocalDateFromString(cmsAcademicYearVo.getStrEndDate()));
        ay = academicYearRepository.save(ay);

        cmsAcademicYearVo.setId(ay.getId());
        cmsAcademicYearVo.setStrStartDate(DateFormatUtil.changeLocalDateFormat(ay.getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        cmsAcademicYearVo.setStrEndDate(DateFormatUtil.changeLocalDateFormat(ay.getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        
        return ResponseEntity.created(new URI("/api/academic-years/" + cmsAcademicYearVo.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cmsAcademicYearVo.getId().toString()))
            .body(cmsAcademicYearVo);
    }

    /**
     * PUT  /academic-years : Updates an existing academicYear.
     *
     * @param cmsAcademicYearVo the cmsAcademicYearVo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cmsAcademicYearVo,
     * or with status 400 (Bad Request) if the cmsAcademicYearVo is not valid,
     * or with status 500 (Internal Server Error) if the cmsAcademicYearVo couldn't be updated
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/cmsacademic-years")
    public ResponseEntity<CmsAcademicYearVo> updateAcademicYear(@Valid @RequestBody CmsAcademicYearVo cmsAcademicYearVo) throws Exception {
        logger.debug("REST request to update an AcademicYear : {}", cmsAcademicYearVo);
        if (cmsAcademicYearVo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
//        AcademicYear ay = CommonUtil.createCopyProperties(cmsAcademicYearVo, AcademicYear.class);
        AcademicYear ay = this.academicYearRepository.findById(cmsAcademicYearVo.getId()).get();
          
//        String stDt[] = cmsAcademicYearVo.getStrStartDate().split("/");
//        String ndDt[] = cmsAcademicYearVo.getStrEndDate().split("/");
//        if(stDt[0].length() == 1) {
//        	stDt[0] = "0"+stDt[0];
//        }
//        if(stDt[1].length() == 1) {
//        	stDt[1] = "0"+stDt[1];
//        }
//        if(ndDt[0].length() == 1) {
//        	ndDt[0] = "0"+ndDt[0];
//        }
//        if(ndDt[1].length() == 1) {
//        	ndDt[1] = "0"+ndDt[1];
//        }
        ay.setDescription(cmsAcademicYearVo.getDescription());
        ay.setStatus(cmsAcademicYearVo.getStatus());
        ay.setStartDate(DateFormatUtil.getLocalDateFromString(cmsAcademicYearVo.getStrStartDate()));
        ay.setEndDate(DateFormatUtil.getLocalDateFromString(cmsAcademicYearVo.getStrEndDate()));
        
//        ay.setStartDate(DateFormatUtil.convertStringToLocalDate(stDt[0]+"/"+stDt[1]+"/"+stDt[2], "MM/dd/yyyy"));
//        ay.setEndDate(DateFormatUtil.convertStringToLocalDate(ndDt[0]+"/"+ndDt[1]+"/"+ndDt[2], "MM/dd/yyyy"));
        
        ay = academicYearRepository.save(ay);
        CmsAcademicYearVo vo = CommonUtil.createCopyProperties(ay, CmsAcademicYearVo.class);
        vo.setStrStartDate(DateFormatUtil.changeLocalDateFormat(ay.getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        vo.setStrEndDate(DateFormatUtil.changeLocalDateFormat(ay.getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
        
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vo.getId().toString()))
            .body(vo);
    }

    /**
     * GET  /cmsacademic-years : get all the academicYears.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of academicYears in body
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/cmsacademic-years")
    public List<CmsAcademicYearVo> getAllAcademicYears() throws Exception {
        logger.debug("REST request to get all the AcademicYears");
        List<AcademicYear> list = academicYearRepository.findAll();
        List<CmsAcademicYearVo> ls = new ArrayList<>();
        for(AcademicYear ay: list) {
            CmsAcademicYearVo cay = CommonUtil.createCopyProperties(ay, CmsAcademicYearVo.class);
            cay.setStrStartDate(DateFormatUtil.changeLocalDateFormat(ay.getStartDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
            cay.setStrEndDate(DateFormatUtil.changeLocalDateFormat(ay.getEndDate(), Constants.DATE_FORMAT_dd_MM_yyyy));
            ls.add(cay);
        }
        Collections.sort(ls, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        return ls;
    }

    /**
     * GET  /academic-years/:id : get the "id" academicYear.
     *
     * @param id the id of the cmsAcademicYearVo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cmsAcademicYearVo, or with status 404 (Not Found)
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/cmsacademic-years/{id}")
    public ResponseEntity<CmsAcademicYearVo> getAcademicYear(@PathVariable Long id) throws Exception {
        logger.debug("REST request to get an AcademicYear : {}", id);
        Optional<AcademicYear> ay = academicYearRepository.findById(id);
        CmsAcademicYearVo cay = new CmsAcademicYearVo();
        if(ay.isPresent()) {
            cay = CommonUtil.createCopyProperties(ay.get(), CmsAcademicYearVo.class);
            cay.setStrStartDate(DateFormatUtil.changeDateFormat(Constants.DATE_FORMAT_dd_MM_yyyy, Constants.DATE_FORMAT_yyyy_MM_dd, DateFormatUtil.changeDateFormat(Constants.DATE_FORMAT_yyyy_MM_dd, DateFormatUtil.converUtilDateFromLocaDate(ay.get().getStartDate()))));
            cay.setStrEndDate(DateFormatUtil.changeDateFormat(Constants.DATE_FORMAT_dd_MM_yyyy, Constants.DATE_FORMAT_yyyy_MM_dd, DateFormatUtil.changeDateFormat(Constants.DATE_FORMAT_yyyy_MM_dd, DateFormatUtil.converUtilDateFromLocaDate(ay.get().getEndDate()))));
        }
        return ResponseUtil.wrapOrNotFound(Optional.of(cay));
    }

    /**
     * DELETE  /academic-years/:id : delete the "id" academicYear.
     *
     * @param id the id of the cmsAcademicYearVo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/cmsacademic-years/{id}")
    public ResponseEntity<Void> deleteAcademicYear(@PathVariable Long id) {
        logger.debug("REST request to delete an AcademicYear : {}", id);
        academicYearRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

}
