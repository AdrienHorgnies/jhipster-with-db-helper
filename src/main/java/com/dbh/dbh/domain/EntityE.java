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
 * A EntityE.
 */
@Entity
@Table(name = "table_e")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityE implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_e_id")
    private Long id;

    @Column(name = "column_ea")
    private String fieldEA;

    @Column(name = "column_eb")
    private String fieldEB;

    @ManyToMany(mappedBy = "entityES")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntityA> entityAS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldEA() {
        return fieldEA;
    }

    public EntityE fieldEA(String fieldEA) {
        this.fieldEA = fieldEA;
        return this;
    }

    public void setFieldEA(String fieldEA) {
        this.fieldEA = fieldEA;
    }

    public String getFieldEB() {
        return fieldEB;
    }

    public EntityE fieldEB(String fieldEB) {
        this.fieldEB = fieldEB;
        return this;
    }

    public void setFieldEB(String fieldEB) {
        this.fieldEB = fieldEB;
    }

    public Set<EntityA> getEntityAS() {
        return entityAS;
    }

    public EntityE entityAS(Set<EntityA> entityAS) {
        this.entityAS = entityAS;
        return this;
    }

    public EntityE addEntityA(EntityA entityA) {
        this.entityAS.add(entityA);
        entityA.getEntityES().add(this);
        return this;
    }

    public EntityE removeEntityA(EntityA entityA) {
        this.entityAS.remove(entityA);
        entityA.getEntityES().remove(this);
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
        EntityE entityE = (EntityE) o;
        if (entityE.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityE.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityE{" +
            "id=" + getId() +
            ", fieldEA='" + getFieldEA() + "'" +
            ", fieldEB='" + getFieldEB() + "'" +
            "}";
    }
}
