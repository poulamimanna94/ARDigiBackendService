package com.siemens.web;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.siemens.domain.Header;
//import com.siemens.dto.SectionAssetKksTagsDTO;
import com.siemens.exception.BadRequestAlertException;
import com.siemens.repository.HeaderRepository;
import com.siemens.service.HeaderService;

/**
 * REST controller for managing {@link com.siemens.domain.Header}.
 */
@RestController
@RequestMapping("/api/unit")
public class HeaderResource
{
    
    private final Logger log = LoggerFactory.getLogger(HeaderResource.class);
    
    private static final String SUCCESSFULLY_MODIFIED = "Successfully modified record.";
    
    private static final String ENTITY_NAME = "section";
    
    private final HeaderService headerService;
    
    private final HeaderRepository headerRepository;
    
    public HeaderResource(HeaderService headerService, HeaderRepository headerRepository)
    {
        this.headerService = headerService;
        this.headerRepository = headerRepository;
    }
    
    /**
     * {@code POST  /sections} : Create a new section.
     *
     * @param section the section to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new section, or with status
     *         {@code 400 (Bad Request)} if the section has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/{unitId}/header")
    public ResponseEntity<ApiResponse<Header>> createHeader(
        @PathVariable(value = "unitId") Long unitId,
        @Valid @RequestBody Header section) throws URISyntaxException
    {
        log.debug("REST request to save Section : {}", section);
        if (section.getId() != null)
        {
            throw new BadRequestAlertException("A new header cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Header result = headerService.save(section, unitId);
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PUT  /sections/:id} : Updates an existing section.
     *
     * @param id the id of the section to save.
     * @param header the section to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated section,
     *         or with status {@code 400 (Bad Request)} if the section is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the section couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{unitId}/headers/{id}")
    public ResponseEntity<ApiResponse<Header>> updateHeader(
        @PathVariable(value = "unitId") Long unitId,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Header header) throws URISyntaxException
    {
        log.debug("REST request to update Section : {}, {}", id, header);
        if (header.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, header.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!headerRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Header result = headerService.save(header, unitId);
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PATCH  /sections/:id} : Partial updates given fields of an existing section, field will ignore if it is null
     *
     * @param id the id of the section to save.
     * @param header the section to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated section,
     *         or with status {@code 400 (Bad Request)} if the section is not valid,
     *         or with status {@code 404 (Not Found)} if the section is not found,
     *         or with status {@code 500 (Internal Server Error)} if the section couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/headers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApiResponse<Header>> partialUpdateHeader(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Header header) throws URISyntaxException
    {
        log.debug("REST request to partial update Header partially : {}, {}", id, header);
        if (header.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, header.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!headerRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Optional<Header> result = headerService.partialUpdate(header);
        
        if (result.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result.get()),
            HttpStatus.OK);
    }
    
    /**
     * {@code GET  /sections} : get all the sections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sections in body.
     */
    @GetMapping("/{unitId}/headers")
    public List<Header> getAllHeaders(@PathVariable(value = "unitId") Long unitId)
    {
        log.debug("REST request to get all Sections");
        
        Optional<List<Header>> temp = headerService.findAll(unitId);
        
        return temp.get();
    }
    
    /**
     * {@code GET  /headers/:id} : get the "id" header.
     *
     * @param id the id of the header to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the section, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{unitId}/headers/{id}")
    public ResponseEntity<ApiResponse<Header>> getHeader(
        @PathVariable(value = "unitId") Long unitId,
        @PathVariable Long id)
    {
        log.debug("REST request to get Section : {}", id);
        Optional<Header> section = headerService.findOne(id, unitId);
        if (section.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record found.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully Record Found.",
                section.get()),
            HttpStatus.OK);
    }
    
    /**
     * {@code DELETE  /headers/:id} : delete the "id" header.
     *
     * @param id the id of the header to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{unitId}/headers/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteHeader(
        @PathVariable(value = "unitId") Long unitId,
        @PathVariable Long id)
    {
        log.debug("REST request to delete Section : {}", id);
        headerService.delete(id, unitId);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.NO_CONTENT.value(), SUCCESSFULLY_MODIFIED, null),
            HttpStatus.NO_CONTENT);
    }
    
    /**
     * {@code GET  /headers} : get the sections with their asset names.
     *
     * @param id the id of the asset to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asset, or with status {@code 404 (Not Found)}.
     */
	/*
	 * @GetMapping("/units/offline-section-asset-kkstags") public
	 * ResponseEntity<ApiResponse<List<SectionAssetKksTagsDTO>>>
	 * getSectionsWithAssetKksTags() {
	 * log.debug("REST request to get Sections with Asset Kks Tags for offline data"
	 * ); List<SectionAssetKksTagsDTO> dtoListObj =
	 * sectionService.findSectionsWithAssetNames();
	 * 
	 * if (Objects.isNull(dtoListObj)) { return new ResponseEntity<>( new
	 * ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No record found.", null),
	 * HttpStatus.OK); }
	 * 
	 * return new ResponseEntity<>( new ApiResponse<>(HttpStatus.OK.value(),
	 * "Successfully fetched record(s)", dtoListObj), HttpStatus.OK); }
	 */
}
