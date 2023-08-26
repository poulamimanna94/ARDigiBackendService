package com.siemens.dto;

import java.time.Instant;

/**
 * A Unit.
 */
public class UnitDto
{
    
    private Long id;
    private String unitName;
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
    
    public UnitDto id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getUnitName()
    {
        return this.unitName;
    }
    
    public UnitDto unitName(String unitName)
    {
        this.unitName = unitName;
        return this;
    }
    
    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }
    
    public Instant getCreatedAt()
    {
        return this.createdAt;
    }
    
    public UnitDto createdAt(Instant createdAt)
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
    
    public UnitDto modifiedAt(Instant modifiedAt)
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
        if (!(o instanceof UnitDto))
        {
            return false;
        }
        return id != null && id.equals(((UnitDto) o).id);
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
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            "}";
    }
}
