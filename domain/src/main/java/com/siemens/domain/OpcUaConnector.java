/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion  
 * 
 */

package com.siemens.domain;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.siemens.constant.Constants;

@Component
public class OpcUaConnector
{
    @JsonProperty
    private int opcPortNumber;
    
    @JsonProperty
    private String opcServerAddress;
    
    @JsonProperty
    private String opcServerName;
    
    @JsonProperty
    private String opcCertificate;
    
    @JsonProperty
    private String opcSecurity;
    
    @JsonProperty
    private String opcMessagePolicy;
    
    @JsonProperty
    private String opcUserName;
    
    @JsonProperty
    private String opcPassword;
    
    @JsonProperty
    @JsonIgnore
    private StringBuilder endPoint;
    
    public OpcUaConnector()
    {
        super();
    }
    
    public StringBuilder getEndPoint()
    {
        return endPoint;
    }
    
    public void setEndPoint(StringBuilder endPoint)
    {
        this.endPoint = endPoint;
    }
    
    // create end point
    public StringBuilder formEndPoint(StringBuilder opcTcp, String opcServerAddress, Integer opcPortNumber, String opcServerName)
    {
        
        this.endPoint = opcTcp
            .append(opcServerAddress)
            .append(":")
            .append(opcPortNumber)
            .append(Constants.FORWARDSLASH)
            .append(opcServerName);
        return endPoint;
    }
    
    public String getOpcCertificate()
    {
        return opcCertificate;
    }
    
    public String getOpcMessagePolicy()
    {
        return opcMessagePolicy;
    }
    
    public String getOpcPassword()
    {
        return opcPassword;
    }
    
    public int getOpcPortNumber()
    {
        return opcPortNumber;
    }
    
    public String getOpcSecurity()
    {
        return opcSecurity;
    }
    
    public String getOpcServerAddress()
    {
        return opcServerAddress;
    }
    
    public String getOpcServerName()
    {
        return opcServerName;
    }
    
    public String getOpcUserName()
    {
        return opcUserName;
    }
    
    public void setOpcCertificate(String opcCertificate)
    {
        this.opcCertificate = opcCertificate;
    }
    
    public void setOpcMessagePolicy(String opcMessagePolicy)
    {
        this.opcMessagePolicy = opcMessagePolicy;
    }
    
    public void setOpcPassword(String opcPassword)
    {
        this.opcPassword = opcPassword;
    }
    
    public void setOpcPortNumber(int opcPortNumber)
    {
        this.opcPortNumber = opcPortNumber;
    }
    
    public void setOpcSecurity(String opcSecurity)
    {
        this.opcSecurity = opcSecurity;
    }
    
    public void setOpcServerAddress(String opcServerAddress)
    {
        this.opcServerAddress = opcServerAddress;
    }
    
    public void setOpcServerName(String opcServerName)
    {
        this.opcServerName = opcServerName;
    }
    
    public void setOpcUserName(String opcUserName)
    {
        this.opcUserName = opcUserName;
    }
    
    @Override
    public String toString()
    {
        return "OpcUaConnector [opcPortNumber=" + opcPortNumber + ", opcServerAddress=" + opcServerAddress
            + ", opcServerName=" + opcServerName + ", opcCertificate=" + opcCertificate + ", opcSecurity="
            + opcSecurity + ", opcMessagePolicy=" + opcMessagePolicy + ", opcUserName=" + opcUserName
            + ", opcPassword=" + opcPassword + "]";
    }
    
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
