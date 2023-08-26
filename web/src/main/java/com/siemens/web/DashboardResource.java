package com.siemens.web;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.config.ApiResponse;
import com.siemens.dto.UserLocationDTO;
import com.siemens.service.UserAuditService;

/**
 * User Dashboard Controller
 */
@RestController
@RequestMapping(URLConstants.URL_API_DASHBOARD)
public class DashboardResource
{
    @Autowired
    private UserAuditService userAuditService;
    
    private final Logger log = LoggerFactory.getLogger(DashboardResource.class);
    
    /**
     * POST User Location Cache
     */
    @PostMapping("/save-operator-loc")
    public ResponseEntity<ApiResponse<HashMap<String, UserLocationDTO>>> operatorPositionSave(@RequestBody UserLocationDTO usrLoc)
    {
        log.info("In Resource , operatorPositionSave()");
        
        String login = usrLoc.getLogin();
        log.info("login: {}", login);
        
        List<String> userNames = userAuditService.getUserNames(login);
        HashMap<String, UserLocationDTO> usrLocMap = null;
        
        if (userNames.contains(login))
        {
            usrLocMap = userAuditService.userLocationSave(login, usrLoc);
        }
        
        log.info("usrLocMap: {}", usrLocMap);
        
        if (Objects.isNull(usrLocMap))
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "Invalid API Request", usrLocMap),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully modified record.", usrLocMap),
            HttpStatus.OK);
    }
    
    /**
     * {@code GET  /tags} : get all the operators current loc.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of operators in body.
     */
    @GetMapping("/fetch-operator-loc")
    public ResponseEntity<ApiResponse<Object[]>> operatorPositionFetch()
    {
        log.info("In Resource , operatorPositionFetch()");
        
        HashMap<String, UserLocationDTO> usrLocMap = userAuditService.userLocationFetch();
        log.info("usrLocMap: {}", usrLocMap);
        log.info("Obj array: {}", Objects.isNull(usrLocMap.values().toArray()));
        
        if (usrLocMap.isEmpty() || Objects.isNull(usrLocMap.values().toArray()))// || usrLocMap.values().contains(null)
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No Records Found", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully found record.", usrLocMap.values().toArray()),
            HttpStatus.OK);
    }
}
