package com.synectiks.pref.web.rest;

import static com.synectiks.pref.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

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

import com.synectiks.pref.PreferencesApp;
import com.synectiks.pref.domain.AcademicYear;
import com.synectiks.pref.domain.enumeration.Status;
import com.synectiks.pref.repository.AcademicYearRepository;
import com.synectiks.pref.repository.search.AcademicYearSearchRepository;
import com.synectiks.pref.service.AcademicYearService;
import com.synectiks.pref.service.dto.AcademicYearDTO;
import com.synectiks.pref.service.mapper.AcademicYearMapper;
import com.synectiks.pref.web.rest.errors.ExceptionTranslator;
/**
 * Integration tests for the {@Link AcademicYearResource} REST controller.
 */
@SpringBootTest(classes = PreferencesApp.class)
public class AcademicYearResourceIT {

    private static final String DEFAULT_YEAR = "AAAAAAAAAA";
    private static final String UPDATED_YEAR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.DEACTIVE;

    @Autowired
    private AcademicYearRepository academicYearRepository;

    @Autowired
    private AcademicYearMapper academicYearMapper;

    @Autowired
    private AcademicYearService academicYearService;

    /**
     * This repository is mocked in the com.synectiks.pref.repository.search test package.
     *
     * @see com.synectiks.pref.repository.search.AcademicYearSearchRepositoryMockConfiguration
     */
    @Autowired
    private AcademicYearSearchRepository mockAcademicYearSearchRepository;

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

    private MockMvc restAcademicYearMockMvc;

