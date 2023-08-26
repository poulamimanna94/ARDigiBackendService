package com.siemens.events;

import org.springframework.context.ApplicationEvent;

public class OnUserLoginEvent extends ApplicationEvent
{
    private String someData;
    
    public OnUserLoginEvent(Object source, String someData)
    {
        super(source);
        this.someData = someData;
    }
    
    public String getSomeData()
    {
        return someData;
    }
    
}
