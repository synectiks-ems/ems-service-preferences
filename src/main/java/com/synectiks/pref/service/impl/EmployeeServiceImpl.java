package com.synectiks.pref.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.synectiks.pref.domain.Employee;
import com.synectiks.pref.repository.EmployeeRepository;
import com.synectiks.pref.repository.search.EmployeeSearchRepository;
import com.synectiks.pref.service.EmployeeService;
import com.synectiks.pref.service.dto.EmployeeDTO;
import com.synectiks.pref.service.mapper.EmployeeMapper;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper;

    private final EmployeeSearchRepository employeeSearchRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper, EmployeeSearchRepository employeeSearchRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.employeeSearchRepository = employeeSearchRepository;
    }

    /**
     * Save a employee.
     *
     * @param employeeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee = employeeRepository.save(employee);
        EmployeeDTO result = employeeMapper.toDto(employee);
        employeeSearchRepository.save(employee);
        return result;
    }

    /**
     * Get all the employees.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findAll() {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll().stream()
            .map(employeeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id)
            .map(employeeMapper::toDto);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
        employeeSearchRepository.deleteById(id);
    }

    /**
     * Search for the employee corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> search(String query) {
        log.debug("Request to search Employees for query {}", query);
        return null;
    }
}
