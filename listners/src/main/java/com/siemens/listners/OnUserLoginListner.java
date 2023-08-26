package com.siemens.listners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.siemens.events.OnUserLoginEvent;

@Component
public class OnUserLoginListner implements ApplicationListener<OnUserLoginEvent>
{
    private static final Logger log = LoggerFactory.getLogger(OnUserLoginListner.class);
        
    @Async
    @Override
    public void onApplicationEvent(OnUserLoginEvent event)
    {
        log.info("++++ In login event listner +++++++");
        
        String userNameFromEvent = event.getSomeData();
        log.info("userNameFromEvent: {}", userNameFromEvent);
        
    }
    
}
