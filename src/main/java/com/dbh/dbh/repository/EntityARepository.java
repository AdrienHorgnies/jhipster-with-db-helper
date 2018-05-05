package com.dbh.dbh.repository;

import com.dbh.dbh.domain.EntityA;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the EntityA entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityARepository extends JpaRepository<EntityA,Long> {
    
    @Query("select distinct table_a from EntityA table_a left join fetch table_a.entityDS left join fetch table_a.entityES")
    List<EntityA> findAllWithEagerRelationships();

    @Query("select table_a from EntityA table_a left join fetch table_a.entityDS left join fetch table_a.entityES where table_a.id =:id")
    EntityA findOneWithEagerRelationships(@Param("id") Long id);
    
}
