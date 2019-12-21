package com.synectiks.pref.web.rest;

import static com.synectiks.pref.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.ZoneId;
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
import com.synectiks.pref.domain.LegalEntity;
import com.synectiks.pref.repository.LegalEntityRepository;
import com.synectiks.pref.repository.search.LegalEntitySearchRepository;
import com.synectiks.pref.service.LegalEntityService;
import com.synectiks.pref.service.dto.LegalEntityDTO;
import com.synectiks.pref.service.mapper.LegalEntityMapper;
import com.synectiks.pref.web.rest.errors.ExceptionTranslator;

/**
 * Integration tests for the {@Link LegalEntityResource} REST controller.
 */
@SpringBootTest(classes = PreferencesApp.class)
public class LegalEntityResourceIT {

    private static final String DEFAULT_LOGO_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_FILE_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGO_FILE_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_LOGO_FILE_EXTENSION = "BBBBBBBBBB";

    private static final String DEFAULT_LEGAL_NAME_OF_COLLEGE = "AAAAAAAAAA";
    private static final String UPDATED_LEGAL_NAME_OF_COLLEGE = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE_OF_COLLEGE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_OF_COLLEGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_OF_INCORPORATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_INCORPORATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REGISTERED_OFFICE_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_REGISTERED_OFFICE_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COLLEGE_IDENTIFICATION_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_COLLEGE_IDENTIFICATION_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_PAN = "AAAAAAAAAA";
    private static final String UPDATED_PAN = "BBBBBBBBBB";

    private static final String DEFAULT_TAN = "AAAAAAAAAA";
    private static final String UPDATED_TAN = "BBBBBBBBBB";

    private static final String DEFAULT_TAN_CIRCLE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TAN_CIRCLE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CIT_TDS_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_CIT_TDS_LOCATION = "BBBBBBBBBB";

    private static final Long DEFAULT_FORM_SIGNATORY = 1L;
    private static final Long UPDATED_FORM_SIGNATORY = 2L;

    private static final String DEFAULT_PF_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PF_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PF_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PF_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PF_SIGNATORY = 1L;
    private static final Long UPDATED_PF_SIGNATORY = 2L;

    private static final String DEFAULT_ESI_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ESI_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ESI_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESI_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ESI_SIGNATORY = 1L;
    private static final Long UPDATED_ESI_SIGNATORY = 2L;

    private static final String DEFAULT_PT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PT_NUMBER = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PT_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_PT_SIGNATORY = 1L;
    private static final Long UPDATED_PT_SIGNATORY = 2L;

    @Autowired
    private LegalEntityRepository legalEntityRepository;

    @Autowired
    private LegalEntityMapper legalEntityMapper;

    @Autowired
    private LegalEntityService legalEntityService;

    /**
     * This repository is mocked in the com.synectiks.pref.repository.search test package.
     *
     * @see com.synectiks.pref.repository.search.LegalEntitySearchRepositoryMockConfiguration
     */
    @Autowired
    private LegalEntitySearchRepository mockLegalEntitySearchRepository;

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

    private MockMvc restLegalEntityMockMvc;

