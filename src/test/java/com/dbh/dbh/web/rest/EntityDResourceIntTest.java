package com.dbh.dbh.web.rest;

import com.dbh.dbh.DbhTravisTestCaseApp;

import com.dbh.dbh.domain.EntityD;
import com.dbh.dbh.repository.EntityDRepository;
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
 * Test class for the EntityDResource REST controller.
 *
 * @see EntityDResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbhTravisTestCaseApp.class)
public class EntityDResourceIntTest {

    private static final String DEFAULT_FIELD_DA = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_DA = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_DB = 1;
    private static final Integer UPDATED_FIELD_DB = 2;

    @Autowired
    private EntityDRepository entityDRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityDMockMvc;

    private EntityD entityD;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityDResource entityDResource = new EntityDResource(entityDRepository);
        this.restEntityDMockMvc = MockMvcBuilders.standaloneSetup(entityDResource)
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
    public static EntityD createEntity(EntityManager em) {
        EntityD entityD = new EntityD()
            .fieldDA(DEFAULT_FIELD_DA)
            .fieldDB(DEFAULT_FIELD_DB);
        return entityD;
    }

    @Before
    public void initTest() {
        entityD = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityD() throws Exception {
        int databaseSizeBeforeCreate = entityDRepository.findAll().size();

        // Create the EntityD
        restEntityDMockMvc.perform(post("/api/entity-ds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityD)))
            .andExpect(status().isCreated());

        // Validate the EntityD in the database
        List<EntityD> entityDList = entityDRepository.findAll();
        assertThat(entityDList).hasSize(databaseSizeBeforeCreate + 1);
        EntityD testEntityD = entityDList.get(entityDList.size() - 1);
        assertThat(testEntityD.getFieldDA()).isEqualTo(DEFAULT_FIELD_DA);
        assertThat(testEntityD.getFieldDB()).isEqualTo(DEFAULT_FIELD_DB);
    }

    @Test
    @Transactional
    public void createEntityDWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityDRepository.findAll().size();

        // Create the EntityD with an existing ID
        entityD.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityDMockMvc.perform(post("/api/entity-ds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityD)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntityD> entityDList = entityDRepository.findAll();
        assertThat(entityDList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityDS() throws Exception {
        // Initialize the database
        entityDRepository.saveAndFlush(entityD);

        // Get all the entityDList
        restEntityDMockMvc.perform(get("/api/entity-ds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityD.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldDA").value(hasItem(DEFAULT_FIELD_DA.toString())))
            .andExpect(jsonPath("$.[*].fieldDB").value(hasItem(DEFAULT_FIELD_DB)));
    }

    @Test
    @Transactional
    public void getEntityD() throws Exception {
        // Initialize the database
        entityDRepository.saveAndFlush(entityD);

        // Get the entityD
        restEntityDMockMvc.perform(get("/api/entity-ds/{id}", entityD.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityD.getId().intValue()))
            .andExpect(jsonPath("$.fieldDA").value(DEFAULT_FIELD_DA.toString()))
            .andExpect(jsonPath("$.fieldDB").value(DEFAULT_FIELD_DB));
    }

    @Test
    @Transactional
    public void getNonExistingEntityD() throws Exception {
        // Get the entityD
        restEntityDMockMvc.perform(get("/api/entity-ds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityD() throws Exception {
        // Initialize the database
        entityDRepository.saveAndFlush(entityD);
        int databaseSizeBeforeUpdate = entityDRepository.findAll().size();

        // Update the entityD
        EntityD updatedEntityD = entityDRepository.findOne(entityD.getId());
        updatedEntityD
            .fieldDA(UPDATED_FIELD_DA)
            .fieldDB(UPDATED_FIELD_DB);

        restEntityDMockMvc.perform(put("/api/entity-ds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityD)))
            .andExpect(status().isOk());

        // Validate the EntityD in the database
        List<EntityD> entityDList = entityDRepository.findAll();
        assertThat(entityDList).hasSize(databaseSizeBeforeUpdate);
        EntityD testEntityD = entityDList.get(entityDList.size() - 1);
        assertThat(testEntityD.getFieldDA()).isEqualTo(UPDATED_FIELD_DA);
        assertThat(testEntityD.getFieldDB()).isEqualTo(UPDATED_FIELD_DB);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityD() throws Exception {
        int databaseSizeBeforeUpdate = entityDRepository.findAll().size();

        // Create the EntityD

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityDMockMvc.perform(put("/api/entity-ds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityD)))
            .andExpect(status().isCreated());

        // Validate the EntityD in the database
        List<EntityD> entityDList = entityDRepository.findAll();
        assertThat(entityDList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntityD() throws Exception {
        // Initialize the database
        entityDRepository.saveAndFlush(entityD);
        int databaseSizeBeforeDelete = entityDRepository.findAll().size();

        // Get the entityD
        restEntityDMockMvc.perform(delete("/api/entity-ds/{id}", entityD.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityD> entityDList = entityDRepository.findAll();
        assertThat(entityDList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityD.class);
        EntityD entityD1 = new EntityD();
        entityD1.setId(1L);
        EntityD entityD2 = new EntityD();
        entityD2.setId(entityD1.getId());
        assertThat(entityD1).isEqualTo(entityD2);
        entityD2.setId(2L);
        assertThat(entityD1).isNotEqualTo(entityD2);
        entityD1.setId(null);
        assertThat(entityD1).isNotEqualTo(entityD2);
    }
}
