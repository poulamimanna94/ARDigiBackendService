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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.config.ApiFilterResponse;

public class RestApiAccessDeniedHandler implements AccessDeniedHandler
{
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
        throws IOException, ServletException
    {
        ApiFilterResponse filterResponse = new ApiFilterResponse(403, Constants.ACCESS_DENIED_RESPONSE_MESSAGE);
        filterResponse.setMessage(Constants.ACCESS_DENIED_RESPONSE_MESSAGE);
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
