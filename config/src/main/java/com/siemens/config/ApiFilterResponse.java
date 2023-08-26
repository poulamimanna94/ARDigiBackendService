/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion  
 * 
 */

package com.siemens.config;

public class ApiFilterResponse
{
    private int status;
    private String message;
    private Object result;
    
    public ApiFilterResponse(int status, String message)
    {
        this.status = status;
        this.message = message;
    }
    
    public ApiFilterResponse(int status, String message, Object result)
    {
        this.status = status;
        this.message = message;
        this.result = result;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public Object getResult()
    {
        return result;
    }
    
    public int getStatus()
    {
        return status;
    }
    
    public void setMessage(String message)
    {
        this.message = message;
    }
    
    public void setResult(Object result)
    {
        this.result = result;
    }
    
    public void setStatus(int status)
    {
        this.status = status;
    }
    
    @Override
    public String toString()
    {
        return "ApiFilterResponse [status=" + status + ", message=" + message + "]";
    }
    
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
