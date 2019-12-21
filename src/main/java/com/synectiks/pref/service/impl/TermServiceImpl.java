package com.synectiks.pref.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.pref.domain.Term;
import com.synectiks.pref.repository.TermRepository;
import com.synectiks.pref.repository.search.TermSearchRepository;
import com.synectiks.pref.service.TermService;
import com.synectiks.pref.service.dto.TermDTO;
import com.synectiks.pref.service.mapper.TermMapper;

/**
 * Service Implementation for managing {@link Term}.
 */
@Service
@Transactional
public class TermServiceImpl implements TermService {

    private final Logger log = LoggerFactory.getLogger(TermServiceImpl.class);

    private final TermRepository termRepository;

    private final TermMapper termMapper;

//    private final TermSearchRepository termSearchRepository;

    public TermServiceImpl(TermRepository termRepository, TermMapper termMapper, TermSearchRepository termSearchRepository) {
        this.termRepository = termRepository;
        this.termMapper = termMapper;
//        this.termSearchRepository = termSearchRepository;
    }

    /**
     * Save a term.
     *
     * @param termDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TermDTO save(TermDTO termDTO) {
        log.debug("Request to save Term : {}", termDTO);
        Term term = termMapper.toEntity(termDTO);
        term = termRepository.save(term);
        TermDTO result = termMapper.toDto(term);
//        termSearchRepository.save(term);
        return result;
    }

    /**
     * Get all the terms.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TermDTO> findAll() {
        log.debug("Request to get all Terms");
        return termRepository.findAll().stream()
            .map(termMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one term by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TermDTO> findOne(Long id) {
        log.debug("Request to get Term : {}", id);
        return termRepository.findById(id)
            .map(termMapper::toDto);
    }

    /**
     * Delete the term by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Term : {}", id);
        termRepository.deleteById(id);
//        termSearchRepository.deleteById(id);
    }

    /**
     * Search for the term corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TermDTO> search(String query) {
        log.debug("Request to search Terms for query {}", query);
        return null;
    }
}
