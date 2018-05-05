package com.dbh.dbh.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A EntityG.
 */
@Entity
@Table(name = "table_g")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityG implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_g_id")
    private Long id;

    @Column(name = "column_ga")
    private String fieldGA;

    @Column(name = "column_gb")
    private Integer fieldGB;

    @ManyToOne
    @JoinColumn(name = "table_a_fk")
    private EntityA entityA;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldGA() {
        return fieldGA;
    }

    public EntityG fieldGA(String fieldGA) {
        this.fieldGA = fieldGA;
        return this;
    }

    public void setFieldGA(String fieldGA) {
        this.fieldGA = fieldGA;
    }

    public Integer getFieldGB() {
        return fieldGB;
    }

    public EntityG fieldGB(Integer fieldGB) {
        this.fieldGB = fieldGB;
        return this;
    }

    public void setFieldGB(Integer fieldGB) {
        this.fieldGB = fieldGB;
    }

    public EntityA getEntityA() {
        return entityA;
    }

    public EntityG entityA(EntityA entityA) {
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
        EntityG entityG = (EntityG) o;
        if (entityG.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityG.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityG{" +
            "id=" + getId() +
            ", fieldGA='" + getFieldGA() + "'" +
            ", fieldGB='" + getFieldGB() + "'" +
            "}";
    }
}
