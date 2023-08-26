package com.siemens.publishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.siemens.events.OnUserLoginEvent;

@Component
public class OnUserLoginPublisher
{
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    private static final Logger log = LoggerFactory.getLogger(OnUserLoginPublisher.class);
    
    public void publishOnUserLogin(String userName)
    {
        log.info("++++++++ In OnUserLoginPublisher ++++++++");
        OnUserLoginEvent loginEvent = new OnUserLoginEvent(this, userName);
        applicationEventPublisher.publishEvent(loginEvent);
    }
    
}
