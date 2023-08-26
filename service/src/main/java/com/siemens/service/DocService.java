package com.siemens.service;

import java.util.List;
import java.util.Optional;

import com.siemens.domain.Document;

/**
 * Service Interface for managing {@link Document}.
 */
public interface DocService
{
    /**
     * Save a document.
     *
     * @param document the entity to save.
     * @return the persisted entity.
     */
    Document save(Document document, Long assetId);
    
    /**
     * Partially updates a document.
     *
     * @param document the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Document> partialUpdate(Document document);
    
    /**
     * Get all the documents.
     *
     * @return the list of entities.
     */
    Optional<List<Document>> findAll(Long assetId);
    
    /**
     * Get the "id" document.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Document> findOne(Long id, Long assetId);
    
    /**
     * Delete the "id" document.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, Long assetId);
    
    /**
     * Delete multiple doc by "assetId".
     *
     * @param assetId the assetId of the entity.
     */
    void deleteByAssetId(Long assetId);
}
