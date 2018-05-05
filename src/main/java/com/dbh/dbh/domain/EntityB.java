package com.dbh.dbh.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EntityB.
 */
@Entity
@Table(name = "table_b")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_ba")
    private String fieldBA;

    @Column(name = "field_bb")
    private Integer fieldBB;

    @OneToOne(mappedBy = "entityB")
    @JsonIgnore
    private EntityA entityA;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldBA() {
        return fieldBA;
    }

    public EntityB fieldBA(String fieldBA) {
        this.fieldBA = fieldBA;
        return this;
    }

    public void setFieldBA(String fieldBA) {
        this.fieldBA = fieldBA;
    }

    public Integer getFieldBB() {
        return fieldBB;
    }

    public EntityB fieldBB(Integer fieldBB) {
        this.fieldBB = fieldBB;
        return this;
    }

    public void setFieldBB(Integer fieldBB) {
        this.fieldBB = fieldBB;
    }

    public EntityA getEntityA() {
        return entityA;
    }

    public EntityB entityA(EntityA entityA) {
        this.entityA = entityA;
        return this;
    }

    public void setEntityA(EntityA entityA) {
        this.entityA = entityA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityB entityB = (EntityB) o;
        if (entityB.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityB.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityB{" +
            "id=" + getId() +
            ", fieldBA='" + getFieldBA() + "'" +
            ", fieldBB='" + getFieldBB() + "'" +
            "}";
    }
}
