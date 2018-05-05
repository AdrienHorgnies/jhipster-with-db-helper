package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityF;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EntityF entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityFRepository extends JpaRepository<EntityF,Long> {
    
}
