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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * A Asset.
 */
@Entity
@Table(name = "asset")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Asset extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "asset_sequenceGenerator")
    @SequenceGenerator(name = "asset_sequenceGenerator")
    private Long id;
    
    @Column(name = "asset_type")
    private String assetType;
    
    @Column(name = "elevation")
    private String elevation;
    
    @Column(name = "x")
    private String x;
    
    @Column(name = "y")
    private String y;
    
    @Column(name = "z")
    private String z;
    
    @Column(name = "kks_tag")
    private String kksTag;
    
    // @JsonBackReference
    @OneToMany(mappedBy = "asset", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    // @JsonIgnoreProperties(value = { "section" }, allowSetters = true)
    @JsonIgnore
    private Set<SharedTags> sharedTags = new HashSet<>();
    
    // @JsonBackReference
    @OneToMany(mappedBy = "asset", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    // @JsonIgnoreProperties(value = { "section" }, allowSetters = true)
    @JsonIgnore
    private Set<Document> documents = new HashSet<>();
    
    @OneToMany(mappedBy = "asset", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotFound(action = NotFoundAction.IGNORE)
    // @JsonIgnoreProperties(value = { "section" }, allowSetters = true)
    @JsonIgnore
    private Set<Tag> tags = new HashSet<>();
    
	
	// @JsonManagedReference
	  
  //  @ManyToOne(fetch = FetchType.LAZY)
  //  @JoinColumn(name = "section_id") 
   // @JsonIgnoreProperties(value = { "assets", "unit" }, allowSetters = true)
 //	@JsonIgnore 
 //	private Section section;
	 
    
 // @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subheader_id")
    @JsonIgnore
    private SubHeader subHeader;
    
    
    public Asset()
    {
        super();
    }
    
    public Asset(Long id, String assetType, String elevation, String x, String y, String z, String kksTag,
        Instant createdAt, Instant modifiedAt, Set<SharedTags> sharedTags, Set<Document> documents, Set<Tag> tags,
        SubHeader subHeader)
    {
        super();
        this.id = id;
        this.assetType = assetType;
        this.elevation = elevation;
        this.x = x;
        this.y = y;
        this.z = z;
        this.kksTag = kksTag;
        this.sharedTags = sharedTags;
        this.documents = documents;
        this.tags = tags;
        this.subHeader = subHeader;
    }
    
    public Asset tags(Set<Tag> tags)
    {
        this.setTags(tags);
        return this;
    }
    
    public Asset addTag(Tag tag)
    {
        this.tags.add(tag);
        tag.setAsset(this);
        return this;
    }
    
    public Asset removeTag(Tag tag)
    {
        this.tags.remove(tag);
        tag.setAsset(null);
        return this;
    }
    
    public Set<Document> getDocuments()
    {
        return documents;
    }
    
    public void setDocuments(Set<Document> documents)
    {
        this.documents = documents;
    }
    
    public Set<Tag> getTags()
    {
        return this.tags;
    }
    
    public void setTags(Set<Tag> tags)
    {
        if (this.tags != null)
            this.tags.forEach(i -> i.setAsset(null));
        if (tags != null)
            tags.forEach(i -> i.setAsset(this));
        this.tags = tags;
    }
    
    public Set<SharedTags> getSharedTags()
    {
        return sharedTags;
    }
    
    public void setSharedTags(Set<SharedTags> sharedTags)
    {
        this.sharedTags = sharedTags;
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
    
    public Asset id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getAssetType()
    {
        return this.assetType;
    }
    
    public Asset assetType(String assetType)
    {
        this.assetType = assetType;
        return this;
    }
    
    public void setAssetType(String assetType)
    {
        this.assetType = assetType;
    }
    
    public String getElevation()
    {
        return this.elevation;
    }
    
    public Asset elevation(String elevation)
    {
        this.elevation = elevation;
        return this;
    }
    
    public void setElevation(String elevation)
    {
        this.elevation = elevation;
    }
    
    public String getX()
    {
        return this.x;
    }
    
    public Asset x(String x)
    {
        this.x = x;
        return this;
    }
    
    public void setX(String x)
    {
        this.x = x;
    }
    
    public String getY()
    {
        return this.y;
    }
    
    public Asset y(String y)
    {
        this.y = y;
        return this;
    }
    
    public void setY(String y)
    {
        this.y = y;
    }
    
    public String getZ()
    {
        return this.z;
    }
    
    public Asset z(String z)
    {
        this.z = z;
        return this;
    }
    
    public void setZ(String z)
    {
        this.z = z;
    }
    
	
	/*
	 * public Section getSection() { return this.section; }
	 * 
	 * public Asset section(Section section) { this.setSection(section); return
	 * this; }
	 * 
	 * public void setSection(Section section) { this.section = section; }
	 */
	 
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    
    public String getKksTag()
    {
        return kksTag;
    }
    
    public void setKksTag(String kksTag)
    {
        this.kksTag = kksTag;
    }
    
    
    
    public SubHeader getSubHeader() {
		return this.subHeader;
	}

    public Asset subHeader(SubHeader subHeader)
    {
        this.setSubHeader(subHeader);
        return this;
    }
    
	public void setSubHeader(SubHeader subHeader) {
		this.subHeader = subHeader;
	}

	@Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Asset))
        {
            return false;
        }
        return id != null && id.equals(((Asset) o).id);
    }
    
    @Override
    public int hashCode()
    {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
    
    @Override
    public String toString()
    {
        return "Asset [id=" + id + ", assetType=" + assetType + ", elevation=" + elevation + ", x=" + x + ", y=" + y
            + ", z=" + z + ", kksTag=" + kksTag
            + "]";
    }
}
