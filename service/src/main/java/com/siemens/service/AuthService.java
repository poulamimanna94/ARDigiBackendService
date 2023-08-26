/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */

package com.siemens.service;

import com.siemens.domain.RolePermission;
import com.siemens.repository.AuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthService
{
    private final Logger log = LoggerFactory.getLogger(AuthService.class);
    
    private final AuthorityRepository authorityRepository;
    
    public AuthService(AuthorityRepository authorityRepository)
    {
        this.authorityRepository = authorityRepository;
    }
    
    public List<RolePermission> getAuthorityPermissions(String authName)
    {
        log.info("in side -> getAuthorityPermissions(String authName) method");
        List<RolePermission> authorities = new ArrayList<>();
        authorityRepository.findById(authName).ifPresent(authorities::add);
        return authorities;
    }
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
