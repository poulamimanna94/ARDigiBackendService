package com.siemens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.siemens.domain.Asset;

/**
 * Spring Data SQL repository for the Asset entity.
 */
@Repository
public interface AssetRepository extends JpaRepository<Asset, Long>
{
    
    Optional<List<Asset>> findBySubHeaderId(Long subHeaderId);
    
    Optional<Asset> findByIdAndSubHeaderId(Long id, Long subHeaderId);
    
    void deleteByIdAndSubHeaderId(Long id, Long subHeaderId);
    
  //  Optional<Asset> findBykksTagAndSectionId(String assetName, Long sectionId);
    
   // Asset findByKksTag(String kksTag);

	/*
	 * @Query(value = "SELECT kks_tag FROM asset WHERE section_id = :sectionId",
	 * nativeQuery = true) List<String> findAssetKksTagsForSectionId(Long
	 * sectionId);
	 */
}
