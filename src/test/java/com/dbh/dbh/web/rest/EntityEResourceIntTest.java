package com.dbh.dbh.web.rest;

import com.dbh.dbh.DbhTravisTestCaseApp;

import com.dbh.dbh.domain.EntityE;
import com.dbh.dbh.repository.EntityERepository;
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
 * Test class for the EntityEResource REST controller.
 *
 * @see EntityEResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbhTravisTestCaseApp.class)
public class EntityEResourceIntTest {

    private static final String DEFAULT_FIELD_EA = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_EA = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_EB = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_EB = "BBBBBBBBBB";

    @Autowired
    private EntityERepository entityERepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityEMockMvc;

    private EntityE entityE;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityEResource entityEResource = new EntityEResource(entityERepository);
        this.restEntityEMockMvc = MockMvcBuilders.standaloneSetup(entityEResource)
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
    public static EntityE createEntity(EntityManager em) {
        EntityE entityE = new EntityE()
            .fieldEA(DEFAULT_FIELD_EA)
            .fieldEB(DEFAULT_FIELD_EB);
        return entityE;
    }

    @Before
    public void initTest() {
        entityE = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityE() throws Exception {
        int databaseSizeBeforeCreate = entityERepository.findAll().size();

        // Create the EntityE
        restEntityEMockMvc.perform(post("/api/entity-es")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityE)))
            .andExpect(status().isCreated());

        // Validate the EntityE in the database
        List<EntityE> entityEList = entityERepository.findAll();
        assertThat(entityEList).hasSize(databaseSizeBeforeCreate + 1);
        EntityE testEntityE = entityEList.get(entityEList.size() - 1);
        assertThat(testEntityE.getFieldEA()).isEqualTo(DEFAULT_FIELD_EA);
        assertThat(testEntityE.getFieldEB()).isEqualTo(DEFAULT_FIELD_EB);
    }

    @Test
    @Transactional
    public void createEntityEWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityERepository.findAll().size();

        // Create the EntityE with an existing ID
        entityE.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityEMockMvc.perform(post("/api/entity-es")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityE)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntityE> entityEList = entityERepository.findAll();
        assertThat(entityEList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityES() throws Exception {
        // Initialize the database
        entityERepository.saveAndFlush(entityE);

        // Get all the entityEList
        restEntityEMockMvc.perform(get("/api/entity-es?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityE.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldEA").value(hasItem(DEFAULT_FIELD_EA.toString())))
            .andExpect(jsonPath("$.[*].fieldEB").value(hasItem(DEFAULT_FIELD_EB.toString())));
    }

    @Test
    @Transactional
    public void getEntityE() throws Exception {
        // Initialize the database
        entityERepository.saveAndFlush(entityE);

        // Get the entityE
        restEntityEMockMvc.perform(get("/api/entity-es/{id}", entityE.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityE.getId().intValue()))
            .andExpect(jsonPath("$.fieldEA").value(DEFAULT_FIELD_EA.toString()))
            .andExpect(jsonPath("$.fieldEB").value(DEFAULT_FIELD_EB.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEntityE() throws Exception {
        // Get the entityE
        restEntityEMockMvc.perform(get("/api/entity-es/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityE() throws Exception {
        // Initialize the database
        entityERepository.saveAndFlush(entityE);
        int databaseSizeBeforeUpdate = entityERepository.findAll().size();

        // Update the entityE
        EntityE updatedEntityE = entityERepository.findOne(entityE.getId());
        updatedEntityE
            .fieldEA(UPDATED_FIELD_EA)
            .fieldEB(UPDATED_FIELD_EB);

        restEntityEMockMvc.perform(put("/api/entity-es")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityE)))
            .andExpect(status().isOk());

        // Validate the EntityE in the database
        List<EntityE> entityEList = entityERepository.findAll();
        assertThat(entityEList).hasSize(databaseSizeBeforeUpdate);
        EntityE testEntityE = entityEList.get(entityEList.size() - 1);
        assertThat(testEntityE.getFieldEA()).isEqualTo(UPDATED_FIELD_EA);
        assertThat(testEntityE.getFieldEB()).isEqualTo(UPDATED_FIELD_EB);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityE() throws Exception {
        int databaseSizeBeforeUpdate = entityERepository.findAll().size();

        // Create the EntityE

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityEMockMvc.perform(put("/api/entity-es")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityE)))
            .andExpect(status().isCreated());

        // Validate the EntityE in the database
        List<EntityE> entityEList = entityERepository.findAll();
        assertThat(entityEList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntityE() throws Exception {
        // Initialize the database
        entityERepository.saveAndFlush(entityE);
        int databaseSizeBeforeDelete = entityERepository.findAll().size();

        // Get the entityE
        restEntityEMockMvc.perform(delete("/api/entity-es/{id}", entityE.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityE> entityEList = entityERepository.findAll();
        assertThat(entityEList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityE.class);
        EntityE entityE1 = new EntityE();
        entityE1.setId(1L);
        EntityE entityE2 = new EntityE();
        entityE2.setId(entityE1.getId());
        assertThat(entityE1).isEqualTo(entityE2);
        entityE2.setId(2L);
        assertThat(entityE1).isNotEqualTo(entityE2);
        entityE1.setId(null);
        assertThat(entityE1).isNotEqualTo(entityE2);
    }
}
