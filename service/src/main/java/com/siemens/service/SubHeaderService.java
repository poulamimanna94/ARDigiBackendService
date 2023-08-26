package com.siemens.service;

import java.util.List;
import java.util.Optional;

import com.siemens.domain.SubHeader;

/**
 * Service Interface for managing {@link SubHeader}.
 */
public interface SubHeaderService
{
    /**
     * Save a subHeader.
     *
     * @param asset the entity to save.
     * @return the persisted entity.
     */
	SubHeader save(SubHeader subHeader, Long sectionId);
    
    /**
     * Get all the subHeaders.
     *
     * @return the list of entities.
     */
    Optional<List<SubHeader>> findAll(Long sectionId);
    
    /**
     * Get the "id" subHeader.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubHeader> findOne(Long id, Long sectionId);
    
    /**
     * Delete the "id" subHeader.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, Long sectionId);
    
}
