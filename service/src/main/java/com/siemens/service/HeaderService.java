package com.siemens.service;

import java.util.List;
import java.util.Optional;

import com.siemens.domain.Header;
import com.siemens.dto.SectionAssetKksTagsDTO;

/**
 * Service Interface for managing {@link Header}.
 */
public interface HeaderService
{
    /**
     * Save a section.
     *
     * @param section the entity to save.
     * @return the persisted entity.
     */
    Header save(Header section, Long unitId);
    
    /**
     * Partially updates a section.
     *
     * @param section the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Header> partialUpdate(Header section);
    
    /**
     * Get all the sections.
     *
     * @return the list of entities.
     */
    Optional<List<Header>> findAll(Long unitId);
    
    /**
     * Get the "id" section.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Header> findOne(Long id, Long unitId);
    
    /**
     * Delete the "id" section.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, Long unitId);
    
    /**
     * Get the "sectionName" section.
     *
     * @param sectionName the name of the entity.
     */
    Optional<Header> findByName(String sectionName);
    
    Optional<Header> findByNameAndUnitId(String sectionName, Long unitId);
    
   // List<SectionAssetKksTagsDTO> findSectionsWithAssetNames();
}
