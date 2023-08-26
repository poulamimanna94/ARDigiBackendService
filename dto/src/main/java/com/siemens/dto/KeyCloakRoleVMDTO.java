package com.siemens.dto;

import com.siemens.domain.Permission;

public class KeyCloakRoleVMDTO extends KeyCloakRolesDTO
{
    
    private Permission permission;
    
    /**
     * @return Permission return the permission
     */
    public Permission getPermission()
    {
        return permission;
    }
    
    /**
     * @param permission the permission to set
     */
    public void setPermission(Permission permission)
    {
        this.permission = permission;
    }
    
}
