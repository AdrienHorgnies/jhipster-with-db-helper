package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityG;

import com.dbh.dbh.repository.EntityGRepository;
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
 * REST controller for managing EntityG.
 */
@RestController
@RequestMapping("/api")
public class EntityGResource {

    private final Logger log = LoggerFactory.getLogger(EntityGResource.class);

    private static final String ENTITY_NAME = "entityG";

    private final EntityGRepository entityGRepository;

    public EntityGResource(EntityGRepository entityGRepository) {
        this.entityGRepository = entityGRepository;
    }

    /**
     * POST  /entity-gs : Create a new entityG.
     *
     * @param entityG the entityG to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityG, or with status 400 (Bad Request) if the entityG has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-gs")
    @Timed
    public ResponseEntity<EntityG> createEntityG(@RequestBody EntityG entityG) throws URISyntaxException {
        log.debug("REST request to save EntityG : {}", entityG);
        if (entityG.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityG cannot already have an ID")).body(null);
        }
        EntityG result = entityGRepository.save(entityG);
        return ResponseEntity.created(new URI("/api/entity-gs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-gs : Updates an existing entityG.
     *
     * @param entityG the entityG to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityG,
     * or with status 400 (Bad Request) if the entityG is not valid,
     * or with status 500 (Internal Server Error) if the entityG couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-gs")
    @Timed
    public ResponseEntity<EntityG> updateEntityG(@RequestBody EntityG entityG) throws URISyntaxException {
        log.debug("REST request to update EntityG : {}", entityG);
        if (entityG.getId() == null) {
            return createEntityG(entityG);
        }
        EntityG result = entityGRepository.save(entityG);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityG.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-gs : get all the entityGS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityGS in body
     */
    @GetMapping("/entity-gs")
    @Timed
    public List<EntityG> getAllEntityGS() {
        log.debug("REST request to get all EntityGS");
        return entityGRepository.findAll();
    }

    /**
     * GET  /entity-gs/:id : get the "id" entityG.
     *
     * @param id the id of the entityG to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityG, or with status 404 (Not Found)
     */
    @GetMapping("/entity-gs/{id}")
    @Timed
    public ResponseEntity<EntityG> getEntityG(@PathVariable Long id) {
        log.debug("REST request to get EntityG : {}", id);
        EntityG entityG = entityGRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityG));
    }

    /**
     * DELETE  /entity-gs/:id : delete the "id" entityG.
     *
     * @param id the id of the entityG to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-gs/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityG(@PathVariable Long id) {
        log.debug("REST request to delete EntityG : {}", id);
        entityGRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
