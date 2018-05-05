package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityF;

import com.dbh.dbh.repository.EntityFRepository;
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
 * REST controller for managing EntityF.
 */
@RestController
@RequestMapping("/api")
public class EntityFResource {

    private final Logger log = LoggerFactory.getLogger(EntityFResource.class);

    private static final String ENTITY_NAME = "entityF";

    private final EntityFRepository entityFRepository;

    public EntityFResource(EntityFRepository entityFRepository) {
        this.entityFRepository = entityFRepository;
    }

    /**
     * POST  /entity-fs : Create a new entityF.
     *
     * @param entityF the entityF to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityF, or with status 400 (Bad Request) if the entityF has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-fs")
    @Timed
    public ResponseEntity<EntityF> createEntityF(@RequestBody EntityF entityF) throws URISyntaxException {
        log.debug("REST request to save EntityF : {}", entityF);
        if (entityF.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityF cannot already have an ID")).body(null);
        }
        EntityF result = entityFRepository.save(entityF);
        return ResponseEntity.created(new URI("/api/entity-fs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-fs : Updates an existing entityF.
     *
     * @param entityF the entityF to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityF,
     * or with status 400 (Bad Request) if the entityF is not valid,
     * or with status 500 (Internal Server Error) if the entityF couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-fs")
    @Timed
    public ResponseEntity<EntityF> updateEntityF(@RequestBody EntityF entityF) throws URISyntaxException {
        log.debug("REST request to update EntityF : {}", entityF);
        if (entityF.getId() == null) {
            return createEntityF(entityF);
        }
        EntityF result = entityFRepository.save(entityF);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityF.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-fs : get all the entityFS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityFS in body
     */
    @GetMapping("/entity-fs")
    @Timed
    public List<EntityF> getAllEntityFS() {
        log.debug("REST request to get all EntityFS");
        return entityFRepository.findAll();
    }

    /**
     * GET  /entity-fs/:id : get the "id" entityF.
     *
     * @param id the id of the entityF to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityF, or with status 404 (Not Found)
     */
    @GetMapping("/entity-fs/{id}")
    @Timed
    public ResponseEntity<EntityF> getEntityF(@PathVariable Long id) {
        log.debug("REST request to get EntityF : {}", id);
        EntityF entityF = entityFRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityF));
    }

    /**
     * DELETE  /entity-fs/:id : delete the "id" entityF.
     *
     * @param id the id of the entityF to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-fs/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityF(@PathVariable Long id) {
        log.debug("REST request to delete EntityF : {}", id);
        entityFRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
