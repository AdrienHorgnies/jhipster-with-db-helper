package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityE;

import com.dbh.dbh.repository.EntityERepository;
import com.dbh.dbh.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing EntityE.
 */
@RestController
@RequestMapping("/api")
public class EntityEResource {

    private final Logger log = LoggerFactory.getLogger(EntityEResource.class);

    private static final String ENTITY_NAME = "entityE";

    private final EntityERepository entityERepository;

    public EntityEResource(EntityERepository entityERepository) {
        this.entityERepository = entityERepository;
    }

    /**
     * POST  /entity-es : Create a new entityE.
     *
     * @param entityE the entityE to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityE, or with status 400 (Bad Request) if the entityE has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-es")
    @Timed
    public ResponseEntity<EntityE> createEntityE(@RequestBody EntityE entityE) throws URISyntaxException {
        log.debug("REST request to save EntityE : {}", entityE);
        if (entityE.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityE cannot already have an ID")).body(null);
        }
        EntityE result = entityERepository.save(entityE);
        return ResponseEntity.created(new URI("/api/entity-es/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-es : Updates an existing entityE.
     *
     * @param entityE the entityE to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityE,
     * or with status 400 (Bad Request) if the entityE is not valid,
     * or with status 500 (Internal Server Error) if the entityE couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-es")
    @Timed
    public ResponseEntity<EntityE> updateEntityE(@RequestBody EntityE entityE) throws URISyntaxException {
        log.debug("REST request to update EntityE : {}", entityE);
        if (entityE.getId() == null) {
            return createEntityE(entityE);
        }
        EntityE result = entityERepository.save(entityE);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityE.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-es : get all the entityES.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityES in body
     */
    @GetMapping("/entity-es")
    @Timed
    public List<EntityE> getAllEntityES() {
        log.debug("REST request to get all EntityES");
        return entityERepository.findAll();
    }

    /**
     * GET  /entity-es/:id : get the "id" entityE.
     *
     * @param id the id of the entityE to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityE, or with status 404 (Not Found)
     */
    @GetMapping("/entity-es/{id}")
    @Timed
    public ResponseEntity<EntityE> getEntityE(@PathVariable Long id) {
        log.debug("REST request to get EntityE : {}", id);
        EntityE entityE = entityERepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityE));
    }

    /**
     * DELETE  /entity-es/:id : delete the "id" entityE.
     *
     * @param id the id of the entityE to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-es/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityE(@PathVariable Long id) {
        log.debug("REST request to delete EntityE : {}", id);
        entityERepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
