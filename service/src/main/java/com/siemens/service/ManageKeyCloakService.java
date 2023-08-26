/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */

package com.siemens.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.siemens.config.KeyCloakConfigProperties;
import com.siemens.constant.AuthoritiesConstants;
import com.siemens.constant.Constants;
import com.siemens.constant.KeyCloakConstants;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.keycloak.authorization.client.AuthorizationDeniedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.representation.ServerConfiguration;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

@Service
public class ManageKeyCloakService
{
    @Autowired
    private KeyCloakConfigProperties keyCloakConfigProperties;
    
    private final Logger log = LoggerFactory.getLogger(ManageKeyCloakService.class);
    
    private AuthzClient authzClient;
    
    private String rpt;
    
    private ServerConfiguration configuration;
    
    @PostConstruct
    public void initManageKeyCloakService()
    {
        authzClient = AuthzClient.create(keyCloakConfigProperties);
    }
    
    public AuthorizationResponse AuthenticateViaKeyCloak(String userName, String password)
    {
        // create an authorization request
        AuthorizationRequest request = new AuthorizationRequest();
        AuthorizationResponse response = null;
        
        try
        {
            // send the entitlement request to the server in order to
            // obtain an RPT with all permissions granted to the user
            response = authzClient.authorization(userName, password).authorize(request);
            
            // Current Requesting Party Token
            rpt = response.getToken();
            
            configuration = authzClient.getServerConfiguration();
            
            ServiceStaticData.authorization = KeyCloakConstants.BEARER + rpt;
            
            log.debug("Current token info: {}", response.getToken());
        }
        catch (AuthorizationDeniedException e)
        {
            log.info("Keycloak manage service: Access denied - {}", e.getMessage());
        }
        catch (Exception e)
        {
            log.info("Keycloak manage service: Access denied generalize - {}", e.getMessage());
        }
        
        return response;
    }
    
    private void setAuthorization(String rpt)
    {
        ServiceStaticData.authorization = KeyCloakConstants.BEARER + rpt;
    }
    
    public org.keycloak.representations.idm.ClientRepresentation[] getKeyCloakClient(Jwt securityContext)
    {
        String clientName = keyCloakConfigProperties.getResource();
        StringBuilder adminUri = getKeyCloakAdminUri(securityContext);
        adminUri.append(Constants.FORWARDSLASH).append(KeyCloakConstants.CLIENTS);
        
        org.keycloak.representations.idm.ClientRepresentation[] clientRepresentation = null;
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            
            builder.setParameter(KeyCloakConstants.CLIENT_ID, clientName);
            builder.setParameter(Constants.SEARCH, Constants.TRUE_VALUE);
            
            HttpResponse response = invokeGetKeyCloakApi(builder.build());
            
            InputStream reqContent = response.getEntity().getContent();
            
            ObjectMapper mapper = new ObjectMapper();
            
            clientRepresentation = mapper.readValue(reqContent, org.keycloak.representations.idm.ClientRepresentation[].class);
            
            log.debug("Current cliend Id : {} ", clientRepresentation[0].getClientId());
        }
        catch (URISyntaxException | UnsupportedOperationException | IOException e)
        {
            log.debug(e.getMessage());
            
        }
        
