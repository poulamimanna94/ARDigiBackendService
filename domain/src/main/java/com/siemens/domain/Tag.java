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
 * A Tag.
 */
@Entity
@Table(name = "tag")
// @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Tag extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tag_sequenceGenerator")
    @SequenceGenerator(name = "tag_sequenceGenerator")
    private Long id;
    
    @NotNull
    @Column(name = "display_name")
    private String displayName;
    
    @Column(name = "subscription_name")
    private String subscriptionName;
    
    @Column(name = "tag_desc")
    private String desc;
    
    @Column(name = "tag_units")
    private String units;
    
    @Column(name = "low_low")
    private Float lowLow;
    
    @Column(name = "low")
    private Float low;
    
    @Column(name = "high_high")
    private Float highHigh;
    
    @Column(name = "high")
    private Float high;
    
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
    
    public String getDisplayName()
    {
        return displayName;
    }
    
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    public String getSubscriptionName()
    {
        return subscriptionName;
    }
    
    public void setSubscriptionName(String subscriptionName)
    {
        this.subscriptionName = subscriptionName;
    }
    
    public String getDesc()
    {
        return desc;
    }
    
    public void setDesc(String desc)
    {
        this.desc = desc;
    }
    
    public String getUnits()
    {
        return units;
    }
    
    public void setUnits(String units)
    {
        this.units = units;
    }
    
    public Float getLowLow()
    {
        return lowLow;
    }
    
    public void setLowLow(Float lowLow)
    {
        this.lowLow = lowLow;
    }
    
    public Float getLow()
    {
        return low;
    }
    
    public void setLow(Float low)
    {
        this.low = low;
    }
    
    public Float getHighHigh()
    {
        return highHigh;
    }
    
    public void setHighHigh(Float highHigh)
    {
        this.highHigh = highHigh;
    }
    
    public Float getHigh()
    {
        return high;
    }
    
    public void setHigh(Float high)
    {
        this.high = high;
    }
    
    public Asset getAsset()
    {
        return this.asset;
    }
    
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }
    
    public Tag()
    {
        super();
    }
    
    public Tag(Long id, @NotNull String displayName, String subscriptionName, String desc, String units, Float lowLow,
        Float low, Float highHigh, Float high, Instant createdAt, Instant modifiedAt)
    {
        super();
        this.id = id;
        this.displayName = displayName;
        this.subscriptionName = subscriptionName;
        this.desc = desc;
        this.units = units;
        this.lowLow = lowLow;
        this.low = low;
        this.highHigh = highHigh;
        this.high = high;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Tag))
        {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
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
        return "Tag [dbid=" + id + ", displayName=" + displayName + ", subscriptionName=" + subscriptionName + ", desc="
            + desc + ", units=" + units + ", lowLow=" + lowLow + ", low=" + low + ", highHigh=" + highHigh
            + ", high=" + high + ", assetID=" + asset.getId()
            + "]";
    }
    
}
