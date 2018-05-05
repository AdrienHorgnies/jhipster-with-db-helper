package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityD;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EntityD entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityDRepository extends JpaRepository<EntityD,Long> {
    
}
