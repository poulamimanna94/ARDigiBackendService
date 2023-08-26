/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */
package com.siemens.mapper;

import org.mapstruct.Mapper;

import com.siemens.domain.SharedTags;
import com.siemens.dto.SharedTagsDTO;
import com.siemens.dto.UserNotificationDTO;

@Mapper(componentModel = "spring")
public interface SharedTagsMapper
{
    // @Mapping(source = "tagObj.displayName" , target = "displayName")
    SharedTagsDTO sharedTagModelToDTO(SharedTags sTag, Long assetId);
    
    UserNotificationDTO sharedTagsToUserNotificationDTO(SharedTags sTag, Long assetId, String assetKksTag);
}
