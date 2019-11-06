package com.synectiks.pref.web.rest;

import com.synectiks.pref.PreferencesApp;
import com.synectiks.pref.domain.Branch;
import com.synectiks.pref.repository.BranchRepository;
import com.synectiks.pref.repository.search.BranchSearchRepository;
import com.synectiks.pref.service.BranchService;
import com.synectiks.pref.service.dto.BranchDTO;
import com.synectiks.pref.service.mapper.BranchMapper;
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
 * Integration tests for the {@Link BranchResource} REST controller.
 */
@SpringBootTest(classes = PreferencesApp.class)
public class BranchResourceIT {

    private static final String DEFAULT_BRANCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_BRANCH_HEAD = "AAAAAAAAAA";
    private static final String UPDATED_BRANCH_HEAD = "BBBBBBBBBB";

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private BranchService branchService;

    /**
     * This repository is mocked in the com.synectiks.pref.repository.search test package.
     *
     * @see com.synectiks.pref.repository.search.BranchSearchRepositoryMockConfiguration
     */
    @Autowired
    private BranchSearchRepository mockBranchSearchRepository;

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

    private MockMvc restBranchMockMvc;

    private Branch branch;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BranchResource branchResource = new BranchResource(branchService);
        this.restBranchMockMvc = MockMvcBuilders.standaloneSetup(branchResource)
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
    public static Branch createEntity(EntityManager em) {
        Branch branch = new Branch()
            .branchName(DEFAULT_BRANCH_NAME)
            .address1(DEFAULT_ADDRESS_1)
            .address2(DEFAULT_ADDRESS_2)
            .branchHead(DEFAULT_BRANCH_HEAD);
        return branch;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Branch createUpdatedEntity(EntityManager em) {
        Branch branch = new Branch()
            .branchName(UPDATED_BRANCH_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .branchHead(UPDATED_BRANCH_HEAD);
        return branch;
    }

    @BeforeEach
    public void initTest() {
        branch = createEntity(em);
    }

    @Test
    @Transactional
    public void createBranch() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);
        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isCreated());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate + 1);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchName()).isEqualTo(DEFAULT_BRANCH_NAME);
        assertThat(testBranch.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testBranch.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testBranch.getBranchHead()).isEqualTo(DEFAULT_BRANCH_HEAD);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(1)).save(testBranch);
    }

    @Test
    @Transactional
    public void createBranchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = branchRepository.findAll().size();

        // Create the Branch with an existing ID
        branch.setId(1L);
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBranchMockMvc.perform(post("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeCreate);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(0)).save(branch);
    }


    @Test
    @Transactional
    public void getAllBranches() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get all the branchList
        restBranchMockMvc.perform(get("/api/branches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME.toString())))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
            .andExpect(jsonPath("$.[*].branchHead").value(hasItem(DEFAULT_BRANCH_HEAD.toString())));
    }
    
    @Test
    @Transactional
    public void getBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(branch.getId().intValue()))
            .andExpect(jsonPath("$.branchName").value(DEFAULT_BRANCH_NAME.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.branchHead").value(DEFAULT_BRANCH_HEAD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBranch() throws Exception {
        // Get the branch
        restBranchMockMvc.perform(get("/api/branches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Update the branch
        Branch updatedBranch = branchRepository.findById(branch.getId()).get();
        // Disconnect from session so that the updates on updatedBranch are not directly saved in db
        em.detach(updatedBranch);
        updatedBranch
            .branchName(UPDATED_BRANCH_NAME)
            .address1(UPDATED_ADDRESS_1)
            .address2(UPDATED_ADDRESS_2)
            .branchHead(UPDATED_BRANCH_HEAD);
        BranchDTO branchDTO = branchMapper.toDto(updatedBranch);

        restBranchMockMvc.perform(put("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isOk());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);
        Branch testBranch = branchList.get(branchList.size() - 1);
        assertThat(testBranch.getBranchName()).isEqualTo(UPDATED_BRANCH_NAME);
        assertThat(testBranch.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testBranch.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testBranch.getBranchHead()).isEqualTo(UPDATED_BRANCH_HEAD);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(1)).save(testBranch);
    }

    @Test
    @Transactional
    public void updateNonExistingBranch() throws Exception {
        int databaseSizeBeforeUpdate = branchRepository.findAll().size();

        // Create the Branch
        BranchDTO branchDTO = branchMapper.toDto(branch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBranchMockMvc.perform(put("/api/branches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(branchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Branch in the database
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(0)).save(branch);
    }

    @Test
    @Transactional
    public void deleteBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);

        int databaseSizeBeforeDelete = branchRepository.findAll().size();

        // Delete the branch
        restBranchMockMvc.perform(delete("/api/branches/{id}", branch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Branch> branchList = branchRepository.findAll();
        assertThat(branchList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Branch in Elasticsearch
        verify(mockBranchSearchRepository, times(1)).deleteById(branch.getId());
    }

    @Test
    @Transactional
    public void searchBranch() throws Exception {
        // Initialize the database
        branchRepository.saveAndFlush(branch);
        when(mockBranchSearchRepository.search(queryStringQuery("id:" + branch.getId())))
            .thenReturn(Collections.singletonList(branch));
        // Search the branch
        restBranchMockMvc.perform(get("/api/_search/branches?query=id:" + branch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(branch.getId().intValue())))
            .andExpect(jsonPath("$.[*].branchName").value(hasItem(DEFAULT_BRANCH_NAME)))
            .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].branchHead").value(hasItem(DEFAULT_BRANCH_HEAD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Branch.class);
        Branch branch1 = new Branch();
        branch1.setId(1L);
        Branch branch2 = new Branch();
        branch2.setId(branch1.getId());
        assertThat(branch1).isEqualTo(branch2);
        branch2.setId(2L);
        assertThat(branch1).isNotEqualTo(branch2);
        branch1.setId(null);
        assertThat(branch1).isNotEqualTo(branch2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BranchDTO.class);
        BranchDTO branchDTO1 = new BranchDTO();
        branchDTO1.setId(1L);
        BranchDTO branchDTO2 = new BranchDTO();
        assertThat(branchDTO1).isNotEqualTo(branchDTO2);
        branchDTO2.setId(branchDTO1.getId());
        assertThat(branchDTO1).isEqualTo(branchDTO2);
        branchDTO2.setId(2L);
        assertThat(branchDTO1).isNotEqualTo(branchDTO2);
        branchDTO1.setId(null);
        assertThat(branchDTO1).isNotEqualTo(branchDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(branchMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(branchMapper.fromId(null)).isNull();
    }
}
