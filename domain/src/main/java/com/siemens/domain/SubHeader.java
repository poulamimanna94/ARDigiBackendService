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
 * A SubHeader.
 */
@Entity
@Table(name = "subheader")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SubHeader extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subHeader_sequenceGenerator")
    @SequenceGenerator(name = "subHeader_sequenceGenerator")
    private Long id;
    
    @Column(name = "subheader_name")
    private String subHeaderName;
    
	@Column(name = "descr")
    private String desc;
    
    // @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonIgnore
    private Header section;
    
    public SubHeader()
    {
        super();
    }
    
    public SubHeader(Long id, String subHeaderName, String desc, Instant createdAt, Instant modifiedAt, Header section)
    {
        super();
        this.id = id;
        this.subHeaderName = subHeaderName;
        this.desc = desc;
        this.section = section;
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
    
    public SubHeader id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getSubHeaderName() {
		return subHeaderName;
	}

    public SubHeader subHeaderName(String subHeaderName)
    {
        this.subHeaderName = subHeaderName;
        return this;
    }
    
	public void setSubHeaderName(String subHeaderName) {
		this.subHeaderName = subHeaderName;
	}

	public String getDesc() {
		return this.desc;
	}
	
	 public SubHeader desc(String desc)
	 {
	        this.desc = desc;
	        return this;
	 }

	public void setDesc(String desc) {
		this.desc = desc;
	}
    
    public Header getSection()
    {
        return this.section;
    }
    
    public SubHeader section(Header section)
    {
        this.setSection(section);
        return this;
    }
    
    public void setSection(Header section)
    {
        this.section = section;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SubHeader))
        {
            return false;
        }
        return id != null && id.equals(((SubHeader) o).id);
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
        return "Asset [id=" + id + ", subHeaderName=" + subHeaderName + ", desc=" + desc 
            + "]";
    }
}
