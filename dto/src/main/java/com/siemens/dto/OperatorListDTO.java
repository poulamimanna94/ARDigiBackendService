package com.siemens.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.siemens.domain.UserAudit;

/**
 * Operator List DTO for dashboard screen
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperatorListDTO
{
    UserAudit usrAudObj;
    private Double dailyDuration;
    private Double avgDuration;
    
    public UserAudit getUsrAudObj()
    {
        return usrAudObj;
    }
    
    public void setUsrAudObj(UserAudit usrAudObj)
    {
        this.usrAudObj = usrAudObj;
    }
    
    public Double getDailyDuration()
    {
        return dailyDuration;
    }
    
    public void setDailyDuration(Double dailyDuration)
    {
        this.dailyDuration = dailyDuration;
    }
    
    public Double getAvgDuration()
    {
        return avgDuration;
    }
    
    public void setAvgDuration(Double avgDuration)
    {
        this.avgDuration = avgDuration;
    }
    
    @Override
    public String toString()
    {
        return "OperatorListDTO [usrAudObj=" + usrAudObj + ", dailyDuration=" + dailyDuration + ", avgDuration=" + avgDuration + "]";
    }
    
}
