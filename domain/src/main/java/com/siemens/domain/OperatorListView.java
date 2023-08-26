package com.siemens.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 * Audit for user login/logout .
 */
@Entity
@Table(name = "operatorlist_temp")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OperatorListView implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operatorlist_view_sequence_generator")
    @SequenceGenerator(name = "operatorlist_view_sequence_generator")
    private Long id;
    
    @Size(max = 50)
    @Column(name = "user_name", length = 50)
    private String userName;
    
    @Column(name = "last_login")
    private Instant lastLogin;
    
    @Column(name = "last_logout")
    private Instant lastLogout;
    
    @Size(max = 20)
    @Column(name = "login_status", length = 20)
    private String loginStatus;
    
    @Column(name = "dailyduration", updatable = false, insertable = false)
    private Double dailyDuration;
    
    @Column(name = "avgduration", updatable = false, insertable = false)
    private Double avgDuration;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public void setUserName(String userName)
    {
        this.userName = userName;
    }
    
    public String getLoginStatus()
    {
        return loginStatus;
    }
    
    public void setLoginStatus(String loginStatus)
    {
        this.loginStatus = loginStatus;
    }
    
    public Instant getLastLogin()
    {
        return lastLogin;
    }
    
    public void setLastLogin(Instant lastLogin)
    {
        this.lastLogin = lastLogin;
    }
    
    public void setLastLogin()
    {
        this.lastLogin = Instant.now();
    }
    
    public Instant getLastLogout()
    {
        return lastLogout;
    }
    
    public void setLastLogout(Instant lastLogout)
    {
        this.lastLogout = lastLogout;
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
        return "OperatorListView [id=" + id + ", userName=" + userName + ", lastLogin=" + lastLogin + ", lastLogout=" + lastLogout
            + ", loginStatus=" + loginStatus + ", dailyDuration=" + dailyDuration + ", avgDuration=" + avgDuration + "]";
    }
    
}
