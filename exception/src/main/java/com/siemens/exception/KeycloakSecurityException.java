package com.siemens.exception;

public class KeycloakSecurityException extends RuntimeException
{
    
    public KeycloakSecurityException(String message)
    {
        super(message);
    }
    
    public KeycloakSecurityException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
