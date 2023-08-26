package com.siemens.service;

import com.siemens.domain.Unit;
import com.siemens.dto.UnitSectionNamesDto;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Unit}.
 */
public interface UnitService
{
    /**
     * Save a unit.
     *
     * @param unit the entity to save.
     * @return the persisted entity.
     */
    Unit save(Unit unit);
    
    /**
     * Partially updates a unit.
     *
     * @param unit the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Unit> partialUpdate(Unit unit);
    
    /**
     * Get all the units.
     *
     * @return the list of entities.
     */
    List<Unit> findAll();
    
    /**
     * Get the "id" unit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Unit> findOne(Long id);
    
    /**
     * Delete the "id" unit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
    
    /**
     * Get the "unitName" unit.
     *
     * @param unitName the name of the entity.
     * @return the entity.
     */
    Optional<Unit> findByName(String unitName);

    List<UnitSectionNamesDto> findUnitsWithSectionNames();
}
