package com.synectiks.pref.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synectiks.pref.service.MetaLectureService;
import com.synectiks.pref.service.dto.MetaLectureDTO;
import com.synectiks.pref.web.rest.errors.BadRequestAlertException;
import com.synectiks.pref.web.rest.util.HeaderUtil;

import io.github.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.synectiks.pref.domain.MetaLecture}.
 */
@RestController
@RequestMapping("/api")
public class MetaLectureResource {

    private final Logger log = LoggerFactory.getLogger(MetaLectureResource.class);

    private static final String ENTITY_NAME = "metaLecture";


    private String applicationName;

    private final MetaLectureService metaLectureService;

    public MetaLectureResource(MetaLectureService metaLectureService) {
        this.metaLectureService = metaLectureService;
    }

    /**
     * {@code POST  /meta-lectures} : Create a new metaLecture.
     *
     * @param metaLectureDTO the metaLectureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaLectureDTO, or with status {@code 400 (Bad Request)} if the metaLecture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/meta-lectures")
    public ResponseEntity<MetaLectureDTO> createMetaLecture(@RequestBody MetaLectureDTO metaLectureDTO) throws URISyntaxException {
        log.debug("REST request to save MetaLecture : {}", metaLectureDTO);
        if (metaLectureDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaLecture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MetaLectureDTO result = metaLectureService.save(metaLectureDTO);
        return ResponseEntity.created(new URI("/api/meta-lectures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /meta-lectures} : Updates an existing metaLecture.
     *
     * @param metaLectureDTO the metaLectureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaLectureDTO,
     * or with status {@code 400 (Bad Request)} if the metaLectureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaLectureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/meta-lectures")
    public ResponseEntity<MetaLectureDTO> updateMetaLecture(@RequestBody MetaLectureDTO metaLectureDTO) throws URISyntaxException {
        log.debug("REST request to update MetaLecture : {}", metaLectureDTO);
        if (metaLectureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MetaLectureDTO result = metaLectureService.save(metaLectureDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaLectureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /meta-lectures} : get all the metaLectures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaLectures in body.
     */
    @GetMapping("/meta-lectures")
    public List<MetaLectureDTO> getAllMetaLectures() {
        log.debug("REST request to get all MetaLectures");
        return metaLectureService.findAll();
    }

    /**
     * {@code GET  /meta-lectures/:id} : get the "id" metaLecture.
     *
     * @param id the id of the metaLectureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaLectureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/meta-lectures/{id}")
    public ResponseEntity<MetaLectureDTO> getMetaLecture(@PathVariable Long id) {
        log.debug("REST request to get MetaLecture : {}", id);
        Optional<MetaLectureDTO> metaLectureDTO = metaLectureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaLectureDTO);
    }

    /**
     * {@code DELETE  /meta-lectures/:id} : delete the "id" metaLecture.
     *
     * @param id the id of the metaLectureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/meta-lectures/{id}")
    public ResponseEntity<Void> deleteMetaLecture(@PathVariable Long id) {
        log.debug("REST request to delete MetaLecture : {}", id);
        metaLectureService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/meta-lectures?query=:query} : search for the metaLecture corresponding
     * to the query.
     *
     * @param query the query of the metaLecture search.
     * @return the result of the search.
     */
    @GetMapping("/_search/meta-lectures")
    public List<MetaLectureDTO> searchMetaLectures(@RequestParam String query) {
        log.debug("REST request to search MetaLectures for query {}", query);
        return metaLectureService.search(query);
    }

}
