package com.synectiks.pref.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.pref.domain.College;
import com.synectiks.pref.repository.CollegeRepository;
import com.synectiks.pref.repository.search.CollegeSearchRepository;
import com.synectiks.pref.service.CollegeService;
import com.synectiks.pref.service.dto.CollegeDTO;
import com.synectiks.pref.service.mapper.CollegeMapper;

/**
 * Service Implementation for managing {@link College}.
 */
@Service
@Transactional
public class CollegeServiceImpl implements CollegeService {

    private final Logger log = LoggerFactory.getLogger(CollegeServiceImpl.class);

    private final CollegeRepository collegeRepository;

    private final CollegeMapper collegeMapper;

//    private final CollegeSearchRepository collegeSearchRepository;

    public CollegeServiceImpl(CollegeRepository collegeRepository, CollegeMapper collegeMapper, CollegeSearchRepository collegeSearchRepository) {
        this.collegeRepository = collegeRepository;
        this.collegeMapper = collegeMapper;
//        this.collegeSearchRepository = collegeSearchRepository;
    }

    /**
     * Save a college.
     *
     * @param collegeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public CollegeDTO save(CollegeDTO collegeDTO) {
        log.debug("Request to save College : {}", collegeDTO);
        College college = collegeMapper.toEntity(collegeDTO);
        college = collegeRepository.save(college);
        CollegeDTO result = collegeMapper.toDto(college);
//        collegeSearchRepository.save(college);
        return result;
    }

    /**
     * Get all the colleges.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollegeDTO> findAll() {
        log.debug("Request to get all Colleges");
        return collegeRepository.findAll().stream()
            .map(collegeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one college by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CollegeDTO> findOne(Long id) {
        log.debug("Request to get College : {}", id);
        return collegeRepository.findById(id)
            .map(collegeMapper::toDto);
    }

    /**
     * Delete the college by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete College : {}", id);
        collegeRepository.deleteById(id);
//        collegeSearchRepository.deleteById(id);
    }

    /**
     * Search for the college corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<CollegeDTO> search(String query) {
        log.debug("Request to search Colleges for query {}", query);
        return null;
    }
}
