package com.dbh.dbh.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.dbh.dbh.domain.EntityD;

import com.dbh.dbh.repository.EntityDRepository;
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
 * REST controller for managing EntityD.
 */
@RestController
@RequestMapping("/api")
public class EntityDResource {

    private final Logger log = LoggerFactory.getLogger(EntityDResource.class);

    private static final String ENTITY_NAME = "entityD";

    private final EntityDRepository entityDRepository;

    public EntityDResource(EntityDRepository entityDRepository) {
        this.entityDRepository = entityDRepository;
    }

    /**
     * POST  /entity-ds : Create a new entityD.
     *
     * @param entityD the entityD to create
     * @return the ResponseEntity with status 201 (Created) and with body the new entityD, or with status 400 (Bad Request) if the entityD has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/entity-ds")
    @Timed
    public ResponseEntity<EntityD> createEntityD(@RequestBody EntityD entityD) throws URISyntaxException {
        log.debug("REST request to save EntityD : {}", entityD);
        if (entityD.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entityD cannot already have an ID")).body(null);
        }
        EntityD result = entityDRepository.save(entityD);
        return ResponseEntity.created(new URI("/api/entity-ds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /entity-ds : Updates an existing entityD.
     *
     * @param entityD the entityD to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated entityD,
     * or with status 400 (Bad Request) if the entityD is not valid,
     * or with status 500 (Internal Server Error) if the entityD couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/entity-ds")
    @Timed
    public ResponseEntity<EntityD> updateEntityD(@RequestBody EntityD entityD) throws URISyntaxException {
        log.debug("REST request to update EntityD : {}", entityD);
        if (entityD.getId() == null) {
            return createEntityD(entityD);
        }
        EntityD result = entityDRepository.save(entityD);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, entityD.getId().toString()))
            .body(result);
    }

    /**
     * GET  /entity-ds : get all the entityDS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of entityDS in body
     */
    @GetMapping("/entity-ds")
    @Timed
    public List<EntityD> getAllEntityDS() {
        log.debug("REST request to get all EntityDS");
        return entityDRepository.findAll();
    }

    /**
     * GET  /entity-ds/:id : get the "id" entityD.
     *
     * @param id the id of the entityD to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the entityD, or with status 404 (Not Found)
     */
    @GetMapping("/entity-ds/{id}")
    @Timed
    public ResponseEntity<EntityD> getEntityD(@PathVariable Long id) {
        log.debug("REST request to get EntityD : {}", id);
        EntityD entityD = entityDRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(entityD));
    }

    /**
     * DELETE  /entity-ds/:id : delete the "id" entityD.
     *
     * @param id the id of the entityD to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/entity-ds/{id}")
    @Timed
    public ResponseEntity<Void> deleteEntityD(@PathVariable Long id) {
        log.debug("REST request to delete EntityD : {}", id);
        entityDRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
