package com.siemens.dto;

import java.util.List;

/**
 * A Unit DTO with Section Names
 */
public class UnitSectionNamesDto
{
    
    private Long id;
    private String unitName;
    
    private List<String> sectionNames;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public UnitSectionNamesDto id(Long id)
    {
        this.id = id;
        return this;
    }
    
    public String getUnitName()
    {
        return this.unitName;
    }
    
    public UnitSectionNamesDto unitName(String unitName)
    {
        this.unitName = unitName;
        return this;
    }
    
    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
    }
    
    public List<String> getSectionNames()
    {
        return sectionNames;
    }
    
    public void setSectionNames(List<String> sectionNames)
    {
        this.sectionNames = sectionNames;
    }
    
    @Override
    public String toString()
    {
        return "UnitSectionNamesDto [id=" + id + ", unitName=" + unitName + ", sectionNames=" + sectionNames + "]";
    }
    
}
