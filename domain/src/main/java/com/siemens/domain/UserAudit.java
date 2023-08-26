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
@Table(name = "user_audit")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserAudit implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_audit_sequence_generator")
    @SequenceGenerator(name = "user_audit_sequence_generator")
    private Long id;
    
    @Size(max = 50)
    @Column(name = "user_name", length = 50)
    private String userName;
    
    @Size(max = 20)
    @Column(name = "login_status", length = 20)
    private String loginStatus;
    
    @Column(name = "last_login")
    private Instant lastLogin;
    
    @Column(name = "last_logout")
    private Instant lastLogout;
    
    @Column(name = "logout_flag")
    private Boolean logoutFlag;
    
    @Column(name = "session_key", length = 2550)
    private String sessionKey;
    
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
    
    public String getSessionKey()
    {
        return sessionKey;
    }
    
    public void setSessionKey(String sessionKey)
    {
        this.sessionKey = sessionKey;
    }
    
    public Boolean getLogoutFlag()
    {
        return logoutFlag;
    }
    
    public void setLogoutFlag(Boolean logoutFlag)
    {
        this.logoutFlag = logoutFlag;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof UserAudit))
        {
            return false;
        }
        return id != null && id.equals(((UserAudit) o).id);
    }
    
    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
    
    @Override
    public String toString()
    {
        return "UserAudit [id=" + id + ", userName=" + userName + ", loginStatus=" + loginStatus + ", lastLogin=" + lastLogin
            + ", lastLogout=" + lastLogout + ", sessionKey=" + sessionKey + ", logoutFlag="
            + logoutFlag + "]";
    }
    
}
