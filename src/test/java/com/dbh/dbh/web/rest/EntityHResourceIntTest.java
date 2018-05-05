package com.dbh.dbh.web.rest;

import com.dbh.dbh.DbhTravisTestCaseApp;

import com.dbh.dbh.domain.EntityH;
import com.dbh.dbh.repository.EntityHRepository;
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
 * Test class for the EntityHResource REST controller.
 *
 * @see EntityHResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DbhTravisTestCaseApp.class)
public class EntityHResourceIntTest {

    private static final String DEFAULT_FIELD_HA = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_HA = "BBBBBBBBBB";

    private static final Integer DEFAULT_FIELD_HB = 1;
    private static final Integer UPDATED_FIELD_HB = 2;

    @Autowired
    private EntityHRepository entityHRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntityHMockMvc;

    private EntityH entityH;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EntityHResource entityHResource = new EntityHResource(entityHRepository);
        this.restEntityHMockMvc = MockMvcBuilders.standaloneSetup(entityHResource)
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
    public static EntityH createEntity(EntityManager em) {
        EntityH entityH = new EntityH()
            .fieldHA(DEFAULT_FIELD_HA)
            .fieldHB(DEFAULT_FIELD_HB);
        return entityH;
    }

    @Before
    public void initTest() {
        entityH = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntityH() throws Exception {
        int databaseSizeBeforeCreate = entityHRepository.findAll().size();

        // Create the EntityH
        restEntityHMockMvc.perform(post("/api/entity-hs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityH)))
            .andExpect(status().isCreated());

        // Validate the EntityH in the database
        List<EntityH> entityHList = entityHRepository.findAll();
        assertThat(entityHList).hasSize(databaseSizeBeforeCreate + 1);
        EntityH testEntityH = entityHList.get(entityHList.size() - 1);
        assertThat(testEntityH.getFieldHA()).isEqualTo(DEFAULT_FIELD_HA);
        assertThat(testEntityH.getFieldHB()).isEqualTo(DEFAULT_FIELD_HB);
    }

    @Test
    @Transactional
    public void createEntityHWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entityHRepository.findAll().size();

        // Create the EntityH with an existing ID
        entityH.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntityHMockMvc.perform(post("/api/entity-hs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityH)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<EntityH> entityHList = entityHRepository.findAll();
        assertThat(entityHList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntityHS() throws Exception {
        // Initialize the database
        entityHRepository.saveAndFlush(entityH);

        // Get all the entityHList
        restEntityHMockMvc.perform(get("/api/entity-hs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entityH.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldHA").value(hasItem(DEFAULT_FIELD_HA.toString())))
            .andExpect(jsonPath("$.[*].fieldHB").value(hasItem(DEFAULT_FIELD_HB)));
    }

    @Test
    @Transactional
    public void getEntityH() throws Exception {
        // Initialize the database
        entityHRepository.saveAndFlush(entityH);

        // Get the entityH
        restEntityHMockMvc.perform(get("/api/entity-hs/{id}", entityH.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entityH.getId().intValue()))
            .andExpect(jsonPath("$.fieldHA").value(DEFAULT_FIELD_HA.toString()))
            .andExpect(jsonPath("$.fieldHB").value(DEFAULT_FIELD_HB));
    }

    @Test
    @Transactional
    public void getNonExistingEntityH() throws Exception {
        // Get the entityH
        restEntityHMockMvc.perform(get("/api/entity-hs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntityH() throws Exception {
        // Initialize the database
        entityHRepository.saveAndFlush(entityH);
        int databaseSizeBeforeUpdate = entityHRepository.findAll().size();

        // Update the entityH
        EntityH updatedEntityH = entityHRepository.findOne(entityH.getId());
        updatedEntityH
            .fieldHA(UPDATED_FIELD_HA)
            .fieldHB(UPDATED_FIELD_HB);

        restEntityHMockMvc.perform(put("/api/entity-hs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntityH)))
            .andExpect(status().isOk());

        // Validate the EntityH in the database
        List<EntityH> entityHList = entityHRepository.findAll();
        assertThat(entityHList).hasSize(databaseSizeBeforeUpdate);
        EntityH testEntityH = entityHList.get(entityHList.size() - 1);
        assertThat(testEntityH.getFieldHA()).isEqualTo(UPDATED_FIELD_HA);
        assertThat(testEntityH.getFieldHB()).isEqualTo(UPDATED_FIELD_HB);
    }

    @Test
    @Transactional
    public void updateNonExistingEntityH() throws Exception {
        int databaseSizeBeforeUpdate = entityHRepository.findAll().size();

        // Create the EntityH

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEntityHMockMvc.perform(put("/api/entity-hs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entityH)))
            .andExpect(status().isCreated());

        // Validate the EntityH in the database
        List<EntityH> entityHList = entityHRepository.findAll();
        assertThat(entityHList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEntityH() throws Exception {
        // Initialize the database
        entityHRepository.saveAndFlush(entityH);
        int databaseSizeBeforeDelete = entityHRepository.findAll().size();

        // Get the entityH
        restEntityHMockMvc.perform(delete("/api/entity-hs/{id}", entityH.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<EntityH> entityHList = entityHRepository.findAll();
        assertThat(entityHList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntityH.class);
        EntityH entityH1 = new EntityH();
        entityH1.setId(1L);
        EntityH entityH2 = new EntityH();
        entityH2.setId(entityH1.getId());
        assertThat(entityH1).isEqualTo(entityH2);
        entityH2.setId(2L);
        assertThat(entityH1).isNotEqualTo(entityH2);
        entityH1.setId(null);
        assertThat(entityH1).isNotEqualTo(entityH2);
    }
}
