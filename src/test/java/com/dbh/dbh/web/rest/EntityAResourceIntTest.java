package com.dbh.dbh.web.rest;

import com.dbh.dbh.DbhTravisTestCaseApp;

import com.dbh.dbh.domain.EntityA;
import com.dbh.dbh.repository.EntityARepository;
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
 * Test class for the EntityAResource REST controller.
 *
 * @see EntityAResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbhTravisTestCaseApp.class)
public class EntityAResourceIntTest {

    private static final String DEFAULT_FIELD_AA = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_AA = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_AB = 1;
    private static final Integer UPDATED_FIELD_AB = 2;

    @Autowired
    private EntityARepository entityARepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityAMockMvc;

    private EntityA entityA;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityAResource entityAResource = new EntityAResource(entityARepository);
        this.restEntityAMockMvc = MockMvcBuilders.standaloneSetup(entityAResource)
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
    public static EntityA createEntity(EntityManager em) {
        EntityA entityA = new EntityA()
            .fieldAA(DEFAULT_FIELD_AA)
            .fieldAB(DEFAULT_FIELD_AB);
        return entityA;
    }

    @Before
    public void initTest() {
        entityA = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityA() throws Exception {
        int databaseSizeBeforeCreate = entityARepository.findAll().size();

        // Create the EntityA
        restEntityAMockMvc.perform(post("/api/entity-as")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityA)))
            .andExpect(status().isCreated());

        // Validate the EntityA in the database
        List<EntityA> entityAList = entityARepository.findAll();
        assertThat(entityAList).hasSize(databaseSizeBeforeCreate + 1);
        EntityA testEntityA = entityAList.get(entityAList.size() - 1);
        assertThat(testEntityA.getFieldAA()).isEqualTo(DEFAULT_FIELD_AA);
        assertThat(testEntityA.getFieldAB()).isEqualTo(DEFAULT_FIELD_AB);
    }

    @Test
    @Transactional
    public void createEntityAWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityARepository.findAll().size();

        // Create the EntityA with an existing ID
        entityA.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityAMockMvc.perform(post("/api/entity-as")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityA)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntityA> entityAList = entityARepository.findAll();
        assertThat(entityAList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityAS() throws Exception {
        // Initialize the database
        entityARepository.saveAndFlush(entityA);

        // Get all the entityAList
        restEntityAMockMvc.perform(get("/api/entity-as?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityA.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldAA").value(hasItem(DEFAULT_FIELD_AA.toString())))
            .andExpect(jsonPath("$.[*].fieldAB").value(hasItem(DEFAULT_FIELD_AB)));
    }

    @Test
    @Transactional
    public void getEntityA() throws Exception {
        // Initialize the database
        entityARepository.saveAndFlush(entityA);

        // Get the entityA
        restEntityAMockMvc.perform(get("/api/entity-as/{id}", entityA.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityA.getId().intValue()))
            .andExpect(jsonPath("$.fieldAA").value(DEFAULT_FIELD_AA.toString()))
            .andExpect(jsonPath("$.fieldAB").value(DEFAULT_FIELD_AB));
    }

    @Test
    @Transactional
    public void getNonExistingEntityA() throws Exception {
        // Get the entityA
        restEntityAMockMvc.perform(get("/api/entity-as/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityA() throws Exception {
        // Initialize the database
        entityARepository.saveAndFlush(entityA);
        int databaseSizeBeforeUpdate = entityARepository.findAll().size();

        // Update the entityA
        EntityA updatedEntityA = entityARepository.findOne(entityA.getId());
        updatedEntityA
            .fieldAA(UPDATED_FIELD_AA)
            .fieldAB(UPDATED_FIELD_AB);

        restEntityAMockMvc.perform(put("/api/entity-as")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityA)))
            .andExpect(status().isOk());

        // Validate the EntityA in the database
        List<EntityA> entityAList = entityARepository.findAll();
        assertThat(entityAList).hasSize(databaseSizeBeforeUpdate);
        EntityA testEntityA = entityAList.get(entityAList.size() - 1);
        assertThat(testEntityA.getFieldAA()).isEqualTo(UPDATED_FIELD_AA);
        assertThat(testEntityA.getFieldAB()).isEqualTo(UPDATED_FIELD_AB);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityA() throws Exception {
        int databaseSizeBeforeUpdate = entityARepository.findAll().size();

        // Create the EntityA

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityAMockMvc.perform(put("/api/entity-as")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityA)))
            .andExpect(status().isCreated());

        // Validate the EntityA in the database
        List<EntityA> entityAList = entityARepository.findAll();
        assertThat(entityAList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntityA() throws Exception {
        // Initialize the database
        entityARepository.saveAndFlush(entityA);
        int databaseSizeBeforeDelete = entityARepository.findAll().size();

        // Get the entityA
        restEntityAMockMvc.perform(delete("/api/entity-as/{id}", entityA.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityA> entityAList = entityARepository.findAll();
        assertThat(entityAList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityA.class);
        EntityA entityA1 = new EntityA();
        entityA1.setId(1L);
        EntityA entityA2 = new EntityA();
        entityA2.setId(entityA1.getId());
        assertThat(entityA1).isEqualTo(entityA2);
        entityA2.setId(2L);
        assertThat(entityA1).isNotEqualTo(entityA2);
        entityA1.setId(null);
        assertThat(entityA1).isNotEqualTo(entityA2);
    }
}
