package com.dbh.dbh.web.rest;

import com.dbh.dbh.DbhTravisTestCaseApp;

import com.dbh.dbh.domain.EntityC;
import com.dbh.dbh.repository.EntityCRepository;
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
 * Test class for the EntityCResource REST controller.
 *
 * @see EntityCResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbhTravisTestCaseApp.class)
public class EntityCResourceIntTest {

    private static final String DEFAULT_FIELD_CA = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_CA = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_CB = 1;
    private static final Integer UPDATED_FIELD_CB = 2;

    @Autowired
    private EntityCRepository entityCRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityCMockMvc;

    private EntityC entityC;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityCResource entityCResource = new EntityCResource(entityCRepository);
        this.restEntityCMockMvc = MockMvcBuilders.standaloneSetup(entityCResource)
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
    public static EntityC createEntity(EntityManager em) {
        EntityC entityC = new EntityC()
            .fieldCA(DEFAULT_FIELD_CA)
            .fieldCB(DEFAULT_FIELD_CB);
        return entityC;
    }

    @Before
    public void initTest() {
        entityC = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityC() throws Exception {
        int databaseSizeBeforeCreate = entityCRepository.findAll().size();

        // Create the EntityC
        restEntityCMockMvc.perform(post("/api/entity-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityC)))
            .andExpect(status().isCreated());

        // Validate the EntityC in the database
        List<EntityC> entityCList = entityCRepository.findAll();
        assertThat(entityCList).hasSize(databaseSizeBeforeCreate + 1);
        EntityC testEntityC = entityCList.get(entityCList.size() - 1);
        assertThat(testEntityC.getFieldCA()).isEqualTo(DEFAULT_FIELD_CA);
        assertThat(testEntityC.getFieldCB()).isEqualTo(DEFAULT_FIELD_CB);
    }

    @Test
    @Transactional
    public void createEntityCWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityCRepository.findAll().size();

        // Create the EntityC with an existing ID
        entityC.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityCMockMvc.perform(post("/api/entity-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityC)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntityC> entityCList = entityCRepository.findAll();
        assertThat(entityCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityCS() throws Exception {
        // Initialize the database
        entityCRepository.saveAndFlush(entityC);

        // Get all the entityCList
        restEntityCMockMvc.perform(get("/api/entity-cs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityC.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldCA").value(hasItem(DEFAULT_FIELD_CA.toString())))
            .andExpect(jsonPath("$.[*].fieldCB").value(hasItem(DEFAULT_FIELD_CB)));
    }

    @Test
    @Transactional
    public void getEntityC() throws Exception {
        // Initialize the database
        entityCRepository.saveAndFlush(entityC);

        // Get the entityC
        restEntityCMockMvc.perform(get("/api/entity-cs/{id}", entityC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityC.getId().intValue()))
            .andExpect(jsonPath("$.fieldCA").value(DEFAULT_FIELD_CA.toString()))
            .andExpect(jsonPath("$.fieldCB").value(DEFAULT_FIELD_CB));
    }

    @Test
    @Transactional
    public void getNonExistingEntityC() throws Exception {
        // Get the entityC
        restEntityCMockMvc.perform(get("/api/entity-cs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityC() throws Exception {
        // Initialize the database
        entityCRepository.saveAndFlush(entityC);
        int databaseSizeBeforeUpdate = entityCRepository.findAll().size();

        // Update the entityC
        EntityC updatedEntityC = entityCRepository.findOne(entityC.getId());
        updatedEntityC
            .fieldCA(UPDATED_FIELD_CA)
            .fieldCB(UPDATED_FIELD_CB);

        restEntityCMockMvc.perform(put("/api/entity-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityC)))
            .andExpect(status().isOk());

        // Validate the EntityC in the database
        List<EntityC> entityCList = entityCRepository.findAll();
        assertThat(entityCList).hasSize(databaseSizeBeforeUpdate);
        EntityC testEntityC = entityCList.get(entityCList.size() - 1);
        assertThat(testEntityC.getFieldCA()).isEqualTo(UPDATED_FIELD_CA);
        assertThat(testEntityC.getFieldCB()).isEqualTo(UPDATED_FIELD_CB);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityC() throws Exception {
        int databaseSizeBeforeUpdate = entityCRepository.findAll().size();

        // Create the EntityC

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityCMockMvc.perform(put("/api/entity-cs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityC)))
            .andExpect(status().isCreated());

        // Validate the EntityC in the database
        List<EntityC> entityCList = entityCRepository.findAll();
        assertThat(entityCList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntityC() throws Exception {
        // Initialize the database
        entityCRepository.saveAndFlush(entityC);
        int databaseSizeBeforeDelete = entityCRepository.findAll().size();

        // Get the entityC
        restEntityCMockMvc.perform(delete("/api/entity-cs/{id}", entityC.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityC> entityCList = entityCRepository.findAll();
        assertThat(entityCList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityC.class);
        EntityC entityC1 = new EntityC();
        entityC1.setId(1L);
        EntityC entityC2 = new EntityC();
        entityC2.setId(entityC1.getId());
        assertThat(entityC1).isEqualTo(entityC2);
        entityC2.setId(2L);
        assertThat(entityC1).isNotEqualTo(entityC2);
        entityC1.setId(null);
        assertThat(entityC1).isNotEqualTo(entityC2);
    }
}
