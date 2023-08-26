package com.siemens.domain;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class TagSubscriptionId
{
    private String subscriptionName;
    
    /**
     * @return the subscriptionName
     */
    public String getSubscriptionName()
    {
        return this.subscriptionName;
    }
    
    /**
     * @param subscriptionName the subscriptionName to set
     */
    public void setSubscriptionName(String subscriptionName)
    {
        this.subscriptionName = subscriptionName;
    }
    
    @Override
    public String toString()
    {
        return "{subscriptionName:" + subscriptionName + "}";
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(subscriptionName);
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
        TagSubscriptionId other = (TagSubscriptionId) obj;
        return Objects.equals(subscriptionName, other.subscriptionName);
    }
    
    public TagSubscriptionId()
    {
        super();
    }
    
    public TagSubscriptionId(String subscriptionName)
    {
        super();
        this.subscriptionName = subscriptionName;
    }
}
