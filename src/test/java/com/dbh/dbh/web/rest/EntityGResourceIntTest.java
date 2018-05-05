package com.dbh.dbh.web.rest;

import com.dbh.dbh.DbhTravisTestCaseApp;

import com.dbh.dbh.domain.EntityG;
import com.dbh.dbh.repository.EntityGRepository;
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
 * Test class for the EntityGResource REST controller.
 *
 * @see EntityGResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbhTravisTestCaseApp.class)
public class EntityGResourceIntTest {

    private static final String DEFAULT_FIELD_GA = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_GA = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_GB = 1;
    private static final Integer UPDATED_FIELD_GB = 2;

    @Autowired
    private EntityGRepository entityGRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityGMockMvc;

    private EntityG entityG;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityGResource entityGResource = new EntityGResource(entityGRepository);
        this.restEntityGMockMvc = MockMvcBuilders.standaloneSetup(entityGResource)
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
    public static EntityG createEntity(EntityManager em) {
        EntityG entityG = new EntityG()
            .fieldGA(DEFAULT_FIELD_GA)
            .fieldGB(DEFAULT_FIELD_GB);
        return entityG;
    }

    @Before
    public void initTest() {
        entityG = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityG() throws Exception {
        int databaseSizeBeforeCreate = entityGRepository.findAll().size();

        // Create the EntityG
        restEntityGMockMvc.perform(post("/api/entity-gs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityG)))
            .andExpect(status().isCreated());

        // Validate the EntityG in the database
        List<EntityG> entityGList = entityGRepository.findAll();
        assertThat(entityGList).hasSize(databaseSizeBeforeCreate + 1);
        EntityG testEntityG = entityGList.get(entityGList.size() - 1);
        assertThat(testEntityG.getFieldGA()).isEqualTo(DEFAULT_FIELD_GA);
        assertThat(testEntityG.getFieldGB()).isEqualTo(DEFAULT_FIELD_GB);
    }

    @Test
    @Transactional
    public void createEntityGWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityGRepository.findAll().size();

        // Create the EntityG with an existing ID
        entityG.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityGMockMvc.perform(post("/api/entity-gs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityG)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntityG> entityGList = entityGRepository.findAll();
        assertThat(entityGList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityGS() throws Exception {
        // Initialize the database
        entityGRepository.saveAndFlush(entityG);

        // Get all the entityGList
        restEntityGMockMvc.perform(get("/api/entity-gs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityG.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldGA").value(hasItem(DEFAULT_FIELD_GA.toString())))
            .andExpect(jsonPath("$.[*].fieldGB").value(hasItem(DEFAULT_FIELD_GB)));
    }

    @Test
    @Transactional
    public void getEntityG() throws Exception {
        // Initialize the database
        entityGRepository.saveAndFlush(entityG);

        // Get the entityG
        restEntityGMockMvc.perform(get("/api/entity-gs/{id}", entityG.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityG.getId().intValue()))
            .andExpect(jsonPath("$.fieldGA").value(DEFAULT_FIELD_GA.toString()))
            .andExpect(jsonPath("$.fieldGB").value(DEFAULT_FIELD_GB));
    }

    @Test
    @Transactional
    public void getNonExistingEntityG() throws Exception {
        // Get the entityG
        restEntityGMockMvc.perform(get("/api/entity-gs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityG() throws Exception {
        // Initialize the database
        entityGRepository.saveAndFlush(entityG);
        int databaseSizeBeforeUpdate = entityGRepository.findAll().size();

        // Update the entityG
        EntityG updatedEntityG = entityGRepository.findOne(entityG.getId());
        updatedEntityG
            .fieldGA(UPDATED_FIELD_GA)
            .fieldGB(UPDATED_FIELD_GB);

        restEntityGMockMvc.perform(put("/api/entity-gs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityG)))
            .andExpect(status().isOk());

        // Validate the EntityG in the database
        List<EntityG> entityGList = entityGRepository.findAll();
        assertThat(entityGList).hasSize(databaseSizeBeforeUpdate);
        EntityG testEntityG = entityGList.get(entityGList.size() - 1);
        assertThat(testEntityG.getFieldGA()).isEqualTo(UPDATED_FIELD_GA);
        assertThat(testEntityG.getFieldGB()).isEqualTo(UPDATED_FIELD_GB);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityG() throws Exception {
        int databaseSizeBeforeUpdate = entityGRepository.findAll().size();

        // Create the EntityG

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityGMockMvc.perform(put("/api/entity-gs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityG)))
            .andExpect(status().isCreated());

        // Validate the EntityG in the database
        List<EntityG> entityGList = entityGRepository.findAll();
        assertThat(entityGList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntityG() throws Exception {
        // Initialize the database
        entityGRepository.saveAndFlush(entityG);
        int databaseSizeBeforeDelete = entityGRepository.findAll().size();

        // Get the entityG
        restEntityGMockMvc.perform(delete("/api/entity-gs/{id}", entityG.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityG> entityGList = entityGRepository.findAll();
        assertThat(entityGList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityG.class);
        EntityG entityG1 = new EntityG();
        entityG1.setId(1L);
        EntityG entityG2 = new EntityG();
        entityG2.setId(entityG1.getId());
        assertThat(entityG1).isEqualTo(entityG2);
        entityG2.setId(2L);
        assertThat(entityG1).isNotEqualTo(entityG2);
        entityG1.setId(null);
        assertThat(entityG1).isNotEqualTo(entityG2);
    }
}
