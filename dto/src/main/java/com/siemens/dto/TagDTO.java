package com.siemens.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.siemens.domain.TagLiveData;

/**
 * A Tag with live data user representation
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TagDTO
{
    private String displayName;
    private String subscriptionName;
    private String desc;
    private String units;
    private Float lowLow;
    private Float low;
    private Float highHigh;
    private Float high;
    
    TagLiveData nodeValue;
    
    private String alarmStatus;
    
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
    
    public TagLiveData getNodeValue()
    {
        return nodeValue;
    }
    
    public void setNodeValue(TagLiveData nodeValue)
    {
        this.nodeValue = nodeValue;
    }
    
    @Override
    public String toString()
    {
        return "displayName=" + displayName + ", subscriptionName=" + subscriptionName + ", desc=" + desc
            + ", units=" + units + ", lowLow=" + lowLow + ", low=" + low + ", highHigh=" + highHigh + ", high="
            + high + ", nodeValue=" + nodeValue + ", alarmStatus=" + alarmStatus;
    }
    
    public String getAlarmStatus()
    {
        return alarmStatus;
    }
    
    public void setAlarmStatus(String alarmStatus)
    {
        this.alarmStatus = alarmStatus;
    }
    
}
