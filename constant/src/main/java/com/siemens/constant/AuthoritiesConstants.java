package com.siemens.constant;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants
{
    private AuthoritiesConstants()
    {
        // hide the implicit one
        
    }
    
    public static final String ADMIN = "ROLE_ADMIN";
    public static final String ADMIN_ROLE = "\"" + AuthoritiesConstants.ADMIN + "\"";
    public static final String USER = "ROLE_USER";
    public static final String USER_ROLE = "\"" + AuthoritiesConstants.USER + "\"";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ANONYMOUS_ROLE = "\"" + AuthoritiesConstants.ANONYMOUS + "\"";
    
    public static final String PREFERRED_USERNAME = "preferred_username";
    
    public static final String SIE_ADMIN = "sie_admin";
    public static final String SIE_ADMIN_ROLE = "\"" + AuthoritiesConstants.SIE_ADMIN + "\"";
    
    public static final String USER_AGENT = "User-Agent";
    
    public static final String AUTHORIZATION = "Authorization";
    
    public static final String MOZILLA_WINDOWS_CHROME_SAFARI = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    
    public static final String USERS = "users";
    
    public static final String ROLE_MAPPINGS = "role-mappings";
    
    public static final String CLIENT = "clients";
    public static final String LOGOUT = "logout";
    
}
