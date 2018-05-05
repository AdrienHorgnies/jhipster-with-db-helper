package com.dbh.dbh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EntityD.
 */
@Entity
@Table(name = "table_d")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityD implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_d_id")
    private Long id;

    @Column(name = "column_da")
    private String fieldDA;

    @Column(name = "column_db")
    private Integer fieldDB;

    @ManyToMany(mappedBy = "entityDS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntityA> entityAS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldDA() {
        return fieldDA;
    }

    public EntityD fieldDA(String fieldDA) {
        this.fieldDA = fieldDA;
        return this;
    }

    public void setFieldDA(String fieldDA) {
        this.fieldDA = fieldDA;
    }

    public Integer getFieldDB() {
        return fieldDB;
    }

    public EntityD fieldDB(Integer fieldDB) {
        this.fieldDB = fieldDB;
        return this;
    }

    public void setFieldDB(Integer fieldDB) {
        this.fieldDB = fieldDB;
    }

    public Set<EntityA> getEntityAS() {
        return entityAS;
    }

    public EntityD entityAS(Set<EntityA> entityAS) {
        this.entityAS = entityAS;
        return this;
    }

    public EntityD addEntityA(EntityA entityA) {
        this.entityAS.add(entityA);
        entityA.getEntityDS().add(this);
        return this;
    }

    public EntityD removeEntityA(EntityA entityA) {
        this.entityAS.remove(entityA);
        entityA.getEntityDS().remove(this);
        return this;
    }

    public void setEntityAS(Set<EntityA> entityAS) {
        this.entityAS = entityAS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityD entityD = (EntityD) o;
        if (entityD.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityD.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityD{" +
            "id=" + getId() +
            ", fieldDA='" + getFieldDA() + "'" +
            ", fieldDB='" + getFieldDB() + "'" +
            "}";
    }
}
