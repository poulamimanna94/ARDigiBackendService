package com.siemens.events;

import org.springframework.context.ApplicationEvent;

public class OnAssetFileUploadEvent extends ApplicationEvent
{
    private String someData;
    
    public OnAssetFileUploadEvent(Object source, String someData)
    {
        super(source);
        this.someData = someData;
    }
    
    public String getSomeData()
    {
        return someData;
    }
    
}
