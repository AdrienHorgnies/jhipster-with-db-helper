package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityE;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EntityE entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityERepository extends JpaRepository<EntityE,Long> {
    
}
