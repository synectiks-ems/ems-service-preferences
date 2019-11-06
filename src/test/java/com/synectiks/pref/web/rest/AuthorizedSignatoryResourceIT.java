package com.synectiks.pref.web.rest;

import com.synectiks.pref.PreferencesApp;
import com.synectiks.pref.domain.AuthorizedSignatory;
import com.synectiks.pref.repository.AuthorizedSignatoryRepository;
import com.synectiks.pref.repository.search.AuthorizedSignatorySearchRepository;
import com.synectiks.pref.service.AuthorizedSignatoryService;
import com.synectiks.pref.service.dto.AuthorizedSignatoryDTO;
import com.synectiks.pref.service.mapper.AuthorizedSignatoryMapper;
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
import java.util.Collections;
import java.util.List;

import static com.synectiks.pref.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link AuthorizedSignatoryResource} REST controller.
 */
@SpringBootTest(classes = PreferencesApp.class)
public class AuthorizedSignatoryResourceIT {

    private static final String DEFAULT_SIGNATORY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATORY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGNATORY_FATHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATORY_FATHER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SIGNATORY_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATORY_DESIGNATION = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_3 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_3 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_4 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_4 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_5 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_5 = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PAN_CARD_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PAN_CARD_NUMBER = "BBBBBBBBBB";

    @Autowired
    private AuthorizedSignatoryRepository authorizedSignatoryRepository;

    @Autowired
    private AuthorizedSignatoryMapper authorizedSignatoryMapper;

    @Autowired
    private AuthorizedSignatoryService authorizedSignatoryService;

    /**
     * This repository is mocked in the com.synectiks.pref.repository.search test package.
     *
     * @see com.synectiks.pref.repository.search.AuthorizedSignatorySearchRepositoryMockConfiguration
     */
    @Autowired
    private AuthorizedSignatorySearchRepository mockAuthorizedSignatorySearchRepository;

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

    private MockMvc restAuthorizedSignatoryMockMvc;

