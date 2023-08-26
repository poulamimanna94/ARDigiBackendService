package com.siemens.serviceImpl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siemens.domain.SubHeader;
import com.siemens.repository.HeaderRepository;
import com.siemens.repository.SubHeaderRepository;
import com.siemens.service.SubHeaderService;

/**
 * Service Implementation for managing {@link SubHeader}.
 */
@Service
@Transactional
public class SubHeaderServiceImpl implements SubHeaderService
{
    private final Logger log = LoggerFactory.getLogger(SubHeaderServiceImpl.class);
    
    private final SubHeaderRepository subHeaderRepository;
    
    @Autowired
    private HeaderRepository sectionRepository;
    
    public SubHeaderServiceImpl(SubHeaderRepository subHeaderRepository)
    {
        this.subHeaderRepository = subHeaderRepository;
    }
    
    @Override
    public SubHeader save(SubHeader subHeader, Long sectionId)
    {
        log.debug("Request to save SubHeader : {}", subHeader);
        
        return sectionRepository.findById(sectionId).map(section ->
        {
        	subHeader.setSection(section);
        	subHeader.setLastModifiedDate(Instant.now());
            return subHeaderRepository.save(subHeader);
        }).orElseThrow(() -> new NullPointerException());
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<List<SubHeader>> findAll(Long sectionId)
    {
        log.debug("Request to get all SubHeaders");
        return subHeaderRepository.findBySectionId(sectionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<SubHeader> findOne(Long id, Long sectionId)
    {
        log.debug("Request to get SubHeader : {}", id);
        return subHeaderRepository.findByIdAndSectionId(id, sectionId);
    }
    
    @Override
    public void delete(Long id, Long sectionId)
    {
        log.debug("Request to delete SubHeader : {}", id);
        subHeaderRepository.deleteByIdAndSectionId(id, sectionId);
    }    
    
}
