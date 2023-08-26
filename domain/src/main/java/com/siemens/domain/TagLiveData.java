package com.siemens.domain;

import java.util.Objects;

//@SuppressWarnings({ "serial", "rawtypes" })
public class TagLiveData
{
    
    private Long index;
    private String displayName;
    private Double value;
    private String date;
    private String nodeId;
    
    public TagLiveData()
    {
        super();
    }
    
    public Long getIndex()
    {
        return index;
    }
    
    public void setIndex(Long index)
    {
        this.index = index;
    }
    
    public String getDisplayName()
    {
        return displayName;
    }
    
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
    
    public Double getValue()
    {
        return value;
    }
    
    public void setValue(Double value)
    {
        this.value = value;
    }
    
    public String getDate()
    {
        return date;
    }
    
    public void setDate(String date)
    {
        this.date = date;
    }
    
    public String getNodeId()
    {
        return nodeId;
    }
    
    public void setNodeId(String nodeId)
    {
        this.nodeId = nodeId;
    }
    
    @Override
    public String toString()
    {
        return "LiveData {index=" + index + ", displayName=" + displayName + ", value=" + value + ", date=" + date
            + ", nodeId=" + nodeId + "}";
    }
    
    public TagLiveData(Long index, String displayName, Double value, String date, String nodeId)
    {
        super();
        this.index = index;
        this.displayName = displayName;
        this.value = value;
        this.date = date;
        this.nodeId = nodeId;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(date, displayName, index, nodeId, value);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TagLiveData other = (TagLiveData) obj;
        return Objects.equals(date, other.date) && Objects.equals(displayName, other.displayName)
            && Objects.equals(index, other.index) && Objects.equals(nodeId, other.nodeId)
            && Objects.equals(value, other.value);
    }
    
}
