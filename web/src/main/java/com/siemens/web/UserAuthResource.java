package com.siemens.web;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.siemens.config.ApiResponse;
import com.siemens.constant.Constants;
import com.siemens.domain.OperatorListView;
import com.siemens.domain.UserAudit;
import com.siemens.dto.UserAuditDTO;
import com.siemens.dto.UserDTO;
import com.siemens.publishers.OnUserLoginPublisher;
import com.siemens.service.ManageKeyCloakService;
import com.siemens.service.PwdHashingTrippleDes;
import com.siemens.service.UserAuditService;
import com.siemens.service.UserService;
import com.siemens.web.viewModel.JwtToken;
import com.siemens.web.viewModel.LoginVM;

/**
 * UserAuthResource controller
 */
@RestController
@RequestMapping(URLConstants.URL_API_USER_AUTH)
public class UserAuthResource
{
    @Autowired
    private PwdHashingTrippleDes pwdHash;
    
    @Autowired
    private ManageKeyCloakService manageKeyCloakService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserAuditService userAuditService;
    
    @Autowired
    private OnUserLoginPublisher onUserLoginPublisher;
    
    private HashMap<String, String> userDetailsMap = new HashMap<>();
    
    private final Logger log = LoggerFactory.getLogger(UserAuthResource.class);
    
    /**
     * POST createClientConfiguration
     * 
     * @throws JsonProcessingException
     * @throws JsonMappingException
     */
    @PostMapping(URLConstants.URL_AUTHORIZE)
    public ResponseEntity<JwtToken> authorize(Principal principal, @Valid @RequestBody LoginVM loginVM)
        throws JsonMappingException, JsonProcessingException
    {
        log.info("User auth controller: inside authorize() method");
        HttpHeaders httpHeaders = new HttpHeaders();
        String exstngSessionKey = null;
        String userName = loginVM.getUsername();
        
        if (Objects.nonNull(userAuditService.getOperator(userName)))
        {
            log.info("getOperator() not null, user present in db... ");
            
            exstngSessionKey = userAuditService.getOperator(userName).getSessionKey();
            log.info("exstngSessionKey:" + exstngSessionKey);
        }
        
        if (Objects.nonNull(exstngSessionKey))
        {
            log.info("exstngSessionKey not null...");
            
            long exp = userAuditService.getExpInSessionKey(exstngSessionKey);
            
            log.info("Current time millis: {}", System.currentTimeMillis() / 1000);
            log.info("Token expiry time: {}", exp);
            
            if (exp > (System.currentTimeMillis() / 1000))// if token not expired
            {
                log.info("Token not expired,...");
                
                String uPassword = loginVM.getPassword();
                if (!uPassword.equals(userDetailsMap.get(userName)) && !userDetailsMap.isEmpty())// incorrect login validation
                {
                    log.info("1...userDetailsMap:" + userDetailsMap);
                    
                    log.info("Password Incorrect...");
                    return new ResponseEntity<>(new JwtToken(""), httpHeaders, HttpStatus.FORBIDDEN);
                }
                
                log.info("Sending same session key...");
                httpHeaders.add(Constants.AUTHORIZATION_HEADER, "Bearer " + exstngSessionKey);
                return new ResponseEntity<>(new JwtToken(exstngSessionKey), httpHeaders, HttpStatus.OK);
            }
        }
        
        // password decryption
        log.info("password from request: {}", loginVM.getPassword());// encoded password
        String textPwd = pwdHash.decrypt(loginVM.getPassword());
        log.info("textPwd: {}", textPwd);
        
        if (Objects.isNull(textPwd))
        {
            return new ResponseEntity<>(new JwtToken(""), httpHeaders, HttpStatus.FORBIDDEN);
        }
        AuthorizationResponse reqToken = manageKeyCloakService.AuthenticateViaKeyCloak(userName, textPwd);
        
        if (Objects.isNull(reqToken))
        {
            return new ResponseEntity<>(new JwtToken(""), httpHeaders, HttpStatus.FORBIDDEN);
        }
        log.info("User auth controller: current JWT token - {}", reqToken.getToken());
        String jwt = reqToken.getToken();
        httpHeaders.add(Constants.AUTHORIZATION_HEADER, "Bearer " + jwt);
        
        log.info("Save user audit details for user: {}", userName);
        
        UserAudit userAudit = new UserAudit();
        userAudit.setUserName(userName);
        userAudit.setLastLogin();// current timestamp
        userAudit.setLoginStatus(Constants.LOGIN_ACTIVE);
        userAudit.setSessionKey(jwt);
        userAuditService.saveUserLoginDetail(userAudit);
        
        // If Valid User and EHS is triggered then add entry in user evac table
        onUserLoginPublisher.publishOnUserLogin(userName);
        
        userDetailsMap.put(userName, loginVM.getPassword());
        log.info("2...userDetailsMap:" + userDetailsMap);
        
        return new ResponseEntity<>(new JwtToken(jwt), httpHeaders, HttpStatus.OK);
    }
    
