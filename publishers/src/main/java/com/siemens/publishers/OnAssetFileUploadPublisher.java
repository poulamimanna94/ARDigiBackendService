package com.siemens.publishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.siemens.events.OnAssetFileUploadEvent;

@Component
public class OnAssetFileUploadPublisher
{
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    private static final Logger log = LoggerFactory.getLogger(OnAssetFileUploadPublisher.class);
    
    public void publishOnUserLogin(String filePath)
    {
        log.info("++++++++ In OnAssetFileUploadPublisher ++++++++");
        OnAssetFileUploadEvent event = new OnAssetFileUploadEvent(this, filePath);
        applicationEventPublisher.publishEvent(event);
    }
    
}
