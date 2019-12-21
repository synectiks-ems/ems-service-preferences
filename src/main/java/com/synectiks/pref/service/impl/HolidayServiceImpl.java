package com.synectiks.pref.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.pref.domain.Holiday;
import com.synectiks.pref.repository.HolidayRepository;
import com.synectiks.pref.repository.search.HolidaySearchRepository;
import com.synectiks.pref.service.HolidayService;
import com.synectiks.pref.service.dto.HolidayDTO;
import com.synectiks.pref.service.mapper.HolidayMapper;

/**
 * Service Implementation for managing {@link Holiday}.
 */
@Service
@Transactional
public class HolidayServiceImpl implements HolidayService {

    private final Logger log = LoggerFactory.getLogger(HolidayServiceImpl.class);

    private final HolidayRepository holidayRepository;

    private final HolidayMapper holidayMapper;

//    private final HolidaySearchRepository holidaySearchRepository;

    public HolidayServiceImpl(HolidayRepository holidayRepository, HolidayMapper holidayMapper, HolidaySearchRepository holidaySearchRepository) {
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
//        this.holidaySearchRepository = holidaySearchRepository;
    }

    /**
     * Save a holiday.
     *
     * @param holidayDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public HolidayDTO save(HolidayDTO holidayDTO) {
        log.debug("Request to save Holiday : {}", holidayDTO);
        Holiday holiday = holidayMapper.toEntity(holidayDTO);
        holiday = holidayRepository.save(holiday);
        HolidayDTO result = holidayMapper.toDto(holiday);
//        holidaySearchRepository.save(holiday);
        return result;
    }

    /**
     * Get all the holidays.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HolidayDTO> findAll() {
        log.debug("Request to get all Holidays");
        return holidayRepository.findAll().stream()
            .map(holidayMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one holiday by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HolidayDTO> findOne(Long id) {
        log.debug("Request to get Holiday : {}", id);
        return holidayRepository.findById(id)
            .map(holidayMapper::toDto);
    }

    /**
     * Delete the holiday by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Holiday : {}", id);
        holidayRepository.deleteById(id);
//        holidaySearchRepository.deleteById(id);
    }

    /**
     * Search for the holiday corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<HolidayDTO> search(String query) {
        log.debug("Request to search Holidays for query {}", query);
        return null;
    }
}
