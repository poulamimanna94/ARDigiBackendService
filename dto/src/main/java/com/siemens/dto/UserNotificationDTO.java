package com.siemens.dto;

import java.time.Instant;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * User Notifications for Shared Tags DTO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserNotificationDTO
{
    private Long id;
    private String userNameFrom;
    private String userNameTo;
    private Boolean hasRead = false;
    private Boolean isComplete = false;
    private Instant createdAt;
    
    private Long assetId;
    
    private String assetKksTag;
    
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
    
    public String getAssetKksTag()
    {
        return assetKksTag;
    }
    
    public void setAssetKksTag(String assetKksTag)
    {
        this.assetKksTag = assetKksTag;
    }
    
    public Instant getCreatedAt()
    {
        return createdAt;
    }
    
    public void setCreatedAt(Instant createdAt)
    {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString()
    {
        return "UserNotificationDTO [id=" + id + ", userNameFrom=" + userNameFrom + ", userNameTo=" + userNameTo
            + ", hasRead=" + hasRead + ", isComplete=" + isComplete + ", createdAt=" + createdAt + ", assetId="
            + assetId + ", assetKksTag=" + assetKksTag + "]";
    }
    
}
