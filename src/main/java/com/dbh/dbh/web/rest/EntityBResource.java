package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityB;

import com.dbh.dbh.repository.EntityBRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing EntityB.
 */
@RestController
@RequestMapping("/api")
public class EntityBResource {

    private final Logger log = LoggerFactory.getLogger(EntityBResource.class);

    private static final String ENTITY_NAME = "entityB";

    private final EntityBRepository entityBRepository;

    public EntityBResource(EntityBRepository entityBRepository) {
        this.entityBRepository = entityBRepository;
    }

    /**
     * POST  /entity-bs : Create a new entityB.
     *
     * @param entityB the entityB to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityB, or with status 400 (Bad Request) if the entityB has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-bs")
    @Timed
    public ResponseEntity<EntityB> createEntityB(@RequestBody EntityB entityB) throws URISyntaxException {
        log.debug("REST request to save EntityB : {}", entityB);
        if (entityB.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityB cannot already have an ID")).body(null);
        }
        EntityB result = entityBRepository.save(entityB);
        return ResponseEntity.created(new URI("/api/entity-bs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-bs : Updates an existing entityB.
     *
     * @param entityB the entityB to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityB,
     * or with status 400 (Bad Request) if the entityB is not valid,
     * or with status 500 (Internal Server Error) if the entityB couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-bs")
    @Timed
    public ResponseEntity<EntityB> updateEntityB(@RequestBody EntityB entityB) throws URISyntaxException {
        log.debug("REST request to update EntityB : {}", entityB);
        if (entityB.getId() == null) {
            return createEntityB(entityB);
        }
        EntityB result = entityBRepository.save(entityB);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityB.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-bs : get all the entityBS.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of entityBS in body
     */
    @GetMapping("/entity-bs")
    @Timed
    public List<EntityB> getAllEntityBS(@RequestParam(required = false) String filter) {
        if ("entitya-is-null".equals(filter)) {
            log.debug("REST request to get all EntityBs where entityA is null");
            return StreamSupport
                .stream(entityBRepository.findAll().spliterator(), false)
                .filter(entityB -> entityB.getEntityA() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all EntityBS");
        return entityBRepository.findAll();
    }

    /**
     * GET  /entity-bs/:id : get the "id" entityB.
     *
     * @param id the id of the entityB to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityB, or with status 404 (Not Found)
     */
    @GetMapping("/entity-bs/{id}")
    @Timed
    public ResponseEntity<EntityB> getEntityB(@PathVariable Long id) {
        log.debug("REST request to get EntityB : {}", id);
        EntityB entityB = entityBRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityB));
    }

    /**
     * DELETE  /entity-bs/:id : delete the "id" entityB.
     *
     * @param id the id of the entityB to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-bs/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityB(@PathVariable Long id) {
        log.debug("REST request to delete EntityB : {}", id);
        entityBRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
