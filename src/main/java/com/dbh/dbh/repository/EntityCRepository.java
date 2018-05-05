package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityC;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EntityC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityCRepository extends JpaRepository<EntityC,Long> {
    
}
