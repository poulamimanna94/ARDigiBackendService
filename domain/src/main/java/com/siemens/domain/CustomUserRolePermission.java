package com.siemens.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "custom_role_permission")
public class CustomUserRolePermission
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_user_role_permission_sequenceGenerator")
    @SequenceGenerator(name = "custom_user_role_permission_sequenceGenerator")
    @Column(name = "user_id")
    private Long id;
    
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_name", referencedColumnName = "name")
    private RolePermission rolePermission;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "custom_user_permission_id")
    private CustomPermission customPermission;
    
    @OneToOne(cascade = CascadeType.DETACH)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public RolePermission getRolePermission()
    {
        return rolePermission;
    }
    
    public void setRolePermission(RolePermission rolePermission)
    {
        this.rolePermission = rolePermission;
    }
    
    public CustomPermission getCustomPermission()
    {
        return customPermission;
    }
    
    public void setCustomPermission(CustomPermission customPermission)
    {
        this.customPermission = customPermission;
    }
    
    public User getUser()
    {
        return user;
    }
    
    public void setUser(User user)
    {
        this.user = user;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomUserRolePermission that = (CustomUserRolePermission) o;
        return id.equals(that.id) && rolePermission.equals(that.rolePermission) && customPermission.equals(that.customPermission)
            && user.equals(that.user);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(id, rolePermission, customPermission, user);
    }
}
