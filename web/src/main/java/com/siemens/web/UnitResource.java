package com.siemens.web;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.config.ApiResponse;
import com.siemens.domain.Unit;
import com.siemens.dto.UnitDto;
import com.siemens.dto.UnitSectionNamesDto;
import com.siemens.exception.BadRequestAlertException;
import com.siemens.mapper.UnitEntityToUnityDto;
import com.siemens.repository.UnitRepository;
import com.siemens.service.UnitService;

/**
 * REST controller for managing {@link com.siemens.domain.Unit}.
 */
@RestController
@RequestMapping("/api")
public class UnitResource
{
    
    private final Logger log = LoggerFactory.getLogger(UnitResource.class);
    
    private static final String ENTITY_NAME = "unit";
    
    private final UnitService unitService;
    
    private final UnitRepository unitRepository;
    
    @Autowired
    private UnitEntityToUnityDto unitEntityToUnityDtoMapper;
    
    public UnitResource(UnitService unitService, UnitRepository unitRepository)
    {
        this.unitService = unitService;
        this.unitRepository = unitRepository;
    }
    
    /**
     * {@code POST  /units} : Create a new unit.
     *
     * @param unit the unit to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new unit, or with status
     *         {@code 400 (Bad Request)} if the unit has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/units")
    public ResponseEntity<ApiResponse<Unit>> createUnit(@Valid @RequestBody Unit unit) throws URISyntaxException
    {
        log.debug("REST request to save Unit : {}", unit);
        if (unit.getId() != null)
        {
            throw new BadRequestAlertException("A new unit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Unit result = unitService.save(unit);
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), URLConstants.SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PUT  /units/:id} : Updates an existing unit.
     *
     * @param id the id of the unit to save.
     * @param unit the unit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unit,
     *         or with status {@code 400 (Bad Request)} if the unit is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the unit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/units/{id}")
    public ResponseEntity<ApiResponse<Unit>> updateUnit(@PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Unit unit)
        throws URISyntaxException
    {
        log.debug("REST request to update Unit : {}, {}", id, unit);
        if (unit.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unit.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!unitRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Unit result = unitService.save(unit);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), URLConstants.SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PATCH  /units/:id} : Partial updates given fields of an existing unit, field will ignore if it is null
     *
     * @param id the id of the unit to save.
     * @param unit the unit to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated unit,
     *         or with status {@code 400 (Bad Request)} if the unit is not valid,
     *         or with status {@code 404 (Not Found)} if the unit is not found,
     *         or with status {@code 500 (Internal Server Error)} if the unit couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/units/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApiResponse<Unit>> partialUpdateUnit(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Unit unit) throws URISyntaxException
    {
        log.debug("REST request to partial update Unit partially : {}, {}", id, unit);
        if (unit.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, unit.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!unitRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Optional<Unit> result = unitService.partialUpdate(unit);
        
        if (result.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), URLConstants.SUCCESSFULLY_MODIFIED, result.get()),
            HttpStatus.OK);
    }
    
    /**
     * {@code GET  /units} : get all the units.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of units in body.
     */
    @GetMapping("/units")
    public List<UnitDto> getAllUnits()
    {
        log.debug("REST request to get all Units");
        return unitService.findAll().stream().map(result -> unitEntityToUnityDtoMapper.UnitEntityToUnitDto(result))
            .collect(Collectors.toList());
    }
    
    /**
     * {@code GET  /units/:id} : get the "id" unit.
     *
     * @param id the id of the unit to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the unit, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/units/{id}")
    public ResponseEntity<ApiResponse<Unit>> getUnit(@PathVariable Long id)
    {
        log.debug("REST request to get Unit : {}", id);
        Optional<Unit> unit = unitService.findOne(id);
        if (unit.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
                HttpStatus.OK);
        }
        
        return unit.map(result ->
        {
            if (Objects.isNull(result.getSections()))
            {
                result.setSections(null);
            }
            
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "Successfully found record.", result),
                HttpStatus.OK);
        }).orElse(
            new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "Record not found.", null),
                HttpStatus.OK));
    }
    
    /**
     * {@code DELETE  /units/:id} : delete the "id" unit.
     *
     * @param id the id of the unit to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/units/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUnit(@PathVariable Long id)
    {
        log.debug("REST request to delete Unit : {}", id);
        unitService.delete(id);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.NO_CONTENT.value(), URLConstants.SUCCESSFULLY_MODIFIED, null),
            HttpStatus.NO_CONTENT);
    }
    
    /**
     * {@code GET  /units/:id} : get the units with their section names.
     *
     * @param id the id of the asset to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asset, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/units/offline-unit-section-names")
    public ResponseEntity<ApiResponse<List<UnitSectionNamesDto>>> getUnitsWithSectionNames()
    {
        log.debug("REST request to get Units with Section names for offline data");
        List<UnitSectionNamesDto> dtoListObj = unitService.findUnitsWithSectionNames();
        
        if (Objects.isNull(dtoListObj))
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No record found.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully fetched record(s)", dtoListObj),
            HttpStatus.OK);
    }
}
