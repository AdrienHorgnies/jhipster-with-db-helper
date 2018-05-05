package com.dbh.dbh.web.rest;

import com.dbh.dbh.DbhTravisTestCaseApp;

import com.dbh.dbh.domain.EntityF;
import com.dbh.dbh.repository.EntityFRepository;
import com.dbh.dbh.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EntityFResource REST controller.
 *
 * @see EntityFResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbhTravisTestCaseApp.class)
public class EntityFResourceIntTest {

    private static final String DEFAULT_FIELD_FA = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_FA = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_FB = 1;
    private static final Integer UPDATED_FIELD_FB = 2;

    @Autowired
    private EntityFRepository entityFRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityFMockMvc;

    private EntityF entityF;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityFResource entityFResource = new EntityFResource(entityFRepository);
        this.restEntityFMockMvc = MockMvcBuilders.standaloneSetup(entityFResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EntityF createEntity(EntityManager em) {
        EntityF entityF = new EntityF()
            .fieldFA(DEFAULT_FIELD_FA)
            .fieldFB(DEFAULT_FIELD_FB);
        return entityF;
    }

    @Before
    public void initTest() {
        entityF = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityF() throws Exception {
        int databaseSizeBeforeCreate = entityFRepository.findAll().size();

        // Create the EntityF
        restEntityFMockMvc.perform(post("/api/entity-fs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityF)))
            .andExpect(status().isCreated());

        // Validate the EntityF in the database
        List<EntityF> entityFList = entityFRepository.findAll();
        assertThat(entityFList).hasSize(databaseSizeBeforeCreate + 1);
        EntityF testEntityF = entityFList.get(entityFList.size() - 1);
        assertThat(testEntityF.getFieldFA()).isEqualTo(DEFAULT_FIELD_FA);
        assertThat(testEntityF.getFieldFB()).isEqualTo(DEFAULT_FIELD_FB);
    }

    @Test
    @Transactional
    public void createEntityFWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityFRepository.findAll().size();

        // Create the EntityF with an existing ID
        entityF.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityFMockMvc.perform(post("/api/entity-fs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityF)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntityF> entityFList = entityFRepository.findAll();
        assertThat(entityFList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityFS() throws Exception {
        // Initialize the database
        entityFRepository.saveAndFlush(entityF);

        // Get all the entityFList
        restEntityFMockMvc.perform(get("/api/entity-fs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityF.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldFA").value(hasItem(DEFAULT_FIELD_FA.toString())))
            .andExpect(jsonPath("$.[*].fieldFB").value(hasItem(DEFAULT_FIELD_FB)));
    }

    @Test
    @Transactional
    public void getEntityF() throws Exception {
        // Initialize the database
        entityFRepository.saveAndFlush(entityF);

        // Get the entityF
        restEntityFMockMvc.perform(get("/api/entity-fs/{id}", entityF.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityF.getId().intValue()))
            .andExpect(jsonPath("$.fieldFA").value(DEFAULT_FIELD_FA.toString()))
            .andExpect(jsonPath("$.fieldFB").value(DEFAULT_FIELD_FB));
    }

    @Test
    @Transactional
    public void getNonExistingEntityF() throws Exception {
        // Get the entityF
        restEntityFMockMvc.perform(get("/api/entity-fs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityF() throws Exception {
        // Initialize the database
        entityFRepository.saveAndFlush(entityF);
        int databaseSizeBeforeUpdate = entityFRepository.findAll().size();

        // Update the entityF
        EntityF updatedEntityF = entityFRepository.findOne(entityF.getId());
        updatedEntityF
            .fieldFA(UPDATED_FIELD_FA)
            .fieldFB(UPDATED_FIELD_FB);

        restEntityFMockMvc.perform(put("/api/entity-fs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityF)))
            .andExpect(status().isOk());

        // Validate the EntityF in the database
        List<EntityF> entityFList = entityFRepository.findAll();
        assertThat(entityFList).hasSize(databaseSizeBeforeUpdate);
        EntityF testEntityF = entityFList.get(entityFList.size() - 1);
        assertThat(testEntityF.getFieldFA()).isEqualTo(UPDATED_FIELD_FA);
        assertThat(testEntityF.getFieldFB()).isEqualTo(UPDATED_FIELD_FB);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityF() throws Exception {
        int databaseSizeBeforeUpdate = entityFRepository.findAll().size();

        // Create the EntityF

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityFMockMvc.perform(put("/api/entity-fs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityF)))
            .andExpect(status().isCreated());

        // Validate the EntityF in the database
        List<EntityF> entityFList = entityFRepository.findAll();
        assertThat(entityFList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntityF() throws Exception {
        // Initialize the database
        entityFRepository.saveAndFlush(entityF);
        int databaseSizeBeforeDelete = entityFRepository.findAll().size();

        // Get the entityF
        restEntityFMockMvc.perform(delete("/api/entity-fs/{id}", entityF.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityF> entityFList = entityFRepository.findAll();
        assertThat(entityFList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityF.class);
        EntityF entityF1 = new EntityF();
        entityF1.setId(1L);
        EntityF entityF2 = new EntityF();
        entityF2.setId(entityF1.getId());
        assertThat(entityF1).isEqualTo(entityF2);
        entityF2.setId(2L);
        assertThat(entityF1).isNotEqualTo(entityF2);
        entityF1.setId(null);
        assertThat(entityF1).isNotEqualTo(entityF2);
    }
}
