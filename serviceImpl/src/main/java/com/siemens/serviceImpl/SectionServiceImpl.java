package com.siemens.serviceImpl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siemens.domain.Header;
import com.siemens.dto.SectionAssetKksTagsDTO;
import com.siemens.mapper.SectionEntityToSectionDto;
import com.siemens.repository.AssetRepository;
import com.siemens.repository.HeaderRepository;
import com.siemens.repository.UnitRepository;
import com.siemens.service.HeaderService;

/**
 * Service Implementation for managing {@link Header}.
 */
@Service
@Transactional
public class SectionServiceImpl implements HeaderService
{
    @Autowired
    private SectionEntityToSectionDto sectionMapper;
    
    @Autowired
    private AssetRepository assetRepo;
    
    private final Logger log = LoggerFactory.getLogger(SectionServiceImpl.class);
    
    private final HeaderRepository sectionRepository;
    
    @Autowired
    private UnitRepository unitRepository;
    
    public SectionServiceImpl(HeaderRepository sectionRepository)
    {
        this.sectionRepository = sectionRepository;
    }
    
    @Override
    public Header save(Header section, Long unitId)
    {
        log.debug("Request to save Section : {}", section);
        
        return unitRepository.findById(unitId).map(unit ->
        {
            section.setUnit(unit);
            section.setLastModifiedDate(Instant.now());
            return sectionRepository.save(section);
        }).orElseThrow(() -> new NullPointerException());
    }
    
    @Override
    public Optional<Header> partialUpdate(Header section)
    {
        log.debug("Request to partially update Section : {}", section);
        
        return sectionRepository
            .findById(section.getId())
            .map(
                existingSection ->
                {
                    if (section.getSectionName() != null)
                    {
                        existingSection.setSectionName(section.getSectionName());
                    }
                    if (section.getCreatedDate() != null)
                    {
                        existingSection.setCreatedDate(section.getCreatedDate());
                    }
                    if (section.getLastModifiedDate() != null)
                    {
                        existingSection.setLastModifiedDate(Instant.now());
                    }
                    
                    return existingSection;
                })
            .map(sectionRepository::save);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<List<Header>> findAll(Long unitId)
    {
        log.debug("Request to get all Sections");
        return sectionRepository.findByUnitId(unitId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Header> findOne(Long id, Long unitId)
    {
        log.debug("Request to get Section by unitId & Id: {}", id);
        return sectionRepository.findByIdAndUnitId(id, unitId);
    }
    
    @Override
    public void delete(Long id, Long unitId)
    {
        log.debug("Request to delete Section : {}", id);
        sectionRepository.deleteByIdAndUnitId(id, unitId);
    }
    
    @Override
    public Optional<Header> findByName(String sectionName)
    {
        
        log.debug("Request to get Section by Name: {}", sectionName);
        
        Optional<Header> sectionData = sectionRepository.findBySectionName(sectionName);
        
        return sectionData;
    }
    
    @Override
    public Optional<Header> findByNameAndUnitId(String sectionName, Long unitId)
    {
        
        log.debug("Request to get Section by unit Id & Name: {}", sectionName);
        
        Optional<Header> sectionData = sectionRepository.findBySectionNameAndUnitId(sectionName, unitId);
        
        return sectionData;
    }
    
	/* @Override */
    /*public List<SectionAssetKksTagsDTO> findSectionsWithAssetNames()
    {
        log.info("Request to findUnitsWithSectionNames()");
        List<Section> sectionList = sectionRepository.findAll();
        List<SectionAssetKksTagsDTO> sectionAssetKksDtoList = new ArrayList<>();
        
        for (Section section : sectionList)
        {
            List<String> assetKksTags = assetRepo.findAssetKksTagsForSectionId(section.getId());
            
            log.info("section: {}", section);
            log.info("assetKksTags: {}", assetKksTags);
            
            SectionAssetKksTagsDTO sectionAssetKksDtoObj = sectionMapper.sectionToAssetKksTagsView(section, assetKksTags);
            log.info("sectionAssetKksDtoObj: {}", sectionAssetKksDtoObj);
            
            sectionAssetKksDtoList.add(sectionAssetKksDtoObj);
        }
        log.info("sectionAssetKksDtoList: {}", sectionAssetKksDtoList);
        
        return sectionAssetKksDtoList;
    }*/
}
