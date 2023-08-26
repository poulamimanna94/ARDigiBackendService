package com.siemens.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.siemens.domain.Header;
import com.siemens.dto.SectionAssetKksTagsDTO;
import com.siemens.dto.SectionDto;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SectionEntityToSectionDto
{
    // @Mapping(target = "assets", ignore = true)
    SectionDto MapSectionEntityToSectionDto(Header section);
    
    SectionAssetKksTagsDTO sectionToAssetKksTagsView(Header section, List<String> assetKksTags);
}
