/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */
package com.siemens.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.siemens.domain.Tag;
import com.siemens.domain.TagLiveData;
import com.siemens.dto.TagDTO;

@Mapper(componentModel = "spring")
public interface TagMapper
{
    
    // @Mapping(source = "tagSn.subscriptionName", target = "subscriptionName")
    @Mapping(source = "tagObj.displayName", target = "displayName")
    TagDTO tagModelToDTO(Tag tagObj, TagLiveData nodeValue, String alarmStatus);
    
    Tag tagDtoToModel(TagDTO tagDto);
    
    List<TagDTO> tagModelsToDTOs(List<Tag> tags);
    
    List<Tag> tagDtosToModels(List<TagDTO> tagDto);
    
}
