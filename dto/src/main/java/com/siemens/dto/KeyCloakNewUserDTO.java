/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */

package com.siemens.dto;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyCloakNewUserDTO
{
    private String username;
    private boolean enabled;
    
    private boolean emailVerified;
    
    private String firstName;
    private String lastName;
    private String email;
    
    private Map<String, List<String>> attributes;
    
    @Override
    public String toString()
    {
        return "KeyCloakUserDTO{" +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", emailVerified='" + emailVerified + '\'' +
            "}";
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
    
    public Map<String, List<String>> getAttributes()
    {
        return attributes;
    }
    
    public void setAttributes(Map<String, List<String>> attributes)
    {
        this.attributes = attributes;
    }
    
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
