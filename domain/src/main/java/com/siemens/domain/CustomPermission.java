package com.siemens.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "custom_permission")
public class CustomPermission
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_user_permission_sequenceGenerator")
    @SequenceGenerator(name = "custom_user_permission_sequenceGenerator")
    private Long id;
    
    @NotNull
    @Column(name = "monitor_dashboard")
    private Boolean monitorDashboard;
    
    @NotNull
    @Column(name = "opc_ua_server_config")
    private Boolean opcUaServerConfig;
    
    @NotNull
    @Column(name = "archive_config")
    private Boolean archiveConfig;
    
    @NotNull
    @Column(name = "connectivity_config")
    private Boolean connectivityConfig;
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public Boolean getMonitorDashboard()
    {
        return monitorDashboard;
    }
    
    public void setMonitorDashboard(Boolean monitorDashboard)
    {
        this.monitorDashboard = monitorDashboard;
    }
    
    public Boolean getOpcUaServerConfig()
    {
        return opcUaServerConfig;
    }
    
    public void setOpcUaServerConfig(Boolean opcUaServerConfig)
    {
        this.opcUaServerConfig = opcUaServerConfig;
    }
    
    public Boolean getArchiveConfig()
    {
        return archiveConfig;
    }
    
    public void setArchiveConfig(Boolean archiveConfig)
    {
        this.archiveConfig = archiveConfig;
    }
    
    public Boolean getConnectivityConfig()
    {
        return connectivityConfig;
    }
    
    public void setConnectivityConfig(Boolean connectivityConfig)
    {
        this.connectivityConfig = connectivityConfig;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CustomPermission that = (CustomPermission) o;
        return Objects.equals(id, that.id) && Objects.equals(monitorDashboard, that.monitorDashboard)
            && Objects.equals(opcUaServerConfig, that.opcUaServerConfig) && Objects.equals(archiveConfig, that.archiveConfig)
            && Objects.equals(connectivityConfig, that.connectivityConfig);
    }
    
    @Override
    public int hashCode()
    {
        return Objects.hash(id, monitorDashboard, opcUaServerConfig, archiveConfig, connectivityConfig);
    }
    
}
