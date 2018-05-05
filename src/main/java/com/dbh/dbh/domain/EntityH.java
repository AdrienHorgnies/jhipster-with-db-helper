package com.dbh.dbh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A EntityH.
 */
@Entity
@Table(name = "table_h")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityH implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "field_ha")
    private String fieldHA;

    @Column(name = "field_hb")
    private Integer fieldHB;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "entityh_entitya",
               joinColumns = @JoinColumn(name="entityhs_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="entityas_id", referencedColumnName="id"))
    private Set<EntityA> entityAS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldHA() {
        return fieldHA;
    }

    public EntityH fieldHA(String fieldHA) {
        this.fieldHA = fieldHA;
        return this;
    }

    public void setFieldHA(String fieldHA) {
        this.fieldHA = fieldHA;
    }

    public Integer getFieldHB() {
        return fieldHB;
    }

    public EntityH fieldHB(Integer fieldHB) {
        this.fieldHB = fieldHB;
        return this;
    }

    public void setFieldHB(Integer fieldHB) {
        this.fieldHB = fieldHB;
    }

    public Set<EntityA> getEntityAS() {
        return entityAS;
    }

    public EntityH entityAS(Set<EntityA> entityAS) {
        this.entityAS = entityAS;
        return this;
    }

    public EntityH addEntityA(EntityA entityA) {
        this.entityAS.add(entityA);
        entityA.getEntityHS().add(this);
        return this;
    }

    public EntityH removeEntityA(EntityA entityA) {
        this.entityAS.remove(entityA);
        entityA.getEntityHS().remove(this);
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
        EntityH entityH = (EntityH) o;
        if (entityH.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityH.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityH{" +
            "id=" + getId() +
            ", fieldHA='" + getFieldHA() + "'" +
            ", fieldHB='" + getFieldHB() + "'" +
            "}";
    }
}
