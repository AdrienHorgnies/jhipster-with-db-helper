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
 * A EntityA.
 */
@Entity
@Table(name = "table_a")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class EntityA implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_a_id")
    private Long id;

    @Column(name = "column_aa")
    private String fieldAA;

    @Column(name = "column_ab")
    private Integer fieldAB;

    @OneToOne
    @JoinColumn(unique = true, name = "table_b_fk")
    private EntityB entityB;

    @ManyToOne
    @JoinColumn(name = "table_c_fk")
    private EntityC entityC;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "table_a_table_d",
               joinColumns = @JoinColumn(name="table_a_fk", referencedColumnName="table_a_id"),
               inverseJoinColumns = @JoinColumn(name="table_d_fk", referencedColumnName="table_d_id"))
    private Set<EntityD> entityDS = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "table_a_table_e",
               joinColumns = @JoinColumn(name="table_a_fk", referencedColumnName="table_a_id"),
               inverseJoinColumns = @JoinColumn(name="table_e_fk", referencedColumnName="table_e_id"))
    private Set<EntityE> entityES = new HashSet<>();

    @OneToOne(mappedBy = "entityA")
    @JsonIgnore
    private EntityF entityF;

    @OneToMany(mappedBy = "entityA")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntityG> entityGS = new HashSet<>();

    @ManyToMany(mappedBy = "entityAS")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<EntityH> entityHS = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldAA() {
        return fieldAA;
    }

    public EntityA fieldAA(String fieldAA) {
        this.fieldAA = fieldAA;
        return this;
    }

    public void setFieldAA(String fieldAA) {
        this.fieldAA = fieldAA;
    }

    public Integer getFieldAB() {
        return fieldAB;
    }

    public EntityA fieldAB(Integer fieldAB) {
        this.fieldAB = fieldAB;
        return this;
    }

    public void setFieldAB(Integer fieldAB) {
        this.fieldAB = fieldAB;
    }

    public EntityB getEntityB() {
        return entityB;
    }

    public EntityA entityB(EntityB entityB) {
        this.entityB = entityB;
        return this;
    }

    public void setEntityB(EntityB entityB) {
        this.entityB = entityB;
    }

    public EntityC getEntityC() {
        return entityC;
    }

    public EntityA entityC(EntityC entityC) {
        this.entityC = entityC;
        return this;
    }

    public void setEntityC(EntityC entityC) {
        this.entityC = entityC;
    }

    public Set<EntityD> getEntityDS() {
        return entityDS;
    }

    public EntityA entityDS(Set<EntityD> entityDS) {
        this.entityDS = entityDS;
        return this;
    }

    public EntityA addEntityD(EntityD entityD) {
        this.entityDS.add(entityD);
        entityD.getEntityAS().add(this);
        return this;
    }

    public EntityA removeEntityD(EntityD entityD) {
        this.entityDS.remove(entityD);
        entityD.getEntityAS().remove(this);
        return this;
    }

    public void setEntityDS(Set<EntityD> entityDS) {
        this.entityDS = entityDS;
    }

    public Set<EntityE> getEntityES() {
        return entityES;
    }

    public EntityA entityES(Set<EntityE> entityES) {
        this.entityES = entityES;
        return this;
    }

    public EntityA addEntityE(EntityE entityE) {
        this.entityES.add(entityE);
        entityE.getEntityAS().add(this);
        return this;
    }

    public EntityA removeEntityE(EntityE entityE) {
        this.entityES.remove(entityE);
        entityE.getEntityAS().remove(this);
        return this;
    }

    public void setEntityES(Set<EntityE> entityES) {
        this.entityES = entityES;
    }

    public EntityF getEntityF() {
        return entityF;
    }

    public EntityA entityF(EntityF entityF) {
        this.entityF = entityF;
        return this;
    }

    public void setEntityF(EntityF entityF) {
        this.entityF = entityF;
    }

    public Set<EntityG> getEntityGS() {
        return entityGS;
    }

    public EntityA entityGS(Set<EntityG> entityGS) {
        this.entityGS = entityGS;
        return this;
    }

    public EntityA addEntityG(EntityG entityG) {
        this.entityGS.add(entityG);
        entityG.setEntityA(this);
        return this;
    }

    public EntityA removeEntityG(EntityG entityG) {
        this.entityGS.remove(entityG);
        entityG.setEntityA(null);
        return this;
    }

    public void setEntityGS(Set<EntityG> entityGS) {
        this.entityGS = entityGS;
    }

    public Set<EntityH> getEntityHS() {
        return entityHS;
    }

    public EntityA entityHS(Set<EntityH> entityHS) {
        this.entityHS = entityHS;
        return this;
    }

    public EntityA addEntityH(EntityH entityH) {
        this.entityHS.add(entityH);
        entityH.getEntityAS().add(this);
        return this;
    }

    public EntityA removeEntityH(EntityH entityH) {
        this.entityHS.remove(entityH);
        entityH.getEntityAS().remove(this);
        return this;
    }

    public void setEntityHS(Set<EntityH> entityHS) {
        this.entityHS = entityHS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EntityA entityA = (EntityA) o;
        if (entityA.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), entityA.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "EntityA{" +
            "id=" + getId() +
            ", fieldAA='" + getFieldAA() + "'" +
            ", fieldAB='" + getFieldAB() + "'" +
            "}";
    }
}
