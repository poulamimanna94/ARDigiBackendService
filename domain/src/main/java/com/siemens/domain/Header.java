package com.siemens.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * A Section.
 */
@Entity
@Table(name = "section")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Header extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "section_sequenceGenerator")
    @SequenceGenerator(name = "section_sequenceGenerator")
    private Long id;
    
    @NotNull
    @Column(name = "section_name", nullable = false)
    private String sectionName;
    
    // @JsonBackReference
 //   @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
 //   @NotFound(action = NotFoundAction.IGNORE)
 //   @JsonIgnoreProperties(value = { "section", "unit" }, allowSetters = true)
//    @JsonIgnore
 //   private Set<Asset> assets = new HashSet<>();
    
    // @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    // @JsonIgnoreProperties(value = { "sections" }, allowSetters = true)
    @JsonIgnore
    private Unit unit;
   
    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE)
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnore
    private Set<SubHeader> subHeaders = new HashSet<>();
    
    public Header()
    {
        super();
    }
    
    public Header(Long id, @NotNull String sectionName, Instant createdAt, Instant modifiedAt, 
    		 Unit unit, Set<SubHeader> subHeaders)
    {
        super();
        this.id = id;
        this.sectionName = sectionName;
     //   this.assets = assets;
        this.unit = unit;
        this.subHeaders = subHeaders;
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
    
    public Header id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getSectionName()
    {
        return this.sectionName;
    }
    
    public Header sectionName(String sectionName)
    {
        this.sectionName = sectionName;
        return this;
    }
    
    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }
    
	/*
	 * public Set<Asset> getAssets() { return this.assets; }
	 */
    
	/*
	 * public Section assets(Set<Asset> assets) { this.setAssets(assets); return
	 * this; }
	 * 
	 * public Section addAsset(Asset asset) { this.assets.add(asset);
	 * asset.setSection(this); return this; }
	 * 
	 * public Section removeAsset(Asset asset) { this.assets.remove(asset);
	 * asset.setSection(null); return this; }
	 */
    
	/*
	 * public void setAssets(Set<Asset> assets) { if (this.assets != null) {
	 * this.assets.forEach(i -> i.setSection(null)); } if (assets != null) {
	 * assets.forEach(i -> i.setSection(this)); } this.assets = assets; }
	 */
    
    public Unit getUnit()
    {
        return this.unit;
    }
    
    public Header unit(Unit unit)
    {
        this.setUnit(unit);
        return this;
    }
    
    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }
    
    public Set<SubHeader> getSubHeaders() {
		return subHeaders;
	}
    
    public Header SubHeaders(Set<SubHeader> subHeaders)
    {
        this.setSubHeaders(subHeaders);
        return this;
    }
    
    public Header addSubHeader(SubHeader subHeader)
    {
        this.subHeaders.add(subHeader);
        subHeader.setSection(this);
        return this;
    }
    
    public Header removeSubHeader(SubHeader subHeader)
    {
        this.subHeaders.remove(subHeader);
        subHeader.setSection(null);
        return this;
    }

	public void setSubHeaders(Set<SubHeader> subHeaders) {
		 if (this.subHeaders != null)
	        {
	            this.subHeaders.forEach(i -> i.setSection(null));
	        }
	        if (subHeaders != null)
	        {
	        	subHeaders.forEach(i -> i.setSection(this));
	        }
		this.subHeaders = subHeaders;
	}

    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Header))
        {
            return false;
        }
        return id != null && id.equals(((Header) o).id);
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
        return "Section{" +
            "id=" + getId() +
            ", sectionName='" + getSectionName() + "'" +
            "}";
    }
}
