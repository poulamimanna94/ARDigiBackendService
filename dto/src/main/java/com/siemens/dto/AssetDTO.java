package com.siemens.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * An Asset with live data user representation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetDTO
{
    private Long id;
    private String assetType;
    private String elevation;
    private String x;
    private String y;
    private String z;
    private String kksTag;
    
    List<TagDTO> tagsData;
    
    private String alarmStatus;
    
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
    
    public String getElevation()
    {
        return elevation;
    }
    
    public void setElevation(String elevation)
    {
        this.elevation = elevation;
    }
    
    public String getX()
    {
        return x;
    }
    
    public void setX(String x)
    {
        this.x = x;
    }
    
    public String getY()
    {
        return y;
    }
    
    public void setY(String y)
    {
        this.y = y;
    }
    
    public String getZ()
    {
        return z;
    }
    
    public void setZ(String z)
    {
        this.z = z;
    }
    
    public String getKksTag()
    {
        return kksTag;
    }
    
    public void setKksTag(String kksTag)
    {
        this.kksTag = kksTag;
    }
    
    @Override
    public String toString()
    {
        return "assetType=" + assetType + ", elevation=" + elevation + ", x=" + x + ", y=" + y + ", z=" + z
            + ", kksTag=" + kksTag + ", TagsData=" + tagsData + ", alarmStatus=" + alarmStatus;
    }
    
    public List<TagDTO> getTagsData()
    {
        return tagsData;
    }
    
    public void setTagsData(List<TagDTO> tagsData)
    {
        this.tagsData = tagsData;
    }
    
    /**
     * @return the alarmStatus
     */
    public String getAlarmStatus()
    {
        return alarmStatus;
    }
    
    /**
     * @param alarmStatus the alarmStatus to set
     */
    public void setAlarmStatus(String alarmStatus)
    {
        this.alarmStatus = alarmStatus;
    }
    
}
