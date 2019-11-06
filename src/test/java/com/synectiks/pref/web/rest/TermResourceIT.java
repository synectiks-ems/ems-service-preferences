package com.synectiks.pref.web.rest;

import com.synectiks.pref.PreferencesApp;
import com.synectiks.pref.domain.Term;
import com.synectiks.pref.repository.TermRepository;
import com.synectiks.pref.repository.search.TermSearchRepository;
import com.synectiks.pref.service.TermService;
import com.synectiks.pref.service.dto.TermDTO;
import com.synectiks.pref.service.mapper.TermMapper;
import com.synectiks.pref.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.synectiks.pref.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.synectiks.pref.domain.enumeration.Status;
/**
 * Integration tests for the {@Link TermResource} REST controller.
 */
@SpringBootTest(classes = PreferencesApp.class)
public class TermResourceIT {

    private static final String DEFAULT_TERMS_DESC = "AAAAAAAAAA";
    private static final String UPDATED_TERMS_DESC = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_TERM_STATUS = Status.ACTIVE;
    private static final Status UPDATED_TERM_STATUS = Status.DEACTIVE;

    @Autowired
    private TermRepository termRepository;

    @Autowired
    private TermMapper termMapper;

    @Autowired
    private TermService termService;

    /**
     * This repository is mocked in the com.synectiks.pref.repository.search test package.
     *
     * @see com.synectiks.pref.repository.search.TermSearchRepositoryMockConfiguration
     */
    @Autowired
    private TermSearchRepository mockTermSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restTermMockMvc;

    private Term term;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TermResource termResource = new TermResource(termService);
        this.restTermMockMvc = MockMvcBuilders.standaloneSetup(termResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Term createEntity(EntityManager em) {
        Term term = new Term()
            .termsDesc(DEFAULT_TERMS_DESC)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .termStatus(DEFAULT_TERM_STATUS);
        return term;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Term createUpdatedEntity(EntityManager em) {
        Term term = new Term()
            .termsDesc(UPDATED_TERMS_DESC)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .termStatus(UPDATED_TERM_STATUS);
        return term;
    }

    @BeforeEach
    public void initTest() {
        term = createEntity(em);
    }

    @Test
    @Transactional
    public void createTerm() throws Exception {
        int databaseSizeBeforeCreate = termRepository.findAll().size();

        // Create the Term
        TermDTO termDTO = termMapper.toDto(term);
        restTermMockMvc.perform(post("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(termDTO)))
            .andExpect(status().isCreated());

        // Validate the Term in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeCreate + 1);
        Term testTerm = termList.get(termList.size() - 1);
        assertThat(testTerm.getTermsDesc()).isEqualTo(DEFAULT_TERMS_DESC);
        assertThat(testTerm.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTerm.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testTerm.getTermStatus()).isEqualTo(DEFAULT_TERM_STATUS);

        // Validate the Term in Elasticsearch
        verify(mockTermSearchRepository, times(1)).save(testTerm);
    }

    @Test
    @Transactional
    public void createTermWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = termRepository.findAll().size();

        // Create the Term with an existing ID
        term.setId(1L);
        TermDTO termDTO = termMapper.toDto(term);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTermMockMvc.perform(post("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(termDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Term in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeCreate);

        // Validate the Term in Elasticsearch
        verify(mockTermSearchRepository, times(0)).save(term);
    }


    @Test
    @Transactional
    public void getAllTerms() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);

        // Get all the termList
        restTermMockMvc.perform(get("/api/terms?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(term.getId().intValue())))
            .andExpect(jsonPath("$.[*].termsDesc").value(hasItem(DEFAULT_TERMS_DESC.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].termStatus").value(hasItem(DEFAULT_TERM_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);

        // Get the term
        restTermMockMvc.perform(get("/api/terms/{id}", term.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(term.getId().intValue()))
            .andExpect(jsonPath("$.termsDesc").value(DEFAULT_TERMS_DESC.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.termStatus").value(DEFAULT_TERM_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTerm() throws Exception {
        // Get the term
        restTermMockMvc.perform(get("/api/terms/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);

        int databaseSizeBeforeUpdate = termRepository.findAll().size();

        // Update the term
        Term updatedTerm = termRepository.findById(term.getId()).get();
        // Disconnect from session so that the updates on updatedTerm are not directly saved in db
        em.detach(updatedTerm);
        updatedTerm
            .termsDesc(UPDATED_TERMS_DESC)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .termStatus(UPDATED_TERM_STATUS);
        TermDTO termDTO = termMapper.toDto(updatedTerm);

        restTermMockMvc.perform(put("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(termDTO)))
            .andExpect(status().isOk());

        // Validate the Term in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeUpdate);
        Term testTerm = termList.get(termList.size() - 1);
        assertThat(testTerm.getTermsDesc()).isEqualTo(UPDATED_TERMS_DESC);
        assertThat(testTerm.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTerm.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testTerm.getTermStatus()).isEqualTo(UPDATED_TERM_STATUS);

        // Validate the Term in Elasticsearch
        verify(mockTermSearchRepository, times(1)).save(testTerm);
    }

    @Test
    @Transactional
    public void updateNonExistingTerm() throws Exception {
        int databaseSizeBeforeUpdate = termRepository.findAll().size();

        // Create the Term
        TermDTO termDTO = termMapper.toDto(term);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTermMockMvc.perform(put("/api/terms")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(termDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Term in the database
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Term in Elasticsearch
        verify(mockTermSearchRepository, times(0)).save(term);
    }

    @Test
    @Transactional
    public void deleteTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);

        int databaseSizeBeforeDelete = termRepository.findAll().size();

        // Delete the term
        restTermMockMvc.perform(delete("/api/terms/{id}", term.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Term> termList = termRepository.findAll();
        assertThat(termList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Term in Elasticsearch
        verify(mockTermSearchRepository, times(1)).deleteById(term.getId());
    }

    @Test
    @Transactional
    public void searchTerm() throws Exception {
        // Initialize the database
        termRepository.saveAndFlush(term);
        when(mockTermSearchRepository.search(queryStringQuery("id:" + term.getId())))
            .thenReturn(Collections.singletonList(term));
        // Search the term
        restTermMockMvc.perform(get("/api/_search/terms?query=id:" + term.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(term.getId().intValue())))
            .andExpect(jsonPath("$.[*].termsDesc").value(hasItem(DEFAULT_TERMS_DESC)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].termStatus").value(hasItem(DEFAULT_TERM_STATUS.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Term.class);
        Term term1 = new Term();
        term1.setId(1L);
        Term term2 = new Term();
        term2.setId(term1.getId());
        assertThat(term1).isEqualTo(term2);
        term2.setId(2L);
        assertThat(term1).isNotEqualTo(term2);
        term1.setId(null);
        assertThat(term1).isNotEqualTo(term2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TermDTO.class);
        TermDTO termDTO1 = new TermDTO();
        termDTO1.setId(1L);
        TermDTO termDTO2 = new TermDTO();
        assertThat(termDTO1).isNotEqualTo(termDTO2);
        termDTO2.setId(termDTO1.getId());
        assertThat(termDTO1).isEqualTo(termDTO2);
        termDTO2.setId(2L);
        assertThat(termDTO1).isNotEqualTo(termDTO2);
        termDTO1.setId(null);
        assertThat(termDTO1).isNotEqualTo(termDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(termMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(termMapper.fromId(null)).isNull();
    }
}
