package com.siemens.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Shared Tags Entity
 */
@Entity
@Table(name = "shared_tags")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class SharedTags extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_sequenceGenerator")
    @SequenceGenerator(name = "tag_sequenceGenerator")
    private Long id;
    
    @NotNull
    @Column(name = "user_name_from")
    private String userNameFrom;
    
    @Column(name = "user_name_to")
    private String userNameTo;
    
    @Column(name = "has_read")
    private Boolean hasRead = false;
    
    @Column(name = "is_complete")
    private Boolean isComplete = false;
    
    // @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id")
    // @JsonIgnoreProperties(value = { "assets", "unit" }, allowSetters = true)
    @JsonIgnore
    private Asset asset;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getUserNameFrom()
    {
        return userNameFrom;
    }
    
    public void setUserNameFrom(String userNameFrom)
    {
        this.userNameFrom = userNameFrom;
    }
    
    public String getUserNameTo()
    {
        return userNameTo;
    }
    
    public void setUserNameTo(String userNameTo)
    {
        this.userNameTo = userNameTo;
    }
    
    public Boolean getHasRead()
    {
        return hasRead;
    }
    
    public void setHasRead(Boolean hasRead)
    {
        this.hasRead = hasRead;
    }
    
    public Boolean getIsComplete()
    {
        return isComplete;
    }
    
    public void setIsComplete(Boolean isComplete)
    {
        this.isComplete = isComplete;
    }
    
    public Asset getAsset()
    {
        return this.asset;
    }
    
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }
    
    public SharedTags()
    {
        super();
    }
    
    public SharedTags(Long id, @NotNull String userNameFrom, String userNameTo, Boolean hasRead, Boolean isComplete,
        Instant createdAt, Instant modifiedAt, Asset asset)
    {
        super();
        this.id = id;
        this.userNameFrom = userNameFrom;
        this.userNameTo = userNameTo;
        this.hasRead = hasRead;
        this.isComplete = isComplete;
        this.asset = asset;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof SharedTags))
        {
            return false;
        }
        return id != null && id.equals(((SharedTags) o).id);
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
        return "SharedTags [id=" + id + ", userNameFrom=" + userNameFrom + ", userNameTo=" + userNameTo + ", hasRead="
            + hasRead + ", isComplete=" + isComplete + ", assetID=" + asset.getId() + "]";
    }
    
}
