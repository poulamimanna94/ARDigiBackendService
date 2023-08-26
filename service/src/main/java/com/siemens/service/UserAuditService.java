package com.siemens.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.domain.OperatorListView;
import com.siemens.domain.User;
import com.siemens.domain.UserAudit;
import com.siemens.dto.OperatorListDTO;
import com.siemens.dto.UserAuditDTO;
import com.siemens.dto.UserLocationDTO;
import com.siemens.mapper.UserAuditMapper;
import com.siemens.repository.OperatorListRepository;
import com.siemens.repository.UserAuditRepository;
import com.siemens.repository.UserRepository;

/**
 * Service class for managing user audit.
 */
@Service
@Transactional
public class UserAuditService
{
    private final Logger log = LoggerFactory.getLogger(UserAuditService.class);
    private final UserAuditRepository userAuditRepository;
    private final OperatorListRepository opListRepository;
    private final UserRepository userRepository;
    
    @Autowired
    private UserAuditMapper userAuditMapper;
    
    ObjectMapper objectMapper;
    
    private HashMap<String, UserLocationDTO> usrLocMap = new HashMap<>();
    
    @Autowired
    CacheManager cMgr;
    
    public UserAuditService(UserAuditRepository userAuditRepository, OperatorListRepository opListRepository, UserRepository userRepository)
    {
        this.userAuditRepository = userAuditRepository;
        this.opListRepository = opListRepository;
        this.userRepository = userRepository;
    }
    
    // to save user details on login
    public void saveUserLoginDetail(UserAudit userAudit)
    {
        log.info("In saveUserLoginDetail() for user: {}", userAudit);
        userAuditRepository.save(userAudit);
    }
    
    // to get operators list
    public List<UserAudit> findOperators()
    {
        log.info("In Service , findOperators()");
        return userAuditRepository.findOperators();
    }
    
    public int findOperatorsCount()
    {
        log.info("In Service , findOperatorsCount()");
        return userAuditRepository.findOperatorsCount();
    }
    
    public int findLoggedInOperatorsCount()
    {
        log.info("In Service , findLoggedInOperatorsCount()");
        return userAuditRepository.findLoggedInOperatorsCount();
    }
    
    public Double findOverallAvgTime()
    {
        log.info("In Service , findOverallAvgTime()");
        
        return userAuditRepository.findOverallAvgTime();
    }
    
    // to get user's audit trail with duration for each session
    public List<UserAuditDTO> getUserAudit(String userName)
    {
        log.info("In getUserAudit() for user: {}", userName);
        
        List<UserAudit> userAudList = userAuditRepository.findByUserName(userName);
        log.info("userAudList: {}", userAudList);
        
        List<UserAuditDTO> userAudDtoList = new ArrayList<>();
        
        for (UserAudit ua : userAudList)
        {
            LocalDate dt = LocalDate.ofInstant(ua.getLastLogin(), ZoneId.systemDefault());
            log.info("Date dt: {}", dt);
            
            log.info("lastLogin: {}", ua.getLastLogin());
            log.info("lastLogout: {}", ua.getLastLogout());
            
            Duration duration = Duration.between(ua.getLastLogin(), ua.getLastLogout());
            log.info("duration: {}", duration);
            
            UserAuditDTO uaDtoObj = userAuditMapper.userAuditModelToDTO(ua, dt, duration.toMinutes());
            log.info("------ uaDtoObj: " + uaDtoObj);
            
            userAudDtoList.add(uaDtoObj);
        }
        log.info("------Final userAudDtoList: " + userAudDtoList);
        
        return userAudDtoList;
    }
    
    // to get operator for authorize()
    public UserAudit getOperator(String userName)
    {
        log.info("In Service , getOperator()");
        return userAuditRepository.getOperator(userName);
    }
    
    public long getExpInSessionKey(String sessionKey) throws JsonMappingException, JsonProcessingException
    {
        log.info("In Service , getExpInSessionKey()");
        
        String[] chunks = sessionKey.split("\\.");
        JSONObject payloadnew = new JSONObject(decode(chunks[1]));
        
        long exp = payloadnew.getLong("exp");
        log.info("USER exp time value: {}", exp);
        
        String name = payloadnew.getString("name");
        log.info("USER name: {}", name);
        
        return exp;
    }
    
