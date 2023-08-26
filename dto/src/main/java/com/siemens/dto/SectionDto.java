package com.siemens.dto;

import java.time.Instant;

/**
 * A Section.
 */
public class SectionDto
{
    
    private Long id;
    private String sectionName;
    private Instant createdAt;
    private Instant modifiedAt;
    
    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public SectionDto id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getSectionName()
    {
        return this.sectionName;
    }
    
    public SectionDto sectionName(String sectionName)
    {
        this.sectionName = sectionName;
        return this;
    }
    
    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }
    
    public Instant getCreatedAt()
    {
        return this.createdAt;
    }
    
    public SectionDto createdAt(Instant createdAt)
    {
        this.createdAt = createdAt;
        return this;
    }
    
    public void setCreatedAt(Instant createdAt)
    {
        this.createdAt = createdAt;
    }
    
    public Instant getModifiedAt()
    {
        return this.modifiedAt;
    }
    
    public SectionDto modifiedAt(Instant modifiedAt)
    {
        this.modifiedAt = modifiedAt;
        return this;
    }
    
    public void setModifiedAt(Instant modifiedAt)
    {
        this.modifiedAt = modifiedAt;
    }
    
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SectionDto))
        {
            return false;
        }
        return id != null && id.equals(((SectionDto) o).id);
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
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            "}";
    }
}
