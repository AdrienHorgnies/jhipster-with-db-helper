package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityB;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EntityB entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityBRepository extends JpaRepository<EntityB,Long> {
    
}
