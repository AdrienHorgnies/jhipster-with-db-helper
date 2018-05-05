package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityH;

import com.dbh.dbh.repository.EntityHRepository;
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
 * REST controller for managing EntityH.
 */
@RestController
@RequestMapping("/api")
public class EntityHResource {

    private final Logger log = LoggerFactory.getLogger(EntityHResource.class);

    private static final String ENTITY_NAME = "entityH";

    private final EntityHRepository entityHRepository;

    public EntityHResource(EntityHRepository entityHRepository) {
        this.entityHRepository = entityHRepository;
    }

    /**
     * POST  /entity-hs : Create a new entityH.
     *
     * @param entityH the entityH to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityH, or with status 400 (Bad Request) if the entityH has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-hs")
    @Timed
    public ResponseEntity<EntityH> createEntityH(@RequestBody EntityH entityH) throws URISyntaxException {
        log.debug("REST request to save EntityH : {}", entityH);
        if (entityH.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityH cannot already have an ID")).body(null);
        }
        EntityH result = entityHRepository.save(entityH);
        return ResponseEntity.created(new URI("/api/entity-hs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-hs : Updates an existing entityH.
     *
     * @param entityH the entityH to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityH,
     * or with status 400 (Bad Request) if the entityH is not valid,
     * or with status 500 (Internal Server Error) if the entityH couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-hs")
    @Timed
    public ResponseEntity<EntityH> updateEntityH(@RequestBody EntityH entityH) throws URISyntaxException {
        log.debug("REST request to update EntityH : {}", entityH);
        if (entityH.getId() == null) {
            return createEntityH(entityH);
        }
        EntityH result = entityHRepository.save(entityH);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityH.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-hs : get all the entityHS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityHS in body
     */
    @GetMapping("/entity-hs")
    @Timed
    public List<EntityH> getAllEntityHS() {
        log.debug("REST request to get all EntityHS");
        return entityHRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /entity-hs/:id : get the "id" entityH.
     *
     * @param id the id of the entityH to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityH, or with status 404 (Not Found)
     */
    @GetMapping("/entity-hs/{id}")
    @Timed
    public ResponseEntity<EntityH> getEntityH(@PathVariable Long id) {
        log.debug("REST request to get EntityH : {}", id);
        EntityH entityH = entityHRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityH));
    }

    /**
     * DELETE  /entity-hs/:id : delete the "id" entityH.
     *
     * @param id the id of the entityH to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-hs/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityH(@PathVariable Long id) {
        log.debug("REST request to delete EntityH : {}", id);
        entityHRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
