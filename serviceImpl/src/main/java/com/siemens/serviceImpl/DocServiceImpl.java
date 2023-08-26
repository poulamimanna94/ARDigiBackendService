package com.siemens.serviceImpl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siemens.domain.Document;
import com.siemens.repository.AssetRepository;
import com.siemens.repository.DocRepository;
import com.siemens.service.DocService;

/**
 * Service Implementation for managing {@link Document}.
 */
@Service
@Transactional
public class DocServiceImpl implements DocService
{
    
    private final Logger log = LoggerFactory.getLogger(DocServiceImpl.class);
    
    private final DocRepository docRepository;
    
    @Autowired
    private AssetRepository assetRepository;
    
    public DocServiceImpl(DocRepository docRepository)
    {
        this.docRepository = docRepository;
    }
    
    @Override
    public Document save(Document document, Long assetId)
    {
        log.debug("Request to save Document : {}", document);
        
        return assetRepository
            .findById(assetId)
            .map(asset ->
            {
                document.setAsset(asset);
                document.setLastModifiedDate(Instant.now());
                return docRepository.save(document);
            })
            .orElseThrow(() -> new NullPointerException());
    }
    
    @Override
    public Optional<Document> partialUpdate(Document document)
    {
        log.debug("Request to partially update Document : {}", document);
        
        return docRepository
            .findById(document.getId())
            .map(
                existingDoc ->
                {
                    if (document.getDocName() != null)
                    {
                        existingDoc.setDocName(document.getDocName());
                    }
                    if (document.getDocLink() != null)
                    {
                        existingDoc.setDocLink(document.getDocLink());
                    }
                    if (document.getCreatedDate() != null)
                    {
                        existingDoc.setCreatedDate(document.getCreatedDate());
                    }
                    if (document.getLastModifiedDate() != null)
                    {
                        existingDoc.setLastModifiedDate(Instant.now());
                    }
                    
                    return existingDoc;
                })
            .map(docRepository::save);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<List<Document>> findAll(Long assetId)
    {
        log.debug("Request to get all Assets");
        return docRepository.findByAssetId(assetId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Document> findOne(Long id, Long assetId)
    {
        log.debug("Request to get Document : {}", id);
        return docRepository.findByIdAndAssetId(id, assetId);
    }
    
    @Override
    public void delete(Long id, Long assetId)
    {
        log.debug("Request to delete Document : {}", id);
        docRepository.deleteByIdAndAssetId(id, assetId);
    }
    
    @Override
    public void deleteByAssetId(Long assetId)
    {
        log.debug("Request to delete Docs : {}", assetId);
        docRepository.deleteByAssetId(assetId);
    }
}
