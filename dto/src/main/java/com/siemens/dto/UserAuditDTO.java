package com.siemens.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Audit DTO for user login/logout history.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuditDTO
{
    private Instant lastLogin;
    private Instant lastLogout;
    private LocalDate dt;
    private long duration;
    
    public Instant getLastLogin()
    {
        return lastLogin;
    }
    
    public void setLastLogin(Instant lastLogin)
    {
        this.lastLogin = lastLogin;
    }
    
    public Instant getLastLogout()
    {
        return lastLogout;
    }
    
    public void setLastLogout(Instant lastLogout)
    {
        this.lastLogout = lastLogout;
    }
    
    public long getDuration()
    {
        return duration;
    }
    
    public void setDuration(long duration)
    {
        this.duration = duration;
    }
    
    public LocalDate getDt()
    {
        return dt;
    }
    
    public void setDt(LocalDate dt)
    {
        this.dt = dt;
    }
    
    @Override
    public String toString()
    {
        return "UserAuditDTO [lastLogin=" + lastLogin + ", lastLogout=" + lastLogout + ", date=" + dt + ", duration=" + duration + "]\n";
    }
}
