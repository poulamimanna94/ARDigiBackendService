/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */
package com.siemens.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.siemens.domain.Asset;
import com.siemens.dto.AssetDTO;
import com.siemens.dto.AssetSectionUnitNameDTO;
import com.siemens.dto.TagDTO;

@Mapper(componentModel = "spring")
public interface AssetMapper
{
    AssetDTO assetModelToDTO(Asset asset, List<TagDTO> tagsData, String alarmStatus);
    
    AssetSectionUnitNameDTO assetToSectionUnitNameDTO(Asset asset, String sectionName, String unitName);
}
