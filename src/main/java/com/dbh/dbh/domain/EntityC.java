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
 * A EntityC.
 */
@Entity
@Table(name = "table_c")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_ca")
    private String fieldCA;

    @Column(name = "field_cb")
    private Integer fieldCB;

    @OneToMany(mappedBy = "entityC")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntityA> entityAS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldCA() {
        return fieldCA;
    }

    public EntityC fieldCA(String fieldCA) {
        this.fieldCA = fieldCA;
        return this;
    }

    public void setFieldCA(String fieldCA) {
        this.fieldCA = fieldCA;
    }

    public Integer getFieldCB() {
        return fieldCB;
    }

    public EntityC fieldCB(Integer fieldCB) {
        this.fieldCB = fieldCB;
        return this;
    }

    public void setFieldCB(Integer fieldCB) {
        this.fieldCB = fieldCB;
    }

    public Set<EntityA> getEntityAS() {
        return entityAS;
    }

    public EntityC entityAS(Set<EntityA> entityAS) {
        this.entityAS = entityAS;
        return this;
    }

    public EntityC addEntityA(EntityA entityA) {
        this.entityAS.add(entityA);
        entityA.setEntityC(this);
        return this;
    }

    public EntityC removeEntityA(EntityA entityA) {
        this.entityAS.remove(entityA);
        entityA.setEntityC(null);
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
        EntityC entityC = (EntityC) o;
        if (entityC.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityC.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityC{" +
            "id=" + getId() +
            ", fieldCA='" + getFieldCA() + "'" +
            ", fieldCB='" + getFieldCB() + "'" +
            "}";
    }
}
