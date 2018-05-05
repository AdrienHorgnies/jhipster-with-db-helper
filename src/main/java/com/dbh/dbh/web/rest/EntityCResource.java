package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityC;

import com.dbh.dbh.repository.EntityCRepository;
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
 * REST controller for managing EntityC.
 */
@RestController
@RequestMapping("/api")
public class EntityCResource {

    private final Logger log = LoggerFactory.getLogger(EntityCResource.class);

    private static final String ENTITY_NAME = "entityC";

    private final EntityCRepository entityCRepository;

    public EntityCResource(EntityCRepository entityCRepository) {
        this.entityCRepository = entityCRepository;
    }

    /**
     * POST  /entity-cs : Create a new entityC.
     *
     * @param entityC the entityC to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityC, or with status 400 (Bad Request) if the entityC has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-cs")
    @Timed
    public ResponseEntity<EntityC> createEntityC(@RequestBody EntityC entityC) throws URISyntaxException {
        log.debug("REST request to save EntityC : {}", entityC);
        if (entityC.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityC cannot already have an ID")).body(null);
        }
        EntityC result = entityCRepository.save(entityC);
        return ResponseEntity.created(new URI("/api/entity-cs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-cs : Updates an existing entityC.
     *
     * @param entityC the entityC to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityC,
     * or with status 400 (Bad Request) if the entityC is not valid,
     * or with status 500 (Internal Server Error) if the entityC couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-cs")
    @Timed
    public ResponseEntity<EntityC> updateEntityC(@RequestBody EntityC entityC) throws URISyntaxException {
        log.debug("REST request to update EntityC : {}", entityC);
        if (entityC.getId() == null) {
            return createEntityC(entityC);
        }
        EntityC result = entityCRepository.save(entityC);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityC.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-cs : get all the entityCS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityCS in body
     */
    @GetMapping("/entity-cs")
    @Timed
    public List<EntityC> getAllEntityCS() {
        log.debug("REST request to get all EntityCS");
        return entityCRepository.findAll();
    }

    /**
     * GET  /entity-cs/:id : get the "id" entityC.
     *
     * @param id the id of the entityC to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityC, or with status 404 (Not Found)
     */
    @GetMapping("/entity-cs/{id}")
    @Timed
    public ResponseEntity<EntityC> getEntityC(@PathVariable Long id) {
        log.debug("REST request to get EntityC : {}", id);
        EntityC entityC = entityCRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityC));
    }

    /**
     * DELETE  /entity-cs/:id : delete the "id" entityC.
     *
     * @param id the id of the entityC to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-cs/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityC(@PathVariable Long id) {
        log.debug("REST request to delete EntityC : {}", id);
        entityCRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
