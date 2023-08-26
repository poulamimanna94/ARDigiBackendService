package com.siemens.security.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.config.ApiFilterResponse;
import com.siemens.constant.Constants;
import com.siemens.service.UserAuditService;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class RequestResponseLoggingFilter implements Filter
{
    
    private final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);
    
    @Autowired
    private UserAuditService usrAudService;
    
    String token = "";
    
    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain) throws IOException, ServletException
    {
        log.info("inside RequestResponseLoggingFilter  --- doFilter()");
        
        HttpServletRequest req = (HttpServletRequest) request;// req uri -> api
        
        String path = req.getRequestURI();
        req.getUserPrincipal();
        if ("/api/user-auth/authorize".equals(path) || path.contains("/api/i/system-config/logo/download"))
        {
            log.info("Request Path: {}", path);
            
            chain.doFilter(request, response);
            return;
        }
        
        token = getJwtToken(req).getTokenValue().toString();
        if (Objects.nonNull(token))
        {
            log.info("Current Token NOT NUll !");
            long exp = usrAudService.getExpInSessionKey(token);
            
            log.info("Current time millis: {}", System.currentTimeMillis() / 1000);
            log.info("Token expiry time: {}", exp);
            
            String latestLoginStatus = usrAudService.findLoginStatusForRequestFilter(token);
            log.info("latestLoginStatus: {}", latestLoginStatus);
            
            if (exp > (System.currentTimeMillis() / 1000))// if token not expired
            {
                log.info("Token Not Expired!");
                
                if (Objects.nonNull(latestLoginStatus) &&
                    (Constants.LOGIN_INACTIVE_BY_SYS.equalsIgnoreCase(latestLoginStatus)
                        || Constants.LOGIN_INACTIVE.equalsIgnoreCase(latestLoginStatus)))
                {
                    log.info("latestLoginStatus: {}", latestLoginStatus); // user, session key ->status inactive/by sys
                    
                    ApiFilterResponse filterResponse = new ApiFilterResponse(401, Constants.UNAUTHORISED);
                    filterResponse.setMessage(Constants.UNAUTHORISED);
                    response.setContentType(Constants.APPLICATION_JSON_CONTENT_TYPE);
                    OutputStream out = response.getOutputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.writeValue(out, filterResponse);
                    out.flush();
                }
            }
        }
        else
        {
            log.info("Current Token NUll: {}", token);
            throw new NullPointerException("Current token is null, please authenticate first !!");
        }
        chain.doFilter(request, response);
    }
    
    private JwtAuthenticationToken getSecurityContext(HttpServletRequest req)
    {
        return (JwtAuthenticationToken) req.getUserPrincipal();
    }
    
    private Jwt getJwtToken(HttpServletRequest req)
    {
        return (Jwt) getSecurityContext(req).getToken();
    }
    // other methods
}