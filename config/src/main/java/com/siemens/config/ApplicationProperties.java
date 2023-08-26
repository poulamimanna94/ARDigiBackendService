package com.siemens.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Properties specific to DVPI Api.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@Configuration
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties
{
    private List<String> audience = new ArrayList<>();
    
    private final CorsConfiguration cors = new CorsConfiguration();
    
    private final ClientApp clientApp = new ClientApp();
    
    private int userLimit = 0;
    
    private String airByteUrl = "";
    
    public List<String> getAudience()
    {
        return Collections.unmodifiableList(audience);
    }
    
    public void setAudience(@NotNull List<String> audience)
    {
        this.audience.addAll(audience);
    }
    
    public CorsConfiguration getCors()
    {
        return cors;
    }
    
    public ClientApp getClientApp()
    {
        return clientApp;
    }
    
    public static class ClientApp
    {
        
        private String name = "";
        
        public String getName()
        {
            return name;
        }
        
        public void setName(String name)
        {
            this.name = name;
        }
    }
    
    public int getUserLimit()
    {
        return userLimit;
    }
    
    public void setUserLimit(int userLimit)
    {
        this.userLimit = userLimit;
    }
    
    public String getAirByteUrl()
    {
        return airByteUrl;
    }
    
    public void setAirByteUrl(String airByteUrl)
    {
        this.airByteUrl = airByteUrl;
    }
    
}
