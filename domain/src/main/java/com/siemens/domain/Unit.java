package com.siemens.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Unit.
 */
@Entity
@Table(name = "unit")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Unit extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "unit_sequenceGenerator")
    @SequenceGenerator(name = "unit_sequenceGenerator")
    private Long id;
    
    @NotNull
    @Column(name = "unit_name", nullable = false)
    private String unitName;
    
    // @JsonBackReference
    @OneToMany(mappedBy = "unit", cascade = CascadeType.REMOVE)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties(value = { "unit" }, allowSetters = true)
    // @JsonIgnore
    // @JsonProperty(access = Access.READ_ONLY)
    private Set<Header> sections = new HashSet<>();
    
    public Unit()
    {
        super();
    }
    
    public Unit(Long id, @NotNull String unitName, Instant createdAt, Instant modifiedAt, Set<Header> sections)
    {
        super();
        this.id = id;
        this.unitName = unitName;
        this.sections = sections;
    }
    
    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Unit id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getUnitName()
    {
        return this.unitName;
    }
    
    public Unit unitName(String unitName)
    {
        this.unitName = unitName;
        return this;
    }
    
    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }
    
    public Set<Header> getSections()
    {
        return this.sections;
    }
    
    public Unit sections(Set<Header> sections)
    {
        this.setSections(sections);
        return this;
    }
    
    public Unit addSection(Header section)
    {
        this.sections.add(section);
        section.setUnit(this);
        return this;
    }
    
    public Unit removeSection(Header section)
    {
        this.sections.remove(section);
        section.setUnit(null);
        return this;
    }
    
    public void setSections(Set<Header> sections)
    {
        if (this.sections != null)
        {
            this.sections.forEach(i -> i.setUnit(null));
        }
        if (sections != null)
        {
            sections.forEach(i -> i.setUnit(this));
        }
        this.sections = sections;
    }
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Unit))
        {
            return false;
        }
        return id != null && id.equals(((Unit) o).id);
    }
    
    @Override
    public int hashCode()
    {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
    
    // prettier-ignore
    @Override
    public String toString()
    {
        return "Unit{" +
            "id=" + getId() +
            ", unitName='" + getUnitName() + "'" +
            "}";
    }
}