        return clientRepresentation;
    }
    
    public List<RoleRepresentation> getKeyCloakClientRoles(Jwt securityContext)
    {
        org.keycloak.representations.idm.ClientRepresentation clientRepresentation = this.getKeyCloakClient(securityContext)[0];
        
        StringBuilder adminUri = getKeyCloakAdminUri(securityContext);
        adminUri.append(Constants.FORWARDSLASH)
            .append(KeyCloakConstants.CLIENTS)
            .append(Constants.FORWARDSLASH)
            .append(clientRepresentation.getId())
            .append(Constants.FORWARDSLASH)
            .append(KeyCloakConstants.ROLES);
        
        List<RoleRepresentation> roleRepresentationsList = null;
        
        HttpResponse response;
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            
            response = invokeGetKeyCloakApi(builder.build());
            
            InputStream reqContent;
            
            reqContent = response.getEntity().getContent();
            
            ObjectMapper mapper = new ObjectMapper();
            
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, RoleRepresentation.class);
            roleRepresentationsList = mapper.readValue(reqContent, type);
            
        }
        catch (URISyntaxException | UnsupportedOperationException | IOException e)
        {
            log.debug(e.getMessage());
            
        }
        
        return roleRepresentationsList;
    }
    
    public List<UserRepresentation> getKeyCloakUsers(Jwt securityContext)
    {
        StringBuilder adminUri = getKeyCloakAdminUri(securityContext);
        
        adminUri.append(Constants.FORWARDSLASH);
        adminUri.append(KeyCloakConstants.USERS);
        
        List<UserRepresentation> userRepresentationsList = null;
        
        HttpResponse response;
        
        URIBuilder builder;
        
        try
        {
            builder = new URIBuilder(adminUri.toString());
            builder.setParameter(KeyCloakConstants.BRIEF_REPRESENTATION, Constants.TRUE_VALUE);
            
            response = invokeGetKeyCloakApi(builder.build());
            
            InputStream reqContent = response.getEntity().getContent();
            
            ObjectMapper mapper = new ObjectMapper();
            
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, UserRepresentation.class);
            userRepresentationsList = mapper.readValue(reqContent, type);
            
        }
        catch (URISyntaxException | UnsupportedOperationException | IOException e)
        {
            log.debug(e.getMessage());
            
        }
        
        return userRepresentationsList;
    }
    
    public StringBuilder getKeyCloakAdminUri(Jwt securityContext)
    {
        String adminIssuerUri;
        
        if (Objects.isNull(configuration))
        {
            adminIssuerUri = securityContext.getIssuer().toString();
        }
        else
        {
            adminIssuerUri = configuration.getIssuer();
        }
        setAuthorization(securityContext.getTokenValue());
        
        String realms = KeyCloakConstants.URI_REALMS;
        String admin = KeyCloakConstants.URI_ADMIN;
        
        int pos = adminIssuerUri.indexOf(realms);
        
        // embedd /admin
        
        StringBuilder adminUri = new StringBuilder(adminIssuerUri.substring(0, pos));
        adminUri.append(admin);
        adminUri.append(adminIssuerUri.substring(pos));
        
        return adminUri;
    }
    
    public HttpResponse invokeGetKeyCloakApi(URI uri)
    {
        HttpResponse response = null;
        
        try
        {
            HttpClient client = HttpClientBuilder.create().build();
            
            HttpGet get = new HttpGet(uri);
            get.setHeader(AuthoritiesConstants.USER_AGENT,
                KeyCloakConstants.USER_AGENTS);
            get.setHeader(KeyCloakConstants.AUTHORIZATION, ServiceStaticData.authorization);
            
            response = client.execute(get);
        }
        catch (IOException e)
        {
            log.debug(e.getMessage());
            
        }
        
        return response;
    }
    
    public HttpPost getPostKeyCloakApi(URI uri)
    {
        
        HttpPost post = new HttpPost(uri);
        post.setHeader(AuthoritiesConstants.USER_AGENT,
            AuthoritiesConstants.MOZILLA_WINDOWS_CHROME_SAFARI);
        post.setHeader(AuthoritiesConstants.AUTHORIZATION, ServiceStaticData.authorization);
        
        return post;
    }
    
    public HttpDelete getDeleteKeyCloakApi(URI uri)
    {
        
        HttpDelete delete = new HttpDelete(uri);
        delete.setHeader(AuthoritiesConstants.USER_AGENT,
            AuthoritiesConstants.MOZILLA_WINDOWS_CHROME_SAFARI);
        delete.setHeader(AuthoritiesConstants.AUTHORIZATION, ServiceStaticData.authorization);
        
        return delete;
    }
    
    public HttpDeleteWithBody getDeleteWithBodyKeyCloakApi(URI uri)
    {
        
        HttpDeleteWithBody delete = new HttpDeleteWithBody(uri);
        delete.setHeader(AuthoritiesConstants.USER_AGENT,
            AuthoritiesConstants.MOZILLA_WINDOWS_CHROME_SAFARI);
        delete.setHeader(AuthoritiesConstants.AUTHORIZATION, ServiceStaticData.authorization);
        
        return delete;
    }
    
    public HttpPut getPutkeyCloakApi(URI uri)
    {
        HttpPut put = new HttpPut(uri);
        put.setHeader(AuthoritiesConstants.USER_AGENT,
            AuthoritiesConstants.MOZILLA_WINDOWS_CHROME_SAFARI);
        put.setHeader(AuthoritiesConstants.AUTHORIZATION, ServiceStaticData.authorization);
        
        return put;
    }
    
    public HttpResponse invokePostKeyCloakApi(HttpPost post)
    {
        HttpResponse response = null;
        HttpClient client = HttpClientBuilder.create().build();
        
        try
        {
            response = client.execute(post);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
    
    public HttpResponse invokeDeleteKeyCloakApi(HttpDelete delete)
    {
        HttpResponse response = null;
        HttpClient client = HttpClientBuilder.create().build();
        
        try
        {
            response = client.execute(delete);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
    
    public HttpResponse invokeDeleteWithBodyKeyCloakApi(HttpDeleteWithBody delete)
    {
        HttpResponse response = null;
        HttpClient client = HttpClientBuilder.create().build();
        
        try
        {
            response = client.execute(delete);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
    
    public HttpResponse invokePutKeyCloakApi(HttpPut put)
    {
        HttpResponse response = null;
        HttpClient client = HttpClientBuilder.create().build();
        
        try
        {
            response = client.execute(put);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
