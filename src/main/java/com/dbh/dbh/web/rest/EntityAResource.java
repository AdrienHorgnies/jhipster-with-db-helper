package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityA;

import com.dbh.dbh.repository.EntityARepository;
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
 * REST controller for managing EntityA.
 */
@RestController
@RequestMapping("/api")
public class EntityAResource {

    private final Logger log = LoggerFactory.getLogger(EntityAResource.class);

    private static final String ENTITY_NAME = "entityA";

    private final EntityARepository entityARepository;

    public EntityAResource(EntityARepository entityARepository) {
        this.entityARepository = entityARepository;
    }

    /**
     * POST  /entity-as : Create a new entityA.
     *
     * @param entityA the entityA to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityA, or with status 400 (Bad Request) if the entityA has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-as")
    @Timed
    public ResponseEntity<EntityA> createEntityA(@RequestBody EntityA entityA) throws URISyntaxException {
        log.debug("REST request to save EntityA : {}", entityA);
        if (entityA.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityA cannot already have an ID")).body(null);
        }
        EntityA result = entityARepository.save(entityA);
        return ResponseEntity.created(new URI("/api/entity-as/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-as : Updates an existing entityA.
     *
     * @param entityA the entityA to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityA,
     * or with status 400 (Bad Request) if the entityA is not valid,
     * or with status 500 (Internal Server Error) if the entityA couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-as")
    @Timed
    public ResponseEntity<EntityA> updateEntityA(@RequestBody EntityA entityA) throws URISyntaxException {
        log.debug("REST request to update EntityA : {}", entityA);
        if (entityA.getId() == null) {
            return createEntityA(entityA);
        }
        EntityA result = entityARepository.save(entityA);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityA.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-as : get all the entityAS.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of entityAS in body
     */
    @GetMapping("/entity-as")
    @Timed
    public List<EntityA> getAllEntityAS(@RequestParam(required = false) String filter) {
        if ("entityf-is-null".equals(filter)) {
            log.debug("REST request to get all EntityAs where entityF is null");
            return StreamSupport
                .stream(entityARepository.findAll().spliterator(), false)
                .filter(entityA -> entityA.getEntityF() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all EntityAS");
        return entityARepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /entity-as/:id : get the "id" entityA.
     *
     * @param id the id of the entityA to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityA, or with status 404 (Not Found)
     */
    @GetMapping("/entity-as/{id}")
    @Timed
    public ResponseEntity<EntityA> getEntityA(@PathVariable Long id) {
        log.debug("REST request to get EntityA : {}", id);
        EntityA entityA = entityARepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityA));
    }

    /**
     * DELETE  /entity-as/:id : delete the "id" entityA.
     *
     * @param id the id of the entityA to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-as/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityA(@PathVariable Long id) {
        log.debug("REST request to delete EntityA : {}", id);
        entityARepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
