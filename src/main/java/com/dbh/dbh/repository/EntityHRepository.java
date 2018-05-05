package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityH;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the EntityH entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityHRepository extends JpaRepository<EntityH,Long> {
    
    @Query("select distinct table_h from EntityH table_h left join fetch table_h.entityAS")
    List<EntityH> findAllWithEagerRelationships();

    @Query("select table_h from EntityH table_h left join fetch table_h.entityAS where table_h.id =:id")
    EntityH findOneWithEagerRelationships(@Param("id") Long id);
    
}
