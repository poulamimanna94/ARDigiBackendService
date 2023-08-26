package com.siemens.domain;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Table(name = "user_permission")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Permission implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_permission_sequenceGenerator")
    @SequenceGenerator(name = "user_permission_sequenceGenerator")
    private Long id;
    
    @NotNull
    @Column(name = "asset_management")
    private Boolean assetManagement;
    
    @NotNull
    @Column(name = "user_management")
    private Boolean userManagement;
    
    @NotNull
    @Column(name = "opc_ua_server_config")
    private Boolean opcUaServerConfig;
    
    @NotNull
    @Column(name = "kpi_dashboard")
    private Boolean kpiDashboard;
    
    @NotNull
    @Column(name = "plant_all_features")
    private Boolean plantAllFeatures;
    
    @OneToMany(mappedBy = "permission")
    private Set<RolePermission> authority;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Boolean getAssetManagement()
    {
        return assetManagement;
    }
    
    public void setAssetManagement(Boolean assetManagement)
    {
        this.assetManagement = assetManagement;
    }
    
    public Boolean getUserManagement()
    {
        return userManagement;
    }
    
    public void setUserManagement(Boolean userManagement)
    {
        this.userManagement = userManagement;
    }
    
    public Boolean getOpcUaServerConfig()
    {
        return opcUaServerConfig;
    }
    
    public void setOpcUaServerConfig(Boolean opcUaServerConfig)
    {
        this.opcUaServerConfig = opcUaServerConfig;
    }
    
    public Boolean getKpiDashboard()
    {
        return kpiDashboard;
    }
    
    public void setKpiDashboard(Boolean kpiDashboard)
    {
        this.kpiDashboard = kpiDashboard;
    }
    
    public Boolean getPlantAllFeatures()
    {
        return plantAllFeatures;
    }
    
    public void setPlantAllFeatures(Boolean plantAllFeatures)
    {
        this.plantAllFeatures = plantAllFeatures;
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(assetManagement, authority, id, kpiDashboard, opcUaServerConfig, plantAllFeatures, userManagement);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Permission other = (Permission) obj;
        return Objects.equals(assetManagement, other.assetManagement) && Objects.equals(authority, other.authority)
            && Objects.equals(id, other.id) && Objects.equals(kpiDashboard, other.kpiDashboard)
            && Objects.equals(opcUaServerConfig, other.opcUaServerConfig) && Objects.equals(plantAllFeatures, other.plantAllFeatures)
            && Objects.equals(userManagement, other.userManagement);
    }
    
}
