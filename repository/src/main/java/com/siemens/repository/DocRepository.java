package com.siemens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siemens.domain.Document;

/**
 * Spring Data SQL repository for the Document entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocRepository extends JpaRepository<Document, Long>
{
    
    Optional<List<Document>> findByAssetId(Long assetId);
    
    Optional<Document> findByIdAndAssetId(Long id, Long assetId);
    
    void deleteByIdAndAssetId(Long id, Long assetId);
    
    void deleteByAssetId(Long assetId);
    
}
