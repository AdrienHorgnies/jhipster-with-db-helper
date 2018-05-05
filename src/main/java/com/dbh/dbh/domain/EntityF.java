package com.dbh.dbh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EntityF.
 */
@Entity
@Table(name = "table_f")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityF implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_f_id")
    private Long id;

    @Column(name = "column_fa")
    private String fieldFA;

    @Column(name = "column_fb")
    private Integer fieldFB;

    @OneToOne
    @JoinColumn(unique = true, name = "table_a_fk")
    private EntityA entityA;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldFA() {
        return fieldFA;
    }

    public EntityF fieldFA(String fieldFA) {
        this.fieldFA = fieldFA;
        return this;
    }

    public void setFieldFA(String fieldFA) {
        this.fieldFA = fieldFA;
    }

    public Integer getFieldFB() {
        return fieldFB;
    }

    public EntityF fieldFB(Integer fieldFB) {
        this.fieldFB = fieldFB;
        return this;
    }

    public void setFieldFB(Integer fieldFB) {
        this.fieldFB = fieldFB;
    }

    public EntityA getEntityA() {
        return entityA;
    }

    public EntityF entityA(EntityA entityA) {
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
        EntityF entityF = (EntityF) o;
        if (entityF.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityF.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityF{" +
            "id=" + getId() +
            ", fieldFA='" + getFieldFA() + "'" +
            ", fieldFB='" + getFieldFB() + "'" +
            "}";
    }
}
