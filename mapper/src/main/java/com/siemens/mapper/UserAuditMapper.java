/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */
package com.siemens.mapper;

import java.time.LocalDate;

import org.mapstruct.Mapper;

import com.siemens.domain.UserAudit;
import com.siemens.dto.OperatorListDTO;
import com.siemens.dto.UserAuditDTO;

@Mapper(componentModel = "spring")
public interface UserAuditMapper
{
    UserAuditDTO userAuditModelToDTO(UserAudit userAudit, LocalDate dt, Long duration);
    
    OperatorListDTO operatorListToDTO(UserAudit usrAudObj, Double dailyDuration, Double avgDuration);
}