    @GetMapping(URLConstants.URL_SYNC)
    public ResponseEntity<UserDTO> sync(Principal principal)
    {
        log.info("in side of sync() method");
        UserDTO userDTO = new UserDTO();
        if (principal instanceof AbstractAuthenticationToken)
        {
            // KeycloakSecurityContext context = ((KeycloakAuthenticationToken) principal).getAccount().getKeycloakSecurityContext();
            // SecurityContextHolder.setContext((SecurityContext) context);
            userDTO = userService.getUserFromAuthentication((AbstractAuthenticationToken) principal);
        }
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
    
    /**
     * {@code GET  /operator-stats} : get no. of operators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and no.
     *         of operators in body.
     */
    @GetMapping("/operator-stats")
    public Map<String, Integer> getOperatorStats()
    {
        log.info("In Resource , getOperatorStats()");
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("Total Operators", userAuditService.findOperatorsCount());
        stats.put("Current Users", userAuditService.findLoggedInOperatorsCount());
        
        log.info("stats: {}", stats);
        
        return stats;
    }
    
    /**
     * {@code GET  /operator-stats} : get avg time of all users in a day.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and no.
     *         of operators in body.
     */
    @GetMapping("/operators-avg")
    public Map<String, Double> getOperatorsAvgTimeDay()
    {
        log.info("In Resource , getOperatorStats()");
        
        Map<String, Double> stats = new HashMap<>();
        Double overallDailyAvg = userAuditService.findOverallAvgTime();
        
        stats.put("Avg Login Time(mins)", overallDailyAvg);
        log.info("stats: {}", stats);
        
        return stats;
    }
    
    /**
     * {@code GET  /user-audit} : get user audit for a user
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of tags in body.
     */
    @GetMapping("/user-audit")
    public ResponseEntity<ApiResponse<List<UserAuditDTO>>> getUserAuditHistory(@RequestParam String userName)
    {
        log.info("GET request to getUserAuditHistory() for user: {}", userName);
        
        List<UserAuditDTO> userAudDtoList = userAuditService.getUserAudit(userName);
        log.info("---- userAudDtoList : {}", userAudDtoList);
        
        if (userAudDtoList.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record found.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully fetched record(s).", userAudDtoList),
            HttpStatus.OK);
    }
    
    /**
     * {@code GET  /tags} : get all the operators.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of operators in body.
     */
    @GetMapping("/operators-list-avg")
    public ResponseEntity<ApiResponse<List<OperatorListView>>> getOperatorsListWithAvg()
    {
        log.info("In Resource , getOperatorsListWithAvg()");
        log.info("REST request to get operators history, Result: {}", userAuditService.findOperatorsWithAvg());
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully found record.", userAuditService.findOperatorsWithAvg()),
            HttpStatus.OK);
    }
    
}
