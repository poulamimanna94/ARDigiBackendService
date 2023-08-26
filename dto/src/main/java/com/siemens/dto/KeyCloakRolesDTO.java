/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion  
 * 
 */

package com.siemens.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KeyCloakRolesDTO
{
    private String id;
    private String name;
    private Boolean composite;
    private Boolean clientRole;
    private String containerId;
    
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Boolean getComposite()
    {
        return composite;
    }
    
    public void setComposite(Boolean composite)
    {
        this.composite = composite;
    }
    
    public Boolean getClientRole()
    {
        return clientRole;
    }
    
    public void setClientRole(Boolean clientRole)
    {
        this.clientRole = clientRole;
    }
    
    public String getContainerId()
    {
        return containerId;
    }
    
    public void setContainerId(String containerId)
    {
        this.containerId = containerId;
    }
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
