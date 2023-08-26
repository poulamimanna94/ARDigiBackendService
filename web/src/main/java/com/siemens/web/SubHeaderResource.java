package com.siemens.web;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.config.ApiResponse;
import com.siemens.domain.SubHeader;
import com.siemens.exception.BadRequestAlertException;
import com.siemens.repository.SubHeaderRepository;
import com.siemens.service.SubHeaderService;

/**
 * REST controller for managing {@link com.siemens.domain.SubHeader}.
 */
@RestController
@RequestMapping(URLConstants.URL_API_ROOT + "/header")
public class SubHeaderResource
{
    
    private final Logger log = LoggerFactory.getLogger(SubHeaderResource.class);
    
    private static final String SUCCESSFULLY_MODIFIED = "Successfully modified record.";
    
    private static final String ENTITY_NAME = "subHeader";
    
    private final SubHeaderService subHeaderService;
    
    private final SubHeaderRepository subHeaderRepository;
    
    public SubHeaderResource(SubHeaderService subHeaderService, SubHeaderRepository subHeaderRepository)
    {
    	  this.subHeaderService = subHeaderService;
          this.subHeaderRepository = subHeaderRepository;
    }
    
    /**
     * {@code POST  /subHeader} : Create a new subHeader.
     *
     * @param subHeader the subHeader to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subHeader, or with status
     *         {@code 400 (Bad Request)} if the asset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */

    @PostMapping("/{headerId}/subHeader")
    public ResponseEntity<ApiResponse<SubHeader>> createSubHeader(
        @PathVariable(value = "headernId") Long headerId,
        @RequestBody SubHeader subHeader) throws URISyntaxException
    {
        log.debug("REST request to save SubHeader : {}", subHeader);
        if (subHeader.getId() != null)
        {
            throw new BadRequestAlertException("A new subHeader cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubHeader result = subHeaderService.save(subHeader, headerId);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PUT  /subHeaders/:id} : Updates an existing subHeader.
     *
     * @param id the id of the subHeader to save.
     * @param asset the subHeader to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asset,
     *         or with status {@code 400 (Bad Request)} if the subHeader is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the subHeader couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{headerId}/subHeaders/{id}")
    public ResponseEntity<ApiResponse<SubHeader>> updateSubHeader(
        @PathVariable(value = "headerId") Long headerId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubHeader subHeader)
        throws URISyntaxException
    {
        log.debug("REST request to update SubHeader : {}, {}", id, subHeader);
        if (subHeader.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subHeader.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!subHeaderRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        SubHeader result = subHeaderService.save(subHeader, headerId);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
     /**
     * {@code GET  /subHeaders} : get all the subHeaders.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subHeaders in body.
     */
    @GetMapping("/{headerId}/subHeaders")
    public List<SubHeader> getAllSubHeaders(
        @PathVariable(value = "headerId") Long headerId)
    {
        log.debug("REST request to get all SubHeaders");
        return subHeaderService.findAll(headerId).get();
    }
    
    /**
     * {@code GET  /subHeaders/:id} : get the "id" subHeader.
     *
     * @param id the id of the subHeader to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subHeader, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{headerId}/subHeaders/{id}")
    public ResponseEntity<ApiResponse<SubHeader>> getSubHeader(
        @PathVariable(value = "headerId") Long headerId,
        @PathVariable Long id)
    {
        log.debug("REST request to get SubHeader : {}", id);
        Optional<SubHeader> subHeader = subHeaderService.findOne(id, headerId);
        
        if (subHeader.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, subHeader.get()),
            HttpStatus.OK);
    }
    
    /**
     * {@code DELETE  /subHeaders/:id} : delete the "id" subHeader.
     *
     * @param id the id of the subHeader to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{headerId}/subHeaders/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubHeader(
        @PathVariable(value = "headerId") Long headerId,
        @PathVariable Long id)
    {
        log.debug("REST request to delete SubHeader : {}", id);
        subHeaderService.delete(id, headerId);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.NO_CONTENT.value(), SUCCESSFULLY_MODIFIED, null),
            HttpStatus.NO_CONTENT);
    }
  }
