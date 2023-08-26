package com.siemens.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * DTO for User Co-ordinates/ Operator Position
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLocationDTO
{
    private Double x;
    private Double y;
    private Double z;
    private String login; // username
    
    public Double getX()
    {
        return x;
    }
    
    public void setX(Double x)
    {
        this.x = x;
    }
    
    public Double getY()
    {
        return y;
    }
    
    public void setY(Double y)
    {
        this.y = y;
    }
    
    public Double getZ()
    {
        return z;
    }
    
    public void setZ(Double z)
    {
        this.z = z;
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public void setLogin(String login)
    {
        this.login = login;
    }
    
    @Override
    public String toString()
    {
        return "x:" + x + ", y:" + y + ", z:" + z;
    }
    
}
