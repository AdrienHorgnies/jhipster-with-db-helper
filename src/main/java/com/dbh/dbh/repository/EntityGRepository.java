package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityG;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EntityG entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityGRepository extends JpaRepository<EntityG,Long> {
    
}
