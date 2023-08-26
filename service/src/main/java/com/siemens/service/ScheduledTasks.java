package com.siemens.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.siemens.domain.UserAudit;
import com.siemens.dto.UserLocationDTO;
import com.siemens.repository.UserAuditRepository;

@Component
public class ScheduledTasks
{
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired
    private UserAuditService usrAudService;
    
    @Autowired
    private UserAuditRepository usrAudRepo;
    
    @Scheduled(fixedRate = 15000) // 15 sec
    public void reportCurrentTime()
    {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
    
    /*
     * This scheduler automatically updates logout time & status for users whose token has expired
     */
    @Scheduled(cron = "0 0/1 * * * *") // every 1 min
    public void logoutAutoTokenExpire() throws JsonMappingException, JsonProcessingException
    {
        log.info(".....in logoutAutoTokenExpire()");
        
        List<UserAudit> usrAudList = usrAudRepo.findAllLogoutNull();
        
        for (UserAudit ua : usrAudList)
        {
            if (Objects.nonNull(ua.getSessionKey()))
            {
                log.info("SessionKey not null... ");
                
                long exp = usrAudService.getExpInSessionKey(ua.getSessionKey());
                
                log.info("Current time millis: {}", System.currentTimeMillis() / 1000);
                log.info("Token expiry time: {}", exp);
                
                if (exp < (System.currentTimeMillis() / 1000))// if token expired
                {
                    log.info("Token Expired!");
                    
                    int rowAffected = usrAudService.setAutoLogout(ua.getId());
                    log.info("rowsAffected: {}", rowAffected);
                }
                else
                {
                    log.info("Token NOT Expired");
                }
            }
        }
    }
    
    /*
     * This scheduler automatically updates user location map - for operators who are active
     */
    @Scheduled(cron = "0 0/3 * * * *") // 3 min
    public void removeOperatorOnLogout()
    {
        log.info("IN removeOperatorOnLogout() ....");
        List<UserAudit> usrAudList = usrAudRepo.findAllActiveUsers();
        
        HashMap<String, UserLocationDTO> usrLocMap = usrAudService.userLocationFetch();
        log.info("Current users hashMap: {}", usrLocMap);
        
        HashMap<String, UserLocationDTO> activeUsrLocMap = new HashMap<>();
        UserLocationDTO usrLocDtoObj;
        
        for (UserAudit ua : usrAudList)
        {
            usrLocDtoObj = usrLocMap.get(ua.getUserName());
            log.info("Current usrLocDtoObj: {}", usrLocDtoObj);
            
            activeUsrLocMap.put(ua.getUserName(), usrLocDtoObj);
        }
        
        log.info("Current activeUsrLocMap: {}", activeUsrLocMap);
        usrAudService.activeUserLocMap(activeUsrLocMap);
    }
}