    private static String decode(String encodedString)
    {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
    
    // to get operators list with avg
    public List<OperatorListDTO> findOperatorsAvgApproach2()
    {
        log.info("In findOperatorsWithAvg(): {}");
        
        List<UserAudit> userAudList = userAuditRepository.findOperators();
        log.info("userAudList: {}", userAudList);
        
        List<OperatorListDTO> oprtrDtoList = new ArrayList<>();
        
        for (UserAudit ua : userAudList)
        {
            log.info("ua: {}", ua);
            
            LocalDate dt = LocalDate.ofInstant(ua.getLastLogin(), ZoneId.systemDefault());
            log.info("Date dt: {}", dt);
            
            log.info("lastLogin: {}", ua.getLastLogin());
            log.info("lastLogout: {}", ua.getLastLogout());
            
            // total duration
            Double dayDuration = 0.0;
            Double avgDurationMonths = 0.0;
            
            if (ua.getLastLogin() != null && ua.getLastLogout() != null)
            {
                Duration duration = Duration.between(ua.getLastLogin(), ua.getLastLogout());
                log.info("duration: {}", duration);
                
                dayDuration += duration.toMinutes();
                log.info("------dayDuration: " + dayDuration);
                
                avgDurationMonths += dayDuration / 66;
                log.info("------avgDurationMonths: " + avgDurationMonths);
                
                OperatorListDTO oprtrDtoObj = userAuditMapper.operatorListToDTO(ua, dayDuration, avgDurationMonths);
                log.info("------ oprtrDtoObj: " + oprtrDtoObj);
                
                oprtrDtoList.add(oprtrDtoObj);
                // set in a avg field for useraudit using native query for last login entry(findOpeartors query) or for its id
                // fetch operators list
            }
        }
        log.info("------Final oprtrDtoList: " + oprtrDtoList);
        
        return oprtrDtoList;
    }
    
    public List<OperatorListView> findOperatorsWithAvg()
    {
        log.info("In Service , findOperatorsWithAvg()");
        
        List<OperatorListView> opList = opListRepository.findOperatorsWithAvg();
        log.info("In Service , opList :" + opList);
        
        return opList;
    }
    
    public int setManualLogout(String userName)
    {
        log.info("In Service , setManualLogout() for user: {}", userName);
        
        return userAuditRepository.setManualLogout(userName);
    }
    
    public int setAutoLogout(Long id)
    {
        log.info("In Service , setAutoLogout() for user with id: {}", id);
        
        return userAuditRepository.setAutoLogout(id);
    }
    
    public HashMap<String, UserLocationDTO> userLocationSave(String name, UserLocationDTO usrLoc)
    {
        log.info("In Service , userLocationSave() ");
        
        usrLocMap.put(name, usrLoc);
        return usrLocMap;
    }
    
    // @Cacheable("userLocCache")
    public HashMap<String, UserLocationDTO> userLocationFetch()
    {
        log.info("In Service , userLocationFetch() ..!");
        return usrLocMap;
    }
    
    /*
     * Fetching user names from user_audit entity for caching
     */
    @Transactional(readOnly = true)
    @Cacheable(value = "userNameCache", key = "#login")
    public List<String> getUserNames(String login)
    {
        log.info("In Service , getUserNames() .. sleeping 2 sec!");
        try
        {
            Thread.sleep(1000 * 2);
        }
        catch (InterruptedException e)
        {
            log.info(e.getMessage());
        }
        
        List<String> unames = userRepository.findAll().stream().map(User::getLogin).collect(Collectors.toList());
        log.info("unames: {}", unames);
        
        return unames;
    }
    
    // query used in filter when user inactive but token active::
    public String findLoginStatusForRequestFilter(String token)
    {
        log.info("In Service , findLoginStatusForRequestFilter() ..");
        return userAuditRepository.findLoginStatusForRequestFilter(token);
    }
    
    public void clearUsrLocMap()
    {
        usrLocMap.clear();
    }
    
    public void activeUserLocMap(HashMap<String, UserLocationDTO> hm)
    {
        log.info("In Service , activeUserLocMap() ..");
        
        usrLocMap = hm;
        log.info("activeUserLocMap() - usrLocMap: {}", usrLocMap);
    }
}
