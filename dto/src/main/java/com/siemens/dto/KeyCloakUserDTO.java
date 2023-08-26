/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */

package com.siemens.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyCloakUserDTO
{
    private String id;
    private String createdTimestamp;
    private String username;
    private boolean enabled;
    private boolean totp;
    
    private boolean emailVerified;
    
    private String firstName;
    private String lastName;
    private String email;
    
    private String notBefore;
    
    private List<KeyCloakRoleVMDTO> roles;
    
    private String department;
    
    @Override
    public String toString()
    {
        return "KeyCloakUserDTO [id=" + id + ", createdTimestamp=" + createdTimestamp + ", username=" + username + ", enabled=" + enabled
            + ", totp=" + totp + ", emailVerified=" + emailVerified + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
            + email + ", notBefore=" + notBefore + ", roles=" + roles + ", department=" + department + "]";
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getCreatedTimestamp()
    {
        return createdTimestamp;
    }
    
    public void setCreatedTimestamp(String createdTimestamp)
    {
        this.createdTimestamp = createdTimestamp;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public boolean isEnabled()
    {
        return enabled;
    }
    
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
    
    public boolean isTotp()
    {
        return totp;
    }
    
    public void setTotp(boolean totp)
    {
        this.totp = totp;
    }
    
    public boolean isEmailVerified()
    {
        return emailVerified;
    }
    
    public void setEmailVerified(boolean emailVerified)
    {
        this.emailVerified = emailVerified;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getNotBefore()
    {
        return notBefore;
    }
    
    public void setNotBefore(String notBefore)
    {
        this.notBefore = notBefore;
    }
    
    public List<KeyCloakRoleVMDTO> getRoles()
    {
        return roles;
    }
    
    public void setRoles(List<KeyCloakRoleVMDTO> roles)
    {
        this.roles = roles;
    }
    
    public String getDepartment()
    {
        return department;
    }
    
    public void setDepartment(String department)
    {
        this.department = department;
    }
    
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
