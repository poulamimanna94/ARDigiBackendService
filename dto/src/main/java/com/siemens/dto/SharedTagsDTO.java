package com.siemens.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Shared Tag DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SharedTagsDTO
{
    private Long id;
    private String userNameFrom;
    private String userNameTo;
    private Long assetId;
    
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
    
    public Long getAssetId()
    {
        return assetId;
    }
    
    public void setAssetId(Long assetId)
    {
        this.assetId = assetId;
    }
    
    @Override
    public String toString()
    {
        return "SharedTagsDTO [id=" + id + ", userNameFrom=" + userNameFrom + ", userNameTo=" + userNameTo
            + ", assetId=" + assetId + "]";
    }
    
}