    private AcademicYear academicYear;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AcademicYearResource academicYearResource = new AcademicYearResource(academicYearService);
        this.restAcademicYearMockMvc = MockMvcBuilders.standaloneSetup(academicYearResource)
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
    public static AcademicYear createEntity(EntityManager em) {
        AcademicYear academicYear = new AcademicYear();
        academicYear.description(DEFAULT_YEAR);
        academicYear.startDate(DEFAULT_START_DATE);
        academicYear.endDate(DEFAULT_END_DATE);
        return academicYear;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AcademicYear createUpdatedEntity(EntityManager em) {
        AcademicYear academicYear = new AcademicYear();
        academicYear.description(UPDATED_YEAR);
        academicYear.startDate(UPDATED_START_DATE);
        academicYear.endDate(UPDATED_END_DATE);
        return academicYear;
    }

    @BeforeEach
    public void initTest() {
        academicYear = createEntity(em);
    }

    @Test
    @Transactional
    public void createAcademicYear() throws Exception {
        int databaseSizeBeforeCreate = academicYearRepository.findAll().size();

        // Create the AcademicYear
        AcademicYearDTO academicYearDTO = academicYearMapper.toDto(academicYear);
        restAcademicYearMockMvc.perform(post("/api/academic-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(academicYearDTO)))
            .andExpect(status().isCreated());

        // Validate the AcademicYear in the database
        List<AcademicYear> academicYearList = academicYearRepository.findAll();
        assertThat(academicYearList).hasSize(databaseSizeBeforeCreate + 1);
        AcademicYear testAcademicYear = academicYearList.get(academicYearList.size() - 1);
        assertThat(testAcademicYear.getDescription()).isEqualTo(DEFAULT_YEAR);
        assertThat(testAcademicYear.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testAcademicYear.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testAcademicYear.getStatus()).isEqualTo(DEFAULT_STATUS);

        // Validate the AcademicYear in Elasticsearch
        verify(mockAcademicYearSearchRepository, times(1)).save(testAcademicYear);
    }

    @Test
    @Transactional
    public void createAcademicYearWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = academicYearRepository.findAll().size();

        // Create the AcademicYear with an existing ID
        academicYear.setId(1L);
        AcademicYearDTO academicYearDTO = academicYearMapper.toDto(academicYear);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAcademicYearMockMvc.perform(post("/api/academic-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(academicYearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AcademicYear in the database
        List<AcademicYear> academicYearList = academicYearRepository.findAll();
        assertThat(academicYearList).hasSize(databaseSizeBeforeCreate);

        // Validate the AcademicYear in Elasticsearch
        verify(mockAcademicYearSearchRepository, times(0)).save(academicYear);
    }


    @Test
    @Transactional
    public void getAllAcademicYears() throws Exception {
        // Initialize the database
        academicYearRepository.saveAndFlush(academicYear);

        // Get all the academicYearList
        restAcademicYearMockMvc.perform(get("/api/academic-years?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(academicYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_YEAR.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getAcademicYear() throws Exception {
        // Initialize the database
        academicYearRepository.saveAndFlush(academicYear);

        // Get the academicYear
        restAcademicYearMockMvc.perform(get("/api/academic-years/{id}", academicYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(academicYear.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_YEAR.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAcademicYear() throws Exception {
        // Get the academicYear
        restAcademicYearMockMvc.perform(get("/api/academic-years/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAcademicYear() throws Exception {
        // Initialize the database
        academicYearRepository.saveAndFlush(academicYear);

        int databaseSizeBeforeUpdate = academicYearRepository.findAll().size();

        // Update the academicYear
        AcademicYear updatedAcademicYear = academicYearRepository.findById(academicYear.getId()).get();
        // Disconnect from session so that the updates on updatedAcademicYear are not directly saved in db
        em.detach(updatedAcademicYear);
//        updatedAcademicYear
//            .description(UPDATED_YEAR)
//            .startDate(UPDATED_START_DATE)
//            .endDate(UPDATED_END_DATE)
//        AcademicYearDTO academicYearDTO = academicYearMapper.toDto(updatedAcademicYear);

//        restAcademicYearMockMvc.perform(put("/api/academic-years")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(academicYearDTO)))
//            .andExpect(status().isOk());

        // Validate the AcademicYear in the database
        List<AcademicYear> academicYearList = academicYearRepository.findAll();
        assertThat(academicYearList).hasSize(databaseSizeBeforeUpdate);
        AcademicYear testAcademicYear = academicYearList.get(academicYearList.size() - 1);
        assertThat(testAcademicYear.getDescription()).isEqualTo(UPDATED_YEAR);
        assertThat(testAcademicYear.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testAcademicYear.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testAcademicYear.getStatus()).isEqualTo(UPDATED_STATUS);

        // Validate the AcademicYear in Elasticsearch
        verify(mockAcademicYearSearchRepository, times(1)).save(testAcademicYear);
    }

    @Test
    @Transactional
    public void updateNonExistingAcademicYear() throws Exception {
        int databaseSizeBeforeUpdate = academicYearRepository.findAll().size();

        // Create the AcademicYear
        AcademicYearDTO academicYearDTO = academicYearMapper.toDto(academicYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAcademicYearMockMvc.perform(put("/api/academic-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(academicYearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AcademicYear in the database
        List<AcademicYear> academicYearList = academicYearRepository.findAll();
        assertThat(academicYearList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AcademicYear in Elasticsearch
        verify(mockAcademicYearSearchRepository, times(0)).save(academicYear);
    }

    @Test
    @Transactional
    public void deleteAcademicYear() throws Exception {
        // Initialize the database
        academicYearRepository.saveAndFlush(academicYear);

        int databaseSizeBeforeDelete = academicYearRepository.findAll().size();

        // Delete the academicYear
        restAcademicYearMockMvc.perform(delete("/api/academic-years/{id}", academicYear.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AcademicYear> academicYearList = academicYearRepository.findAll();
        assertThat(academicYearList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AcademicYear in Elasticsearch
        verify(mockAcademicYearSearchRepository, times(1)).deleteById(academicYear.getId());
    }

//    @Test
//    @Transactional
//    public void searchAcademicYear() throws Exception {
//        // Initialize the database
//        academicYearRepository.saveAndFlush(academicYear);
//        when(mockAcademicYearSearchRepository.search(queryStringQuery("id:" + academicYear.getId())))
//            .thenReturn(Collections.singletonList(academicYear));
//        // Search the academicYear
//        restAcademicYearMockMvc.perform(get("/api/_search/academic-years?query=id:" + academicYear.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(academicYear.getId().intValue())))
//            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_YEAR)))
//            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
//            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
//            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
//    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicYear.class);
        AcademicYear academicYear1 = new AcademicYear();
        academicYear1.setId(1L);
        AcademicYear academicYear2 = new AcademicYear();
        academicYear2.setId(academicYear1.getId());
        assertThat(academicYear1).isEqualTo(academicYear2);
        academicYear2.setId(2L);
        assertThat(academicYear1).isNotEqualTo(academicYear2);
        academicYear1.setId(null);
        assertThat(academicYear1).isNotEqualTo(academicYear2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AcademicYearDTO.class);
        AcademicYearDTO academicYearDTO1 = new AcademicYearDTO();
        academicYearDTO1.setId(1L);
        AcademicYearDTO academicYearDTO2 = new AcademicYearDTO();
        assertThat(academicYearDTO1).isNotEqualTo(academicYearDTO2);
        academicYearDTO2.setId(academicYearDTO1.getId());
        assertThat(academicYearDTO1).isEqualTo(academicYearDTO2);
        academicYearDTO2.setId(2L);
        assertThat(academicYearDTO1).isNotEqualTo(academicYearDTO2);
        academicYearDTO1.setId(null);
        assertThat(academicYearDTO1).isNotEqualTo(academicYearDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(academicYearMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(academicYearMapper.fromId(null)).isNull();
    }
}
