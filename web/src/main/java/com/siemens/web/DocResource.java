package com.siemens.web;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.config.ApiResponse;
import com.siemens.domain.Document;
import com.siemens.exception.BadRequestAlertException;
import com.siemens.repository.DocRepository;
import com.siemens.service.DocService;

/**
 * REST controller for managing {@link com.siemens.domain.Document}.
 */
@RestController
@RequestMapping(URLConstants.URL_API_ROOT + "/asset")
public class DocResource
{
    
    private final Logger log = LoggerFactory.getLogger(DocResource.class);
    
    private static final String SUCCESSFULLY_MODIFIED = "Successfully modified record.";
    
    private static final String ENTITY_NAME = "document";
    
    private final DocService docService;
    
    private final DocRepository docRepository;
    
    public DocResource(DocService docService, DocRepository docRepository)
    {
        this.docService = docService;
        this.docRepository = docRepository;
    }
    
    /**
     * {@code POST  /document} : Create a new document.
     *
     * @param document the document to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new document, or with status {@code 400 (Bad Request)} if
     *         the document has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/{assetId}/documents")
    public ResponseEntity<ApiResponse<Document>> createDocument(
        @PathVariable(value = "assetId") Long assetId,
        @RequestBody Document document) throws URISyntaxException
    {
        log.debug("REST request to save Document : {}", document);
        if (document.getId() != null)
        {
            throw new BadRequestAlertException("A new document cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Document result = docService.save(document, assetId);
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PUT  /documents/:id} : Updates an existing document.
     *
     * @param id the id of the document to save.
     * @param document the document to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated document, or with status {@code 400 (Bad Request)} if the
     *         document is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the document couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    
    @PutMapping("/{assetId}/documents/{id}")
    public ResponseEntity<ApiResponse<Document>> updateDoc(
        @PathVariable(value = "assetId") Long assetId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Document document) throws URISyntaxException
    {
        log.debug("REST request to update Document : {}, {}", id, document);
        
        if (document.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, document.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!docRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Document result = docService.save(document, assetId);
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result), HttpStatus.OK);
    }
    
    /**
     * {@code PATCH  /documents/:id} : Partial updates given fields of an existing
     * document, field will ignore if it is null
     *
     * @param id the id of the document to save.
     * @param document the document to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated document, or with status {@code 400 (Bad Request)} if the
     *         document is not valid, or with status {@code 404 (Not Found)} if the
     *         document is not found, or with status
     *         {@code 500 (Internal Server Error)} if the document couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/documents/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApiResponse<Document>> partialUpdateDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Document document) throws URISyntaxException
    {
        log.debug("REST request to partial update Document partially : {}, {}", id, document);
        
        if (document.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, document.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!docRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Optional<Document> result = docService.partialUpdate(document);
        
        if (result.isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result.get()), HttpStatus.OK);
    }
    
    /**
     * {@code GET  /documents} : get all the documents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of documents in body.
     */
    @GetMapping("/{assetId}/documents")
    public List<Document> getAllDocs(@PathVariable(value = "assetId") Long assetId)
    {
        log.debug("REST request to get all Assets");
        return docService.findAll(assetId).get();
    }
    
    /**
     * {@code GET  /documents/:id} : get the "id" document.
     *
     * @param id the id of the document to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the document, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{assetId}/documents/{id}")
    public ResponseEntity<ApiResponse<Document>> getDoc(
        @PathVariable(value = "assetId") Long assetId,
        @PathVariable Long id)
    {
        log.debug("REST request to get Document : {}", id);
        Optional<Document> document = docService.findOne(id, assetId);
        
        if (document.isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, document.get()),
            HttpStatus.OK);
    }
    
    /**
     * 
     * {@code DELETE  /documents} : delete documents.
     *
     * @param assetId the assetId of the document to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{assetId}/documents")
    public ResponseEntity<ApiResponse<Void>> deleteDocs(
        @PathVariable(value = "assetId") Long assetId,
        @RequestParam List<Long> ids)
    {
        log.debug("REST request to delete Document : {}", ids);
        
        ids.stream().forEach(
            docId -> docService.delete(docId, assetId));
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.NO_CONTENT.value(), SUCCESSFULLY_MODIFIED,
                null),
            HttpStatus.NO_CONTENT);
    }
}
