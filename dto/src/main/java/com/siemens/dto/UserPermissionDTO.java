package com.siemens.dto;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.siemens.constant.Constants;
import com.siemens.domain.CustomPermission;
import com.siemens.domain.CustomUserRolePermission;
import com.siemens.domain.User;

public class UserPermissionDTO
{
    private Long id;
    
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;
    private Set<String> authorities = new HashSet<>();
    private Boolean customPermission;
    private Boolean connectivityConfigPermission = false;
    private Boolean archiveConfigPermission = false;
    private Boolean opcUaServerConfigPermission = false;
    private Boolean monitorDashboardPermission = false;
    
    public UserPermissionDTO()
    {
        // Empty constructor needed for Jackson.
    }
    
    public UserPermissionDTO(User user)
    {
        this.id = user.getId();
        this.login = user.getLogin();
        CustomUserRolePermission customUserRolePermission = user.getCustomUserRolePermission();
        if (Objects.nonNull(customUserRolePermission))
        {
            if (Objects.nonNull(customUserRolePermission.getUser()) && Objects.nonNull(customUserRolePermission.getCustomPermission()))
            {
                CustomPermission customPermission = customUserRolePermission.getCustomPermission();
                this.archiveConfigPermission = customPermission.getArchiveConfig();
                this.opcUaServerConfigPermission = customPermission.getOpcUaServerConfig();
                this.monitorDashboardPermission = customPermission.getMonitorDashboard();
                this.connectivityConfigPermission = customPermission.getConnectivityConfig();
                this.authorities.add(customUserRolePermission.getRolePermission().getName());
            }
        }
        this.customPermission = Objects.nonNull(customUserRolePermission);
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public void setLogin(String login)
    {
        this.login = login;
    }
    
    public Boolean getCustomPermission()
    {
        return customPermission;
    }
    
    public void setCustomPermission(Boolean customPermission)
    {
        this.customPermission = customPermission;
    }
    
    public Boolean getConnectivityConfigPermission()
    {
        return connectivityConfigPermission;
    }
    
    public void setConnectivityConfigPermission(Boolean connectivityConfigPermission)
    {
        this.connectivityConfigPermission = connectivityConfigPermission;
    }
    
    public Boolean getArchiveConfigPermission()
    {
        return archiveConfigPermission;
    }
    
    public void setArchiveConfigPermission(Boolean archiveConfigPermission)
    {
        this.archiveConfigPermission = archiveConfigPermission;
    }
    
    public Boolean getOpcUaServerConfigPermission()
    {
        return opcUaServerConfigPermission;
    }
    
    public void setOpcUaServerConfigPermission(Boolean opcUaServerConfigPermission)
    {
        this.opcUaServerConfigPermission = opcUaServerConfigPermission;
    }
    
    public Boolean getMonitorDashboardPermission()
    {
        return monitorDashboardPermission;
    }
    
    public void setMonitorDashboardPermission(Boolean monitorDashboardPermission)
    {
        this.monitorDashboardPermission = monitorDashboardPermission;
    }
    
    public Set<String> getAuthorities()
    {
        return authorities;
    }
    
    public void setAuthorities(Set<String> authorities)
    {
        this.authorities = authorities;
    }
}
