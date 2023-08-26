package com.siemens.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.siemens.domain.Asset;
import com.siemens.domain.Tag;
import com.siemens.dto.AssetDTO;
//import com.siemens.dto.AssetSubHeaderSectionUnitNameDTO;

/**
 * Service Interface for managing {@link Asset}.
 */
public interface AssetService
{
    /**
     * Save a asset.
     *
     * @param asset the entity to save.
     * @return the persisted entity.
     */
    Asset save(Asset asset, Long subHeaderId);
    
    /**
     * Partially updates a asset.
     *
     * @param asset the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Asset> partialUpdate(Asset asset);
    
    /**
     * Get all the assets.
     *
     * @return the list of entities.
     */
    Optional<List<Asset>> findAll(Long subHeaderId);
    
    /**
     * Get the "id" asset.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Asset> findOne(Long id, Long subHeaderId);
    
    /**
     * Delete the "id" asset.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, Long subHeaderId);
    
    /**
     * Get all the assets with its docs & tags
     *
     * @return the list of entities.
     */
    Optional<List<Asset>> findAllAssetsWithDocsTags();
    
    /**
     * Get tags live data from Node OPC.
     * 
     * @param asset
     * @param assetTagSet
     *
     * @return the Asset Dto with assets & related tags live data
     */
    AssetDTO getAssetTagLiveDataNodeSrvr(Asset asset, Set<Tag> assetTagSet);
    
    /**
     * Get Shared tags live data from Node OPC.
     * 
     * @param userNameTo
     * @param assetTagSet
     *
     * @return the Asset Dto with assets & related shared tags live data
     */
    List<AssetDTO> getSharedTagsLiveDataNodeSrvr(String userNameTo, Set<Tag> assetTagSet);
    
    /**
     * Get the "assetsKKSTags" asset.
     *
     * @param assetsKKSTag the name of the associated KKSTag of Asset.
     * @return the entity.
     */
 //   Optional<Asset> findByNameAndSectionId(String assetsKKSTags, Long sectionId);

  //  AssetSectionUnitNameDTO findAssetKKSWithSectionUnitName(String kksTag);
    
}
