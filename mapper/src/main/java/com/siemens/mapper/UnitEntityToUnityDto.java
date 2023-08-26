package com.siemens.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.siemens.domain.Unit;
import com.siemens.dto.UnitDto;
import com.siemens.dto.UnitSectionNamesDto;

@Mapper(componentModel = "spring")
public interface UnitEntityToUnityDto
{
    UnitDto UnitEntityToUnitDto(Unit unit);

    UnitSectionNamesDto unitToSectionNamesView(Unit unit, List<String> sectionNames);
}
