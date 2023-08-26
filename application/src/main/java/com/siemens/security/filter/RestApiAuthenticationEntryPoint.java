/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion  
 * 
 */

package com.siemens.security.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.siemens.constant.Constants;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.config.ApiFilterResponse;

public class RestApiAuthenticationEntryPoint implements AuthenticationEntryPoint
{
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
        throws IOException, ServletException
    {
        ApiFilterResponse filterResponse = new ApiFilterResponse(401, Constants.UNAUTHORISED);
        filterResponse.setMessage(Constants.UNAUTHORISED);
        response.setContentType(Constants.APPLICATION_JSON_CONTENT_TYPE);
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, filterResponse);
        out.flush();
    }
    
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
