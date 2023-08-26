package com.siemens.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An Asset with its Section and Unit
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetSectionUnitNameDTO
{
    private Long id;
    private String assetType;
    private String kksTag;
    
    private String sectionName;
    
    private String unitName;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getAssetType()
    {
        return assetType;
    }
    
    public void setAssetType(String assetType)
    {
        this.assetType = assetType;
    }
    
    public String getKksTag()
    {
        return kksTag;
    }
    
    public void setKksTag(String kksTag)
    {
        this.kksTag = kksTag;
    }
    
    public String getSectionName()
    {
        return sectionName;
    }
    
    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }
    
    public String getUnitName()
    {
        return unitName;
    }
    
    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }
    
    @Override
    public String toString()
    {
        return "AssetSectionUnitNameDTO [id=" + id + ", assetType=" + assetType + ", kksTag=" + kksTag + ", sectionName=" + sectionName
            + ", unitName=" + unitName + "]";
    }
    
}
