package com.siemens.listners;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.siemens.events.OnAssetFileUploadEvent;
import com.siemens.serviceImpl.FileReaderService;

@Component
public class OnAssetFileUploadListner implements ApplicationListener<OnAssetFileUploadEvent>
{
    private static final Logger log = LoggerFactory.getLogger(OnAssetFileUploadListner.class);
    
    @Autowired
    private FileReaderService fileservice;
    
    @Async
    @Override
    public void onApplicationEvent(OnAssetFileUploadEvent event)
    {
        log.info("++++ In login event listner +++++++");
        
        String filePath = event.getSomeData();
        log.info("filePath: {}", filePath);
        
        try
        {
            fileservice.getSheetFromExcel(filePath);
        }
        catch (FileNotFoundException e)
        {
            log.error(e.getMessage());
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
    }
    
}
