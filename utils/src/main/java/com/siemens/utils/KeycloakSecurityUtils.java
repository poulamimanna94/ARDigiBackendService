package com.siemens.utils;

import com.siemens.constant.Constants;
import com.siemens.domain.User;
import com.siemens.dto.UserDTO;
import org.apache.http.HttpResponse;

public class KeycloakSecurityUtils
{
    private KeycloakSecurityUtils()
    {
        // intentionally created private constructor to hide the implicit public one
    }
    
    public static boolean isUnsupportedMediaTypeResponse(HttpResponse response)
    {
        return getStatusCode(response) == 415;
    }
    
    public static boolean isBadRequestResponse(HttpResponse response)
    {
        return getStatusCode(response) == 400;
    }
    
    public static boolean isCreatedResponse(HttpResponse response)
    {
        return getStatusCode(response) == 201;
    }
    
    public static boolean isNoContentResponse(HttpResponse response)
    {
        return getStatusCode(response) == 204;
    }
    
    private static int getStatusCode(HttpResponse response)
    {
        return response.getStatusLine().getStatusCode();
    }
    
    public static String getAuthName(String authName)
    {
        return (!authName.contains(Constants.ROLE)) ? Constants.ROLE + authName : "";
    }
    
    public static User createNewUser(UserDTO userDTO)
    {
        User newUser = new User();
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null)
        {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        return newUser;
    }
    
}
