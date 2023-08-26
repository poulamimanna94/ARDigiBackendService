package com.siemens.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siemens.domain.Header;
import com.siemens.domain.Unit;
import com.siemens.dto.UnitSectionNamesDto;
import com.siemens.mapper.UnitEntityToUnityDto;
import com.siemens.repository.HeaderRepository;
import com.siemens.repository.UnitRepository;
import com.siemens.service.UnitService;

/**
 * Service Implementation for managing {@link Unit}.
 */
@Service
@Transactional
public class UnitServiceImpl implements UnitService
{
    @Autowired
    private UnitEntityToUnityDto unitMapper;
    
    private final Logger log = LoggerFactory.getLogger(UnitServiceImpl.class);
    
    private final UnitRepository unitRepository;
    
    @Autowired
    private HeaderRepository sectionRepository;
    
    public UnitServiceImpl(UnitRepository unitRepository)
    {
        this.unitRepository = unitRepository;
    }
    
    @Override
    public Unit save(Unit unit)
    {
        log.debug("Request to save Unit : {}", unit);
        unit.setLastModifiedDate(Instant.now());
        return unitRepository.save(unit);
    }
    
    @Override
    public Optional<Unit> partialUpdate(Unit unit)
    {
        log.debug("Request to partially update Unit : {}", unit);
        
        return unitRepository
            .findById(unit.getId())
            .map(
                existingUnit ->
                {
                    if (unit.getUnitName() != null)
                    {
                        existingUnit.setUnitName(unit.getUnitName());
                    }
                    if (unit.getCreatedDate() != null)
                    {
                        existingUnit.setCreatedDate(unit.getCreatedDate());
                    }
                    if (unit.getLastModifiedDate() != null)
                    {
                        existingUnit.setLastModifiedDate(Instant.now());
                    }
                    
                    return existingUnit;
                })
            .map(unitRepository::save);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Unit> findAll()
    {
        log.debug("Request to get all Units");
        return unitRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Unit> findOne(Long id)
    {
        log.debug("Request to get Unit : {}", id);
        
        Optional<Unit> unitData = unitRepository.findById(id);
        
        if (unitData.isEmpty())
        {
            return unitData;
        }
        
        Unit unitResult = unitData.get();
        Optional<List<Header>> sectionData = sectionRepository.findByUnitId(id);
        unitResult.setSections(sectionData.get().stream().collect(Collectors.toSet()));
        
        return Optional.of(unitResult);
    }
    
    @Override
    public void delete(Long id)
    {
        log.debug("Request to delete Unit : {}", id);
        unitRepository.deleteById(id);
    }
    
    @Override
    public Optional<Unit> findByName(String unitName)
    {
        
        log.debug("Request to get Unit : {}", unitName);
        Optional<Unit> unitData = unitRepository.findByUnitName(unitName);
        return unitData;
    }
    
    @Override
    public List<UnitSectionNamesDto> findUnitsWithSectionNames()
    {
        log.info("Request to findUnitsWithSectionNames()");
        List<Unit> unitsList = unitRepository.findAll();
        List<UnitSectionNamesDto> unitsSectionNamesDtoList = new ArrayList<>();
        
        for (Unit unit : unitsList)
        {
            List<String> sectionNames = sectionRepository.findSectionNamesForUnitId(unit.getId());
            
            log.info("unit: {}", unit);
            log.info("sectionNames: {}", sectionNames);
            
            UnitSectionNamesDto unitSectionDtoObj = unitMapper.unitToSectionNamesView(unit, sectionNames);
            log.info("unitSectionDtoObj: {}", unitSectionDtoObj);
            
            unitsSectionNamesDtoList.add(unitSectionDtoObj);
        }
        log.info("unitsSectionNamesDtoList: {}", unitsSectionNamesDtoList);
        
        return unitsSectionNamesDtoList;
    }
}
