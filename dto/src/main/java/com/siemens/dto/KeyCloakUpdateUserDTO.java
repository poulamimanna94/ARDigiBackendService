/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */

package com.siemens.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyCloakUpdateUserDTO
{
    private String id;
    
    private String email;
    
    @Override
    public String toString()
    {
        return "KeyCloakUserDTO{" +
            "id='" + id + '\'' +
            "}";
    }
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
