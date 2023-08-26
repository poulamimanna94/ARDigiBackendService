package com.siemens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.siemens.domain.Tag;

/**
 * Spring Data SQL repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TagRepository extends JpaRepository<Tag, Long>
{
    
    Optional<List<Tag>> findByAssetId(Long assetId);
    
    Optional<Tag> findByIdAndAssetId(Long id, Long assetId);
    
    void deleteByIdAndAssetId(Long id, Long assetId);
    
    void deleteByAssetId(Long assetId);
    
    // test
    @Query(value = "SELECT * FROM tag", nativeQuery = true)
    List<Tag> findAllTagsNative();
}
