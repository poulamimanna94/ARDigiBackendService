package com.siemens.dto;

import java.util.List;

/**
 * A Section DTO with Asset KKS Tags
 */
public class SectionAssetKksTagsDTO
{
    
    private Long id;
    private String sectionName;
    
    private List<String> assetKksTags;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public SectionAssetKksTagsDTO id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getSectionName()
    {
        return this.sectionName;
    }
    
    public SectionAssetKksTagsDTO sectionName(String sectionName)
    {
        this.sectionName = sectionName;
        return this;
    }
    
    public void setSectionName(String sectionName)
    {
        this.sectionName = sectionName;
    }
    
    public List<String> getAssetKksTags()
    {
        return assetKksTags;
    }
    
    public void setAssetKksTags(List<String> assetKksTags)
    {
        this.assetKksTags = assetKksTags;
    }
    
    @Override
    public String toString()
    {
        return "SectionAssetKksTagsDTO [id=" + id + ", sectionName=" + sectionName + ", assetKksTags=" + assetKksTags + "]";
    }
    
}
