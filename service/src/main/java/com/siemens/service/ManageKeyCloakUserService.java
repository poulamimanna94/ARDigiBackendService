/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */

package com.siemens.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.siemens.constant.AuthoritiesConstants;
import com.siemens.constant.Constants;
import com.siemens.constant.KeyCloakConstants;
import com.siemens.domain.User;
import com.siemens.dto.KeyCloakNewUserDTO;
import com.siemens.dto.KeyCloakRolesDTO;
import com.siemens.dto.KeyCloakUpdateUserDTO;
import com.siemens.exception.KeycloakSecurityException;
import com.siemens.utils.KeycloakSecurityUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManageKeyCloakUserService
{
    @Autowired
    private ManageKeyCloakService keyCloakService;
    
    private final Logger log = LoggerFactory.getLogger(ManageKeyCloakUserService.class);
    
    public List<RoleRepresentation> getUserAssignedRole(Jwt securityContext, String userId)
    {
        org.keycloak.representations.idm.ClientRepresentation clientRepresentation = keyCloakService
            .getKeyCloakClient(securityContext)[0];
        
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        adminUri.append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.USERS)
            .append(Constants.FORWARDSLASH)
            .append(userId)
            .append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.ROLE_MAPPINGS)
            .append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.CLIENT)
            .append(Constants.FORWARDSLASH)
            .append(clientRepresentation.getId());
        
        HttpResponse response;
        
        List<RoleRepresentation> roleRepresentationsList = null;
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            response = keyCloakService.invokeGetKeyCloakApi(builder.build());
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
    
    public void createKeyCloakUser(Jwt securityContext, User newUser, String keyCloakRoleId, String keyCloakRoleName,
        String keyCloakPassword)
        throws KeycloakSecurityException
    {
        org.keycloak.representations.idm.ClientRepresentation clientRepresentation = keyCloakService.getKeyCloakClient(securityContext)[0];
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        KeyCloakNewUserDTO userDTO = new KeyCloakNewUserDTO();
        HttpResponse response;
        
        String keyCloakClientId = clientRepresentation.getId();
        adminUri.append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.USERS);
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpPost post = keyCloakService.getPostKeyCloakApi(builder.build());
            
            Map<String, List<String>> blankAttr = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            
            userDTO.setEnabled(true);
            userDTO.setAttributes(blankAttr);
            userDTO.setEmailVerified(true);
            userDTO.setUsername(newUser.getLogin());
            userDTO.setEmail(newUser.getEmail());
            userDTO.setFirstName(newUser.getFirstName());
            userDTO.setLastName("");
            
            String postUserReqParam = objectMapper.writeValueAsString(userDTO);
            HttpEntity stringEntity = new StringEntity(postUserReqParam, ContentType.APPLICATION_JSON);
            
            post.setEntity(stringEntity);
            
            response = keyCloakService.invokePostKeyCloakApi(post);
            
            if (KeycloakSecurityUtils.isCreatedResponse(response))
            {
                String newUserKeyCloakId = getNewUserKeyCloakId(response.getAllHeaders());
                keyCloakRoleName = keyCloakRoleName.substring(keyCloakRoleName.indexOf(Constants.DASH_SYMBOL) + 1);
                KeyCloakRolesDTO rolesDTO = createNewKeyCloakUserRole(keyCloakRoleId, keyCloakClientId, keyCloakRoleName);
                addKeyCloakUserRole(securityContext, rolesDTO, newUserKeyCloakId, keyCloakClientId);
                setKeyCloakUserPassword(securityContext, newUserKeyCloakId, keyCloakPassword);
            }
            
            if (KeycloakSecurityUtils.isBadRequestResponse(response) || KeycloakSecurityUtils.isUnsupportedMediaTypeResponse(response))
            {
                throw new KeycloakSecurityException(KeyCloakConstants.KEYCLOAK_SECURITY_ERROR_MESSAGE);
            }
        }
        catch (URISyntaxException | JsonProcessingException e)
        {
            log.error(e.getMessage());
        }
    }
    
    public HttpResponse addKeyCloakUserRole(Jwt securityContext, KeyCloakRolesDTO rolesDTO, String newUserKeyCloakId,
        String keyCloakClientId)
    {
        HttpResponse response = null;
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        
        adminUri.append(Constants.FORWARDSLASH).append(AuthoritiesConstants.USERS).append(Constants.FORWARDSLASH)
            .append(newUserKeyCloakId)
            .append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.ROLE_MAPPINGS)
            .append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.CLIENT)
            .append(Constants.FORWARDSLASH)
            .append(keyCloakClientId);
        
        URIBuilder builder;
        
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpPost post = keyCloakService.getPostKeyCloakApi(builder.build());
            ObjectMapper objectMapper = new ObjectMapper();
            
            List<KeyCloakRolesDTO> rolesDTOList = new ArrayList<>();
            rolesDTOList.add(rolesDTO);
            
            String postUserReqParam = objectMapper.writeValueAsString(rolesDTOList);
            HttpEntity stringEntity = new StringEntity(postUserReqParam, ContentType.APPLICATION_JSON);
            
            post.setEntity(stringEntity);
            
            response = keyCloakService.invokePostKeyCloakApi(post);
            
        }
        catch (URISyntaxException | JsonProcessingException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
    
    public KeyCloakRolesDTO createNewKeyCloakUserRole(String keyCloakRoleId, String keyCloakClientId, String keyCloakRoleName)
    {
        KeyCloakRolesDTO rolesDTO = new KeyCloakRolesDTO();
        
        rolesDTO.setClientRole(true);
        rolesDTO.setComposite(false);
        rolesDTO.setId(keyCloakRoleId);
        rolesDTO.setName(keyCloakRoleName);
        rolesDTO.setContainerId(keyCloakClientId);
        
        return rolesDTO;
    }
    
    private String getNewUserKeyCloakId(Header[] respHeaders)
    {
        Map<String, String> headers = new HashMap<>();
        
        for (Header header : respHeaders)
        {
            headers.put(header.getName(), header.getValue());
        }
        String locationUri = headers.get(KeyCloakConstants.LOCATION);
        int strUserIdIndx = locationUri.lastIndexOf(Constants.FORWARDSLASH);
        
        if (strUserIdIndx > 0)
        {
            strUserIdIndx += 1;
        }
        
        return locationUri.substring(strUserIdIndx);
    }
    
    public void deleteKeyCloakUser(Jwt securityContext, String keyCloakUserId)
    {
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.USERS)
            .append(Constants.FORWARDSLASH)
            .append(keyCloakUserId);
        
        URIBuilder builder;
        
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpDelete delete = keyCloakService.getDeleteKeyCloakApi(builder.build());
            
            keyCloakService.invokeDeleteKeyCloakApi(delete);
        }
        catch (URISyntaxException e)
        {
            log.error(e.getMessage());
        }
        
    }
    
    public HttpResponse deleteKeyCloakUserRole(Jwt securityContext, String keyCloakUserId, String keyCloakClientId,
        String keyCloakRoleId, String keyCloakRoleName)
    {
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        HttpResponse response = null;
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.USERS)
            .append(Constants.FORWARDSLASH)
            .append(keyCloakUserId)
            .append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.ROLE_MAPPINGS)
            .append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.CLIENT)
            .append(Constants.FORWARDSLASH)
            .append(keyCloakClientId);
        
        URIBuilder builder;
        
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpDeleteWithBody deleteWithBody = keyCloakService.getDeleteWithBodyKeyCloakApi(builder.build());
            
            List<RoleRepresentation> tempRoleRepresentation = new ArrayList<>();
            
            RoleRepresentation roleRepresentation = new RoleRepresentation();
            roleRepresentation.setId(keyCloakRoleId);
            roleRepresentation.setName(keyCloakRoleName);
            roleRepresentation.setContainerId(keyCloakClientId);
            roleRepresentation.setComposite(false);
            roleRepresentation.setClientRole(true);
            
            tempRoleRepresentation.add(roleRepresentation);
            ObjectMapper objectMapper = new ObjectMapper();
            String postUserReqParam = objectMapper.writeValueAsString(tempRoleRepresentation);
            HttpEntity stringEntity = new StringEntity(postUserReqParam, ContentType.APPLICATION_JSON);
            deleteWithBody.setEntity(stringEntity);
            response = keyCloakService.invokeDeleteWithBodyKeyCloakApi(deleteWithBody);
            
        }
        catch (URISyntaxException | JsonProcessingException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
    
    public void setKeyCloakUserPassword(Jwt securityContext, String keyCloakUserId, String newPassword)
    {
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.USERS)
            .append(Constants.FORWARDSLASH)
            .append(keyCloakUserId)
            .append(Constants.FORWARDSLASH)
            .append(KeyCloakConstants.RESET_PW_PROPERTY);
        
        URIBuilder builder;
        
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpPut put = keyCloakService.getPutkeyCloakApi(builder.build());
            
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(KeyCloakConstants.PW_PROPERTY);
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setValue(newPassword);
            
            ObjectMapper objectMapper = new ObjectMapper();
            
            String postUserReqParam = objectMapper.writeValueAsString(credentialRepresentation);
            HttpEntity stringEntity = new StringEntity(postUserReqParam, ContentType.APPLICATION_JSON);
            
            put.setEntity(stringEntity);
            
            keyCloakService.invokePutKeyCloakApi(put);
        }
        catch (URISyntaxException | JsonProcessingException e)
        {
            log.error(e.getMessage());
        }
        
    }
    
    public void updateKeyCloakUser(Jwt securityContext, String keyCloakUserId, String newEmail, String newPassword,
        String keyCloakRoleId, String keyCloakRoleName)
    {
        org.keycloak.representations.idm.ClientRepresentation clientRepresentation = keyCloakService.getKeyCloakClient(securityContext)[0];
        HttpResponse response;
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        String keyCloakClientId = clientRepresentation.getId();
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.USERS)
            .append(Constants.FORWARDSLASH)
            .append(keyCloakUserId);
        
        URIBuilder builder;
        
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpPut put = keyCloakService.getPutkeyCloakApi(builder.build());
            
            KeyCloakUpdateUserDTO updateUser = new KeyCloakUpdateUserDTO();
            updateUser.setEmail(newEmail);
            updateUser.setId(keyCloakUserId);
            
            ObjectMapper objectMapper = new ObjectMapper();
            
            String postUserReqParam = objectMapper.writeValueAsString(updateUser);
            HttpEntity stringEntity = new StringEntity(postUserReqParam, ContentType.APPLICATION_JSON);
            
            put.setEntity(stringEntity);
            keyCloakService.invokePutKeyCloakApi(put);
            
            if (newPassword != null && !newPassword.isBlank() && !newPassword.isEmpty())
            {
                setKeyCloakUserPassword(securityContext, keyCloakUserId, newPassword);
            }
            
            // Step 1 - Get user Role
            List<RoleRepresentation> roleRepresentations = getUserAssignedRole(securityContext, keyCloakUserId);
            if (!roleRepresentations.isEmpty())
            {
                response = deleteKeyCloakUserRole(securityContext, keyCloakUserId, keyCloakClientId, roleRepresentations.get(0).getId(),
                    roleRepresentations.get(0).getName());
                if (KeycloakSecurityUtils.isNoContentResponse(response))
                {
                    keyCloakRoleName = keyCloakRoleName.substring(keyCloakRoleName.indexOf('_') + 1);
                    KeyCloakRolesDTO rolesDTO = createNewKeyCloakUserRole(keyCloakRoleId, keyCloakClientId, keyCloakRoleName);
                    
                    addKeyCloakUserRole(securityContext, rolesDTO, keyCloakUserId, keyCloakClientId);
                    
                }
            }
            
        }
        catch (URISyntaxException | JsonProcessingException e)
        {
            log.error(e.getMessage());
        }
        
    }
    
    public void logoutKeyCloakUser(Jwt securityContext, String keyCloakUserId)
    {
        HttpResponse response;
        StringBuilder adminUri = keyCloakService.getKeyCloakAdminUri(securityContext);
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.USERS)
            .append(Constants.FORWARDSLASH)
            .append(keyCloakUserId)
            .append(Constants.FORWARDSLASH)
            .append(AuthoritiesConstants.LOGOUT);
        
        URIBuilder builder;
        
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpPost post = keyCloakService.getPostKeyCloakApi(builder.build());
            
            response = keyCloakService.invokePostKeyCloakApi(post);
            
            if (KeycloakSecurityUtils.isBadRequestResponse(response) || KeycloakSecurityUtils.isUnsupportedMediaTypeResponse(response))
            {
                throw new KeycloakSecurityException(KeyCloakConstants.KEYCLOAK_SECURITY_ERROR_MESSAGE);
            }
        }
        catch (URISyntaxException e)
        {
            log.error(e.getMessage());
        }
    }
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
