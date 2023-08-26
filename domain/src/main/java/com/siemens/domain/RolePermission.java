package com.siemens.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "role_permission")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RolePermission implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "user_permission_id")
    private Permission permission;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public Permission getPermission()
    {
        return permission;
    }
    
    public void setPermission(Permission permission)
    {
        this.permission = permission;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof RolePermission))
        {
            return false;
        }
        return Objects.equals(name, ((RolePermission) o).name);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hashCode(name);
    }
    
    // prettier-ignore
    @Override
    public String toString()
    {
        return "Authority{" +
            "name='" + name + '\'' +
            "}";
    }
}