    private AuthorizedSignatory authorizedSignatory;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AuthorizedSignatoryResource authorizedSignatoryResource = new AuthorizedSignatoryResource(authorizedSignatoryService);
        this.restAuthorizedSignatoryMockMvc = MockMvcBuilders.standaloneSetup(authorizedSignatoryResource)
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
    public static AuthorizedSignatory createEntity(EntityManager em) {
        AuthorizedSignatory authorizedSignatory = new AuthorizedSignatory()
            .signatoryName(DEFAULT_SIGNATORY_NAME)
            .signatoryFatherName(DEFAULT_SIGNATORY_FATHER_NAME)
            .signatoryDesignation(DEFAULT_SIGNATORY_DESIGNATION)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .address3(DEFAULT_ADDRESS_3)
            .address4(DEFAULT_ADDRESS_4)
            .address5(DEFAULT_ADDRESS_5)
            .email(DEFAULT_EMAIL)
            .panCardNumber(DEFAULT_PAN_CARD_NUMBER);
        return authorizedSignatory;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AuthorizedSignatory createUpdatedEntity(EntityManager em) {
        AuthorizedSignatory authorizedSignatory = new AuthorizedSignatory()
            .signatoryName(UPDATED_SIGNATORY_NAME)
            .signatoryFatherName(UPDATED_SIGNATORY_FATHER_NAME)
            .signatoryDesignation(UPDATED_SIGNATORY_DESIGNATION)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .address4(UPDATED_ADDRESS_4)
            .address5(UPDATED_ADDRESS_5)
            .email(UPDATED_EMAIL)
            .panCardNumber(UPDATED_PAN_CARD_NUMBER);
        return authorizedSignatory;
    }

    @BeforeEach
    public void initTest() {
        authorizedSignatory = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuthorizedSignatory() throws Exception {
        int databaseSizeBeforeCreate = authorizedSignatoryRepository.findAll().size();

        // Create the AuthorizedSignatory
        AuthorizedSignatoryDTO authorizedSignatoryDTO = authorizedSignatoryMapper.toDto(authorizedSignatory);
        restAuthorizedSignatoryMockMvc.perform(post("/api/authorized-signatories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizedSignatoryDTO)))
            .andExpect(status().isCreated());

        // Validate the AuthorizedSignatory in the database
        List<AuthorizedSignatory> authorizedSignatoryList = authorizedSignatoryRepository.findAll();
        assertThat(authorizedSignatoryList).hasSize(databaseSizeBeforeCreate + 1);
        AuthorizedSignatory testAuthorizedSignatory = authorizedSignatoryList.get(authorizedSignatoryList.size() - 1);
        assertThat(testAuthorizedSignatory.getSignatoryName()).isEqualTo(DEFAULT_SIGNATORY_NAME);
        assertThat(testAuthorizedSignatory.getSignatoryFatherName()).isEqualTo(DEFAULT_SIGNATORY_FATHER_NAME);
        assertThat(testAuthorizedSignatory.getSignatoryDesignation()).isEqualTo(DEFAULT_SIGNATORY_DESIGNATION);
        assertThat(testAuthorizedSignatory.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testAuthorizedSignatory.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testAuthorizedSignatory.getAddress3()).isEqualTo(DEFAULT_ADDRESS_3);
        assertThat(testAuthorizedSignatory.getAddress4()).isEqualTo(DEFAULT_ADDRESS_4);
        assertThat(testAuthorizedSignatory.getAddress5()).isEqualTo(DEFAULT_ADDRESS_5);
        assertThat(testAuthorizedSignatory.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAuthorizedSignatory.getPanCardNumber()).isEqualTo(DEFAULT_PAN_CARD_NUMBER);

        // Validate the AuthorizedSignatory in Elasticsearch
        verify(mockAuthorizedSignatorySearchRepository, times(1)).save(testAuthorizedSignatory);
    }

    @Test
    @Transactional
    public void createAuthorizedSignatoryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = authorizedSignatoryRepository.findAll().size();

        // Create the AuthorizedSignatory with an existing ID
        authorizedSignatory.setId(1L);
        AuthorizedSignatoryDTO authorizedSignatoryDTO = authorizedSignatoryMapper.toDto(authorizedSignatory);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuthorizedSignatoryMockMvc.perform(post("/api/authorized-signatories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizedSignatoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuthorizedSignatory in the database
        List<AuthorizedSignatory> authorizedSignatoryList = authorizedSignatoryRepository.findAll();
        assertThat(authorizedSignatoryList).hasSize(databaseSizeBeforeCreate);

        // Validate the AuthorizedSignatory in Elasticsearch
        verify(mockAuthorizedSignatorySearchRepository, times(0)).save(authorizedSignatory);
    }


    @Test
    @Transactional
    public void getAllAuthorizedSignatories() throws Exception {
        // Initialize the database
        authorizedSignatoryRepository.saveAndFlush(authorizedSignatory);

        // Get all the authorizedSignatoryList
        restAuthorizedSignatoryMockMvc.perform(get("/api/authorized-signatories?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorizedSignatory.getId().intValue())))
            .andExpect(jsonPath("$.[*].signatoryName").value(hasItem(DEFAULT_SIGNATORY_NAME.toString())))
            .andExpect(jsonPath("$.[*].signatoryFatherName").value(hasItem(DEFAULT_SIGNATORY_FATHER_NAME.toString())))
            .andExpect(jsonPath("$.[*].signatoryDesignation").value(hasItem(DEFAULT_SIGNATORY_DESIGNATION.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3.toString())))
            .andExpect(jsonPath("$.[*].address4").value(hasItem(DEFAULT_ADDRESS_4.toString())))
            .andExpect(jsonPath("$.[*].address5").value(hasItem(DEFAULT_ADDRESS_5.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].panCardNumber").value(hasItem(DEFAULT_PAN_CARD_NUMBER.toString())));
    }
    
    @Test
    @Transactional
    public void getAuthorizedSignatory() throws Exception {
        // Initialize the database
        authorizedSignatoryRepository.saveAndFlush(authorizedSignatory);

        // Get the authorizedSignatory
        restAuthorizedSignatoryMockMvc.perform(get("/api/authorized-signatories/{id}", authorizedSignatory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(authorizedSignatory.getId().intValue()))
            .andExpect(jsonPath("$.signatoryName").value(DEFAULT_SIGNATORY_NAME.toString()))
            .andExpect(jsonPath("$.signatoryFatherName").value(DEFAULT_SIGNATORY_FATHER_NAME.toString()))
            .andExpect(jsonPath("$.signatoryDesignation").value(DEFAULT_SIGNATORY_DESIGNATION.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.address3").value(DEFAULT_ADDRESS_3.toString()))
            .andExpect(jsonPath("$.address4").value(DEFAULT_ADDRESS_4.toString()))
            .andExpect(jsonPath("$.address5").value(DEFAULT_ADDRESS_5.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.panCardNumber").value(DEFAULT_PAN_CARD_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuthorizedSignatory() throws Exception {
        // Get the authorizedSignatory
        restAuthorizedSignatoryMockMvc.perform(get("/api/authorized-signatories/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuthorizedSignatory() throws Exception {
        // Initialize the database
        authorizedSignatoryRepository.saveAndFlush(authorizedSignatory);

        int databaseSizeBeforeUpdate = authorizedSignatoryRepository.findAll().size();

        // Update the authorizedSignatory
        AuthorizedSignatory updatedAuthorizedSignatory = authorizedSignatoryRepository.findById(authorizedSignatory.getId()).get();
        // Disconnect from session so that the updates on updatedAuthorizedSignatory are not directly saved in db
        em.detach(updatedAuthorizedSignatory);
        updatedAuthorizedSignatory
            .signatoryName(UPDATED_SIGNATORY_NAME)
            .signatoryFatherName(UPDATED_SIGNATORY_FATHER_NAME)
            .signatoryDesignation(UPDATED_SIGNATORY_DESIGNATION)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .address3(UPDATED_ADDRESS_3)
            .address4(UPDATED_ADDRESS_4)
            .address5(UPDATED_ADDRESS_5)
            .email(UPDATED_EMAIL)
            .panCardNumber(UPDATED_PAN_CARD_NUMBER);
        AuthorizedSignatoryDTO authorizedSignatoryDTO = authorizedSignatoryMapper.toDto(updatedAuthorizedSignatory);

        restAuthorizedSignatoryMockMvc.perform(put("/api/authorized-signatories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizedSignatoryDTO)))
            .andExpect(status().isOk());

        // Validate the AuthorizedSignatory in the database
        List<AuthorizedSignatory> authorizedSignatoryList = authorizedSignatoryRepository.findAll();
        assertThat(authorizedSignatoryList).hasSize(databaseSizeBeforeUpdate);
        AuthorizedSignatory testAuthorizedSignatory = authorizedSignatoryList.get(authorizedSignatoryList.size() - 1);
        assertThat(testAuthorizedSignatory.getSignatoryName()).isEqualTo(UPDATED_SIGNATORY_NAME);
        assertThat(testAuthorizedSignatory.getSignatoryFatherName()).isEqualTo(UPDATED_SIGNATORY_FATHER_NAME);
        assertThat(testAuthorizedSignatory.getSignatoryDesignation()).isEqualTo(UPDATED_SIGNATORY_DESIGNATION);
        assertThat(testAuthorizedSignatory.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testAuthorizedSignatory.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testAuthorizedSignatory.getAddress3()).isEqualTo(UPDATED_ADDRESS_3);
        assertThat(testAuthorizedSignatory.getAddress4()).isEqualTo(UPDATED_ADDRESS_4);
        assertThat(testAuthorizedSignatory.getAddress5()).isEqualTo(UPDATED_ADDRESS_5);
        assertThat(testAuthorizedSignatory.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAuthorizedSignatory.getPanCardNumber()).isEqualTo(UPDATED_PAN_CARD_NUMBER);

        // Validate the AuthorizedSignatory in Elasticsearch
        verify(mockAuthorizedSignatorySearchRepository, times(1)).save(testAuthorizedSignatory);
    }

    @Test
    @Transactional
    public void updateNonExistingAuthorizedSignatory() throws Exception {
        int databaseSizeBeforeUpdate = authorizedSignatoryRepository.findAll().size();

        // Create the AuthorizedSignatory
        AuthorizedSignatoryDTO authorizedSignatoryDTO = authorizedSignatoryMapper.toDto(authorizedSignatory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuthorizedSignatoryMockMvc.perform(put("/api/authorized-signatories")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(authorizedSignatoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AuthorizedSignatory in the database
        List<AuthorizedSignatory> authorizedSignatoryList = authorizedSignatoryRepository.findAll();
        assertThat(authorizedSignatoryList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AuthorizedSignatory in Elasticsearch
        verify(mockAuthorizedSignatorySearchRepository, times(0)).save(authorizedSignatory);
    }

    @Test
    @Transactional
    public void deleteAuthorizedSignatory() throws Exception {
        // Initialize the database
        authorizedSignatoryRepository.saveAndFlush(authorizedSignatory);

        int databaseSizeBeforeDelete = authorizedSignatoryRepository.findAll().size();

        // Delete the authorizedSignatory
        restAuthorizedSignatoryMockMvc.perform(delete("/api/authorized-signatories/{id}", authorizedSignatory.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AuthorizedSignatory> authorizedSignatoryList = authorizedSignatoryRepository.findAll();
        assertThat(authorizedSignatoryList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AuthorizedSignatory in Elasticsearch
        verify(mockAuthorizedSignatorySearchRepository, times(1)).deleteById(authorizedSignatory.getId());
    }

    @Test
    @Transactional
    public void searchAuthorizedSignatory() throws Exception {
        // Initialize the database
        authorizedSignatoryRepository.saveAndFlush(authorizedSignatory);
        when(mockAuthorizedSignatorySearchRepository.search(queryStringQuery("id:" + authorizedSignatory.getId())))
            .thenReturn(Collections.singletonList(authorizedSignatory));
        // Search the authorizedSignatory
        restAuthorizedSignatoryMockMvc.perform(get("/api/_search/authorized-signatories?query=id:" + authorizedSignatory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(authorizedSignatory.getId().intValue())))
            .andExpect(jsonPath("$.[*].signatoryName").value(hasItem(DEFAULT_SIGNATORY_NAME)))
            .andExpect(jsonPath("$.[*].signatoryFatherName").value(hasItem(DEFAULT_SIGNATORY_FATHER_NAME)))
            .andExpect(jsonPath("$.[*].signatoryDesignation").value(hasItem(DEFAULT_SIGNATORY_DESIGNATION)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].address3").value(hasItem(DEFAULT_ADDRESS_3)))
            .andExpect(jsonPath("$.[*].address4").value(hasItem(DEFAULT_ADDRESS_4)))
            .andExpect(jsonPath("$.[*].address5").value(hasItem(DEFAULT_ADDRESS_5)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].panCardNumber").value(hasItem(DEFAULT_PAN_CARD_NUMBER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorizedSignatory.class);
        AuthorizedSignatory authorizedSignatory1 = new AuthorizedSignatory();
        authorizedSignatory1.setId(1L);
        AuthorizedSignatory authorizedSignatory2 = new AuthorizedSignatory();
        authorizedSignatory2.setId(authorizedSignatory1.getId());
        assertThat(authorizedSignatory1).isEqualTo(authorizedSignatory2);
        authorizedSignatory2.setId(2L);
        assertThat(authorizedSignatory1).isNotEqualTo(authorizedSignatory2);
        authorizedSignatory1.setId(null);
        assertThat(authorizedSignatory1).isNotEqualTo(authorizedSignatory2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AuthorizedSignatoryDTO.class);
        AuthorizedSignatoryDTO authorizedSignatoryDTO1 = new AuthorizedSignatoryDTO();
        authorizedSignatoryDTO1.setId(1L);
        AuthorizedSignatoryDTO authorizedSignatoryDTO2 = new AuthorizedSignatoryDTO();
        assertThat(authorizedSignatoryDTO1).isNotEqualTo(authorizedSignatoryDTO2);
        authorizedSignatoryDTO2.setId(authorizedSignatoryDTO1.getId());
        assertThat(authorizedSignatoryDTO1).isEqualTo(authorizedSignatoryDTO2);
        authorizedSignatoryDTO2.setId(2L);
        assertThat(authorizedSignatoryDTO1).isNotEqualTo(authorizedSignatoryDTO2);
        authorizedSignatoryDTO1.setId(null);
        assertThat(authorizedSignatoryDTO1).isNotEqualTo(authorizedSignatoryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(authorizedSignatoryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(authorizedSignatoryMapper.fromId(null)).isNull();
    }
}