    private LegalEntity legalEntity;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LegalEntityResource legalEntityResource = new LegalEntityResource(legalEntityService);
        this.restLegalEntityMockMvc = MockMvcBuilders.standaloneSetup(legalEntityResource)
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
    public static LegalEntity createEntity(EntityManager em) {
        LegalEntity legalEntity = new LegalEntity()
            .logoFilePath(DEFAULT_LOGO_FILE_PATH)
            .logoFileName(DEFAULT_LOGO_FILE_NAME)
            .logoFileExtension(DEFAULT_LOGO_FILE_EXTENSION)
            .legalNameOfCollege(DEFAULT_LEGAL_NAME_OF_COLLEGE)
            .typeOfCollege(DEFAULT_TYPE_OF_COLLEGE)
            .dateOfIncorporation(DEFAULT_DATE_OF_INCORPORATION)
            .registeredOfficeAddress(DEFAULT_REGISTERED_OFFICE_ADDRESS)
            .collegeIdentificationNumber(DEFAULT_COLLEGE_IDENTIFICATION_NUMBER)
            .pan(DEFAULT_PAN)
            .tan(DEFAULT_TAN)
            .tanCircleNumber(DEFAULT_TAN_CIRCLE_NUMBER)
            .citTdsLocation(DEFAULT_CIT_TDS_LOCATION)
            .formSignatory(DEFAULT_FORM_SIGNATORY)
            .pfNumber(DEFAULT_PF_NUMBER)
            .pfRegistrationDate(DEFAULT_PF_REGISTRATION_DATE)
            .pfSignatory(DEFAULT_PF_SIGNATORY)
            .esiNumber(DEFAULT_ESI_NUMBER)
            .esiRegistrationDate(DEFAULT_ESI_REGISTRATION_DATE)
            .esiSignatory(DEFAULT_ESI_SIGNATORY)
            .ptNumber(DEFAULT_PT_NUMBER)
            .ptRegistrationDate(DEFAULT_PT_REGISTRATION_DATE)
            .ptSignatory(DEFAULT_PT_SIGNATORY);
        return legalEntity;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LegalEntity createUpdatedEntity(EntityManager em) {
        LegalEntity legalEntity = new LegalEntity()
            .logoFilePath(UPDATED_LOGO_FILE_PATH)
            .logoFileName(UPDATED_LOGO_FILE_NAME)
            .logoFileExtension(UPDATED_LOGO_FILE_EXTENSION)
            .legalNameOfCollege(UPDATED_LEGAL_NAME_OF_COLLEGE)
            .typeOfCollege(UPDATED_TYPE_OF_COLLEGE)
            .dateOfIncorporation(UPDATED_DATE_OF_INCORPORATION)
            .registeredOfficeAddress(UPDATED_REGISTERED_OFFICE_ADDRESS)
            .collegeIdentificationNumber(UPDATED_COLLEGE_IDENTIFICATION_NUMBER)
            .pan(UPDATED_PAN)
            .tan(UPDATED_TAN)
            .tanCircleNumber(UPDATED_TAN_CIRCLE_NUMBER)
            .citTdsLocation(UPDATED_CIT_TDS_LOCATION)
            .formSignatory(UPDATED_FORM_SIGNATORY)
            .pfNumber(UPDATED_PF_NUMBER)
            .pfRegistrationDate(UPDATED_PF_REGISTRATION_DATE)
            .pfSignatory(UPDATED_PF_SIGNATORY)
            .esiNumber(UPDATED_ESI_NUMBER)
            .esiRegistrationDate(UPDATED_ESI_REGISTRATION_DATE)
            .esiSignatory(UPDATED_ESI_SIGNATORY)
            .ptNumber(UPDATED_PT_NUMBER)
            .ptRegistrationDate(UPDATED_PT_REGISTRATION_DATE)
            .ptSignatory(UPDATED_PT_SIGNATORY);
        return legalEntity;
    }

    @BeforeEach
    public void initTest() {
        legalEntity = createEntity(em);
    }

    @Test
    @Transactional
    public void createLegalEntity() throws Exception {
        int databaseSizeBeforeCreate = legalEntityRepository.findAll().size();

        // Create the LegalEntity
        LegalEntityDTO legalEntityDTO = legalEntityMapper.toDto(legalEntity);
        restLegalEntityMockMvc.perform(post("/api/legal-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalEntityDTO)))
            .andExpect(status().isCreated());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntityList = legalEntityRepository.findAll();
        assertThat(legalEntityList).hasSize(databaseSizeBeforeCreate + 1);
        LegalEntity testLegalEntity = legalEntityList.get(legalEntityList.size() - 1);
        assertThat(testLegalEntity.getLogoFilePath()).isEqualTo(DEFAULT_LOGO_FILE_PATH);
        assertThat(testLegalEntity.getLogoFileName()).isEqualTo(DEFAULT_LOGO_FILE_NAME);
        assertThat(testLegalEntity.getLogoFileExtension()).isEqualTo(DEFAULT_LOGO_FILE_EXTENSION);
        assertThat(testLegalEntity.getLegalNameOfCollege()).isEqualTo(DEFAULT_LEGAL_NAME_OF_COLLEGE);
        assertThat(testLegalEntity.getTypeOfCollege()).isEqualTo(DEFAULT_TYPE_OF_COLLEGE);
        assertThat(testLegalEntity.getDateOfIncorporation()).isEqualTo(DEFAULT_DATE_OF_INCORPORATION);
        assertThat(testLegalEntity.getRegisteredOfficeAddress()).isEqualTo(DEFAULT_REGISTERED_OFFICE_ADDRESS);
        assertThat(testLegalEntity.getCollegeIdentificationNumber()).isEqualTo(DEFAULT_COLLEGE_IDENTIFICATION_NUMBER);
        assertThat(testLegalEntity.getPan()).isEqualTo(DEFAULT_PAN);
        assertThat(testLegalEntity.getTan()).isEqualTo(DEFAULT_TAN);
        assertThat(testLegalEntity.getTanCircleNumber()).isEqualTo(DEFAULT_TAN_CIRCLE_NUMBER);
        assertThat(testLegalEntity.getCitTdsLocation()).isEqualTo(DEFAULT_CIT_TDS_LOCATION);
        assertThat(testLegalEntity.getFormSignatory()).isEqualTo(DEFAULT_FORM_SIGNATORY);
        assertThat(testLegalEntity.getPfNumber()).isEqualTo(DEFAULT_PF_NUMBER);
        assertThat(testLegalEntity.getPfRegistrationDate()).isEqualTo(DEFAULT_PF_REGISTRATION_DATE);
        assertThat(testLegalEntity.getPfSignatory()).isEqualTo(DEFAULT_PF_SIGNATORY);
        assertThat(testLegalEntity.getEsiNumber()).isEqualTo(DEFAULT_ESI_NUMBER);
        assertThat(testLegalEntity.getEsiRegistrationDate()).isEqualTo(DEFAULT_ESI_REGISTRATION_DATE);
        assertThat(testLegalEntity.getEsiSignatory()).isEqualTo(DEFAULT_ESI_SIGNATORY);
        assertThat(testLegalEntity.getPtNumber()).isEqualTo(DEFAULT_PT_NUMBER);
        assertThat(testLegalEntity.getPtRegistrationDate()).isEqualTo(DEFAULT_PT_REGISTRATION_DATE);
        assertThat(testLegalEntity.getPtSignatory()).isEqualTo(DEFAULT_PT_SIGNATORY);

        // Validate the LegalEntity in Elasticsearch
        verify(mockLegalEntitySearchRepository, times(1)).save(testLegalEntity);
    }

    @Test
    @Transactional
    public void createLegalEntityWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = legalEntityRepository.findAll().size();

        // Create the LegalEntity with an existing ID
        legalEntity.setId(1L);
        LegalEntityDTO legalEntityDTO = legalEntityMapper.toDto(legalEntity);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLegalEntityMockMvc.perform(post("/api/legal-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalEntityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntityList = legalEntityRepository.findAll();
        assertThat(legalEntityList).hasSize(databaseSizeBeforeCreate);

        // Validate the LegalEntity in Elasticsearch
        verify(mockLegalEntitySearchRepository, times(0)).save(legalEntity);
    }


    @Test
    @Transactional
    public void getAllLegalEntities() throws Exception {
        // Initialize the database
        legalEntityRepository.saveAndFlush(legalEntity);

        // Get all the legalEntityList
        restLegalEntityMockMvc.perform(get("/api/legal-entities?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(legalEntity.getId().intValue())))
            .andExpect(jsonPath("$.[*].logoFilePath").value(hasItem(DEFAULT_LOGO_FILE_PATH.toString())))
            .andExpect(jsonPath("$.[*].logoFileName").value(hasItem(DEFAULT_LOGO_FILE_NAME.toString())))
            .andExpect(jsonPath("$.[*].logoFileExtension").value(hasItem(DEFAULT_LOGO_FILE_EXTENSION.toString())))
            .andExpect(jsonPath("$.[*].legalNameOfCollege").value(hasItem(DEFAULT_LEGAL_NAME_OF_COLLEGE.toString())))
            .andExpect(jsonPath("$.[*].typeOfCollege").value(hasItem(DEFAULT_TYPE_OF_COLLEGE.toString())))
            .andExpect(jsonPath("$.[*].dateOfIncorporation").value(hasItem(DEFAULT_DATE_OF_INCORPORATION.toString())))
            .andExpect(jsonPath("$.[*].registeredOfficeAddress").value(hasItem(DEFAULT_REGISTERED_OFFICE_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].collegeIdentificationNumber").value(hasItem(DEFAULT_COLLEGE_IDENTIFICATION_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].pan").value(hasItem(DEFAULT_PAN.toString())))
            .andExpect(jsonPath("$.[*].tan").value(hasItem(DEFAULT_TAN.toString())))
            .andExpect(jsonPath("$.[*].tanCircleNumber").value(hasItem(DEFAULT_TAN_CIRCLE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].citTdsLocation").value(hasItem(DEFAULT_CIT_TDS_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].formSignatory").value(hasItem(DEFAULT_FORM_SIGNATORY.intValue())))
            .andExpect(jsonPath("$.[*].pfNumber").value(hasItem(DEFAULT_PF_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].pfRegistrationDate").value(hasItem(DEFAULT_PF_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].pfSignatory").value(hasItem(DEFAULT_PF_SIGNATORY.intValue())))
            .andExpect(jsonPath("$.[*].esiNumber").value(hasItem(DEFAULT_ESI_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].esiRegistrationDate").value(hasItem(DEFAULT_ESI_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].esiSignatory").value(hasItem(DEFAULT_ESI_SIGNATORY.intValue())))
            .andExpect(jsonPath("$.[*].ptNumber").value(hasItem(DEFAULT_PT_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].ptRegistrationDate").value(hasItem(DEFAULT_PT_REGISTRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].ptSignatory").value(hasItem(DEFAULT_PT_SIGNATORY.intValue())));
    }
    
    @Test
    @Transactional
    public void getLegalEntity() throws Exception {
        // Initialize the database
        legalEntityRepository.saveAndFlush(legalEntity);

        // Get the legalEntity
        restLegalEntityMockMvc.perform(get("/api/legal-entities/{id}", legalEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(legalEntity.getId().intValue()))
            .andExpect(jsonPath("$.logoFilePath").value(DEFAULT_LOGO_FILE_PATH.toString()))
            .andExpect(jsonPath("$.logoFileName").value(DEFAULT_LOGO_FILE_NAME.toString()))
            .andExpect(jsonPath("$.logoFileExtension").value(DEFAULT_LOGO_FILE_EXTENSION.toString()))
            .andExpect(jsonPath("$.legalNameOfCollege").value(DEFAULT_LEGAL_NAME_OF_COLLEGE.toString()))
            .andExpect(jsonPath("$.typeOfCollege").value(DEFAULT_TYPE_OF_COLLEGE.toString()))
            .andExpect(jsonPath("$.dateOfIncorporation").value(DEFAULT_DATE_OF_INCORPORATION.toString()))
            .andExpect(jsonPath("$.registeredOfficeAddress").value(DEFAULT_REGISTERED_OFFICE_ADDRESS.toString()))
            .andExpect(jsonPath("$.collegeIdentificationNumber").value(DEFAULT_COLLEGE_IDENTIFICATION_NUMBER.toString()))
            .andExpect(jsonPath("$.pan").value(DEFAULT_PAN.toString()))
            .andExpect(jsonPath("$.tan").value(DEFAULT_TAN.toString()))
            .andExpect(jsonPath("$.tanCircleNumber").value(DEFAULT_TAN_CIRCLE_NUMBER.toString()))
            .andExpect(jsonPath("$.citTdsLocation").value(DEFAULT_CIT_TDS_LOCATION.toString()))
            .andExpect(jsonPath("$.formSignatory").value(DEFAULT_FORM_SIGNATORY.intValue()))
            .andExpect(jsonPath("$.pfNumber").value(DEFAULT_PF_NUMBER.toString()))
            .andExpect(jsonPath("$.pfRegistrationDate").value(DEFAULT_PF_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.pfSignatory").value(DEFAULT_PF_SIGNATORY.intValue()))
            .andExpect(jsonPath("$.esiNumber").value(DEFAULT_ESI_NUMBER.toString()))
            .andExpect(jsonPath("$.esiRegistrationDate").value(DEFAULT_ESI_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.esiSignatory").value(DEFAULT_ESI_SIGNATORY.intValue()))
            .andExpect(jsonPath("$.ptNumber").value(DEFAULT_PT_NUMBER.toString()))
            .andExpect(jsonPath("$.ptRegistrationDate").value(DEFAULT_PT_REGISTRATION_DATE.toString()))
            .andExpect(jsonPath("$.ptSignatory").value(DEFAULT_PT_SIGNATORY.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingLegalEntity() throws Exception {
        // Get the legalEntity
        restLegalEntityMockMvc.perform(get("/api/legal-entities/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLegalEntity() throws Exception {
        // Initialize the database
        legalEntityRepository.saveAndFlush(legalEntity);

        int databaseSizeBeforeUpdate = legalEntityRepository.findAll().size();

        // Update the legalEntity
        LegalEntity updatedLegalEntity = legalEntityRepository.findById(legalEntity.getId()).get();
        // Disconnect from session so that the updates on updatedLegalEntity are not directly saved in db
        em.detach(updatedLegalEntity);
        updatedLegalEntity
            .logoFilePath(UPDATED_LOGO_FILE_PATH)
            .logoFileName(UPDATED_LOGO_FILE_NAME)
            .logoFileExtension(UPDATED_LOGO_FILE_EXTENSION)
            .legalNameOfCollege(UPDATED_LEGAL_NAME_OF_COLLEGE)
            .typeOfCollege(UPDATED_TYPE_OF_COLLEGE)
            .dateOfIncorporation(UPDATED_DATE_OF_INCORPORATION)
            .registeredOfficeAddress(UPDATED_REGISTERED_OFFICE_ADDRESS)
            .collegeIdentificationNumber(UPDATED_COLLEGE_IDENTIFICATION_NUMBER)
            .pan(UPDATED_PAN)
            .tan(UPDATED_TAN)
            .tanCircleNumber(UPDATED_TAN_CIRCLE_NUMBER)
            .citTdsLocation(UPDATED_CIT_TDS_LOCATION)
            .formSignatory(UPDATED_FORM_SIGNATORY)
            .pfNumber(UPDATED_PF_NUMBER)
            .pfRegistrationDate(UPDATED_PF_REGISTRATION_DATE)
            .pfSignatory(UPDATED_PF_SIGNATORY)
            .esiNumber(UPDATED_ESI_NUMBER)
            .esiRegistrationDate(UPDATED_ESI_REGISTRATION_DATE)
            .esiSignatory(UPDATED_ESI_SIGNATORY)
            .ptNumber(UPDATED_PT_NUMBER)
            .ptRegistrationDate(UPDATED_PT_REGISTRATION_DATE)
            .ptSignatory(UPDATED_PT_SIGNATORY);
        LegalEntityDTO legalEntityDTO = legalEntityMapper.toDto(updatedLegalEntity);

        restLegalEntityMockMvc.perform(put("/api/legal-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalEntityDTO)))
            .andExpect(status().isOk());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntityList = legalEntityRepository.findAll();
        assertThat(legalEntityList).hasSize(databaseSizeBeforeUpdate);
        LegalEntity testLegalEntity = legalEntityList.get(legalEntityList.size() - 1);
        assertThat(testLegalEntity.getLogoFilePath()).isEqualTo(UPDATED_LOGO_FILE_PATH);
        assertThat(testLegalEntity.getLogoFileName()).isEqualTo(UPDATED_LOGO_FILE_NAME);
        assertThat(testLegalEntity.getLogoFileExtension()).isEqualTo(UPDATED_LOGO_FILE_EXTENSION);
        assertThat(testLegalEntity.getLegalNameOfCollege()).isEqualTo(UPDATED_LEGAL_NAME_OF_COLLEGE);
        assertThat(testLegalEntity.getTypeOfCollege()).isEqualTo(UPDATED_TYPE_OF_COLLEGE);
        assertThat(testLegalEntity.getDateOfIncorporation()).isEqualTo(UPDATED_DATE_OF_INCORPORATION);
        assertThat(testLegalEntity.getRegisteredOfficeAddress()).isEqualTo(UPDATED_REGISTERED_OFFICE_ADDRESS);
        assertThat(testLegalEntity.getCollegeIdentificationNumber()).isEqualTo(UPDATED_COLLEGE_IDENTIFICATION_NUMBER);
        assertThat(testLegalEntity.getPan()).isEqualTo(UPDATED_PAN);
        assertThat(testLegalEntity.getTan()).isEqualTo(UPDATED_TAN);
        assertThat(testLegalEntity.getTanCircleNumber()).isEqualTo(UPDATED_TAN_CIRCLE_NUMBER);
        assertThat(testLegalEntity.getCitTdsLocation()).isEqualTo(UPDATED_CIT_TDS_LOCATION);
        assertThat(testLegalEntity.getFormSignatory()).isEqualTo(UPDATED_FORM_SIGNATORY);
        assertThat(testLegalEntity.getPfNumber()).isEqualTo(UPDATED_PF_NUMBER);
        assertThat(testLegalEntity.getPfRegistrationDate()).isEqualTo(UPDATED_PF_REGISTRATION_DATE);
        assertThat(testLegalEntity.getPfSignatory()).isEqualTo(UPDATED_PF_SIGNATORY);
        assertThat(testLegalEntity.getEsiNumber()).isEqualTo(UPDATED_ESI_NUMBER);
        assertThat(testLegalEntity.getEsiRegistrationDate()).isEqualTo(UPDATED_ESI_REGISTRATION_DATE);
        assertThat(testLegalEntity.getEsiSignatory()).isEqualTo(UPDATED_ESI_SIGNATORY);
        assertThat(testLegalEntity.getPtNumber()).isEqualTo(UPDATED_PT_NUMBER);
        assertThat(testLegalEntity.getPtRegistrationDate()).isEqualTo(UPDATED_PT_REGISTRATION_DATE);
        assertThat(testLegalEntity.getPtSignatory()).isEqualTo(UPDATED_PT_SIGNATORY);

        // Validate the LegalEntity in Elasticsearch
        verify(mockLegalEntitySearchRepository, times(1)).save(testLegalEntity);
    }

    @Test
    @Transactional
    public void updateNonExistingLegalEntity() throws Exception {
        int databaseSizeBeforeUpdate = legalEntityRepository.findAll().size();

        // Create the LegalEntity
        LegalEntityDTO legalEntityDTO = legalEntityMapper.toDto(legalEntity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLegalEntityMockMvc.perform(put("/api/legal-entities")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(legalEntityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LegalEntity in the database
        List<LegalEntity> legalEntityList = legalEntityRepository.findAll();
        assertThat(legalEntityList).hasSize(databaseSizeBeforeUpdate);

        // Validate the LegalEntity in Elasticsearch
        verify(mockLegalEntitySearchRepository, times(0)).save(legalEntity);
    }

    @Test
    @Transactional
    public void deleteLegalEntity() throws Exception {
        // Initialize the database
        legalEntityRepository.saveAndFlush(legalEntity);

        int databaseSizeBeforeDelete = legalEntityRepository.findAll().size();

        // Delete the legalEntity
        restLegalEntityMockMvc.perform(delete("/api/legal-entities/{id}", legalEntity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LegalEntity> legalEntityList = legalEntityRepository.findAll();
        assertThat(legalEntityList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the LegalEntity in Elasticsearch
        verify(mockLegalEntitySearchRepository, times(1)).deleteById(legalEntity.getId());
    }

//    @Test
//    @Transactional
//    public void searchLegalEntity() throws Exception {
//        // Initialize the database
//        legalEntityRepository.saveAndFlush(legalEntity);
//        when(mockLegalEntitySearchRepository.search(queryStringQuery("id:" + legalEntity.getId())))
//            .thenReturn(Collections.singletonList(legalEntity));
//        // Search the legalEntity
//        restLegalEntityMockMvc.perform(get("/api/_search/legal-entities?query=id:" + legalEntity.getId()))
//            .andExpect(status().isOk())
//            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//            .andExpect(jsonPath("$.[*].id").value(hasItem(legalEntity.getId().intValue())))
//            .andExpect(jsonPath("$.[*].logoFilePath").value(hasItem(DEFAULT_LOGO_FILE_PATH)))
//            .andExpect(jsonPath("$.[*].logoFileName").value(hasItem(DEFAULT_LOGO_FILE_NAME)))
//            .andExpect(jsonPath("$.[*].logoFileExtension").value(hasItem(DEFAULT_LOGO_FILE_EXTENSION)))
//            .andExpect(jsonPath("$.[*].legalNameOfCollege").value(hasItem(DEFAULT_LEGAL_NAME_OF_COLLEGE)))
//            .andExpect(jsonPath("$.[*].typeOfCollege").value(hasItem(DEFAULT_TYPE_OF_COLLEGE)))
//            .andExpect(jsonPath("$.[*].dateOfIncorporation").value(hasItem(DEFAULT_DATE_OF_INCORPORATION.toString())))
//            .andExpect(jsonPath("$.[*].registeredOfficeAddress").value(hasItem(DEFAULT_REGISTERED_OFFICE_ADDRESS)))
//            .andExpect(jsonPath("$.[*].collegeIdentificationNumber").value(hasItem(DEFAULT_COLLEGE_IDENTIFICATION_NUMBER)))
//            .andExpect(jsonPath("$.[*].pan").value(hasItem(DEFAULT_PAN)))
//            .andExpect(jsonPath("$.[*].tan").value(hasItem(DEFAULT_TAN)))
//            .andExpect(jsonPath("$.[*].tanCircleNumber").value(hasItem(DEFAULT_TAN_CIRCLE_NUMBER)))
//            .andExpect(jsonPath("$.[*].citTdsLocation").value(hasItem(DEFAULT_CIT_TDS_LOCATION)))
//            .andExpect(jsonPath("$.[*].formSignatory").value(hasItem(DEFAULT_FORM_SIGNATORY.intValue())))
//            .andExpect(jsonPath("$.[*].pfNumber").value(hasItem(DEFAULT_PF_NUMBER)))
//            .andExpect(jsonPath("$.[*].pfRegistrationDate").value(hasItem(DEFAULT_PF_REGISTRATION_DATE.toString())))
//            .andExpect(jsonPath("$.[*].pfSignatory").value(hasItem(DEFAULT_PF_SIGNATORY.intValue())))
//            .andExpect(jsonPath("$.[*].esiNumber").value(hasItem(DEFAULT_ESI_NUMBER)))
//            .andExpect(jsonPath("$.[*].esiRegistrationDate").value(hasItem(DEFAULT_ESI_REGISTRATION_DATE.toString())))
//            .andExpect(jsonPath("$.[*].esiSignatory").value(hasItem(DEFAULT_ESI_SIGNATORY.intValue())))
//            .andExpect(jsonPath("$.[*].ptNumber").value(hasItem(DEFAULT_PT_NUMBER)))
//            .andExpect(jsonPath("$.[*].ptRegistrationDate").value(hasItem(DEFAULT_PT_REGISTRATION_DATE.toString())))
//            .andExpect(jsonPath("$.[*].ptSignatory").value(hasItem(DEFAULT_PT_SIGNATORY.intValue())));
//    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LegalEntity.class);
        LegalEntity legalEntity1 = new LegalEntity();
        legalEntity1.setId(1L);
        LegalEntity legalEntity2 = new LegalEntity();
        legalEntity2.setId(legalEntity1.getId());
        assertThat(legalEntity1).isEqualTo(legalEntity2);
        legalEntity2.setId(2L);
        assertThat(legalEntity1).isNotEqualTo(legalEntity2);
        legalEntity1.setId(null);
        assertThat(legalEntity1).isNotEqualTo(legalEntity2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LegalEntityDTO.class);
        LegalEntityDTO legalEntityDTO1 = new LegalEntityDTO();
        legalEntityDTO1.setId(1L);
        LegalEntityDTO legalEntityDTO2 = new LegalEntityDTO();
        assertThat(legalEntityDTO1).isNotEqualTo(legalEntityDTO2);
        legalEntityDTO2.setId(legalEntityDTO1.getId());
        assertThat(legalEntityDTO1).isEqualTo(legalEntityDTO2);
        legalEntityDTO2.setId(2L);
        assertThat(legalEntityDTO1).isNotEqualTo(legalEntityDTO2);
        legalEntityDTO1.setId(null);
        assertThat(legalEntityDTO1).isNotEqualTo(legalEntityDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(legalEntityMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(legalEntityMapper.fromId(null)).isNull();
    }
}
