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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.config.ApiResponse;
import com.siemens.domain.Asset;
import com.siemens.domain.Tag;
import com.siemens.dto.AssetDTO;
//import com.siemens.dto.AssetSectionUnitNameDTO;
//import com.siemens.dto.AssetSubHeaderSectionUnitNameDTO;
import com.siemens.exception.BadRequestAlertException;
import com.siemens.repository.AssetRepository;
import com.siemens.service.AssetService;

/**
 * REST controller for managing {@link com.siemens.domain.Asset}.
 */
@RestController
@RequestMapping(URLConstants.URL_API_ROOT + "/subHeader")
public class AssetResource
{
    
    private final Logger log = LoggerFactory.getLogger(AssetResource.class);
    
    private static final String SUCCESSFULLY_MODIFIED = "Successfully modified record.";
    
    private static final String ENTITY_NAME = "asset";
    
    private final AssetService assetService;
    
    private final AssetRepository assetRepository;
    
    public AssetResource(AssetService assetService, AssetRepository assetRepository)
    {
        this.assetService = assetService;
        this.assetRepository = assetRepository;
    }
    
    /**
     * {@code POST  /assets} : Create a new asset.
     *
     * @param asset the asset to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new asset, or with status
     *         {@code 400 (Bad Request)} if the asset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/{subHeaderId}/assets")
    public ResponseEntity<ApiResponse<Asset>> createAsset(
        @PathVariable(value = "subHeaderId") Long subHeaderId,
        @RequestBody Asset asset) throws URISyntaxException
    {
        log.debug("REST request to save Asset : {}", asset);
        if (asset.getId() != null)
        {
            throw new BadRequestAlertException("A new asset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Asset result = assetService.save(asset, subHeaderId);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PUT  /assets/:id} : Updates an existing asset.
     *
     * @param id the id of the asset to save.
     * @param asset the asset to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asset,
     *         or with status {@code 400 (Bad Request)} if the asset is not valid,
     *         or with status {@code 500 (Internal Server Error)} if the asset couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{subHeaderId}/assets/{id}")
    public ResponseEntity<ApiResponse<Asset>> updateAsset(
        @PathVariable(value = "subHeaderId") Long subHeaderId,
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Asset asset)
        throws URISyntaxException
    {
        log.debug("REST request to update Asset : {}, {}", id, asset);
        if (asset.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, asset.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!assetRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Asset result = assetService.save(asset, subHeaderId);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PATCH  /assets/:id} : Partial updates given fields of an existing asset, field will ignore if it is null
     *
     * @param id the id of the asset to save.
     * @param asset the asset to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated asset,
     *         or with status {@code 400 (Bad Request)} if the asset is not valid,
     *         or with status {@code 404 (Not Found)} if the asset is not found,
     *         or with status {@code 500 (Internal Server Error)} if the asset couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
	/*
	 * @PatchMapping(value = "/assets/{id}", consumes =
	 * "application/merge-patch+json") public ResponseEntity<ApiResponse<Asset>>
	 * partialUpdateAsset(@PathVariable(value = "id", required = false) final Long
	 * id,
	 * 
	 * @RequestBody Asset asset) throws URISyntaxException {
	 * log.debug("REST request to partial update Asset partially : {}, {}", id,
	 * asset); if (asset.getId() == null) { throw new
	 * BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull"); } if
	 * (!Objects.equals(id, asset.getId())) { throw new
	 * BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid"); }
	 * 
	 * if (!assetRepository.existsById(id)) { throw new
	 * BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"); }
	 * 
	 * Optional<Asset> result = assetService.partialUpdate(asset);
	 * 
	 * if (result.isEmpty()) { return new ResponseEntity<>( new
	 * ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
	 * HttpStatus.OK); }
	 * 
	 * return new ResponseEntity<>( new ApiResponse<>(HttpStatus.OK.value(),
	 * SUCCESSFULLY_MODIFIED, result.get()), HttpStatus.OK); }
	 */
    /**
     * {@code GET  /assets} : get all the assets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assets in body.
     */
    @GetMapping("/{subHeaderId}/assets")
    public List<Asset> getAllAssets(
        @PathVariable(value = "subHeaderId") Long subHeaderId)
    {
        log.debug("REST request to get all Assets");
        return assetService.findAll(subHeaderId).get();
    }
    
    /**
     * {@code GET  /assets/:id} : get the "id" asset.
     *
     * @param id the id of the asset to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asset, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{subHeaderId}/assets/{id}")
    public ResponseEntity<ApiResponse<Asset>> getAsset(
        @PathVariable(value = "subHeaderId") Long subHeaderId,
        @PathVariable Long id)
    {
        log.debug("REST request to get Asset : {}", id);
        Optional<Asset> asset = assetService.findOne(id, subHeaderId);
        
        if (asset.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, asset.get()),
            HttpStatus.OK);
    }
    
    /**
     * {@code DELETE  /assets/:id} : delete the "id" asset.
     *
     * @param id the id of the asset to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{subHeaderId}/assets/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAsset(
        @PathVariable(value = "subHeaderId") Long subHeaderId,
        @PathVariable Long id)
    {
        log.debug("REST request to delete Asset : {}", id);
        assetService.delete(id, subHeaderId);
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.NO_CONTENT.value(), SUCCESSFULLY_MODIFIED, null),
            HttpStatus.NO_CONTENT);
    }
    
    /**
     * {@code GET  /tag-live-data} : get asset-tag-live-data from node server .
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and config in body.
     */
	/*
	 * @SuppressWarnings("null")
	 * 
	 * @GetMapping("/asset-tag-live-data") public
	 * ResponseEntity<ApiResponse<List<AssetDTO>>> getAssetTagLiveData() {
	 * log.info("--- AssetResource: inside getAssetTagLiveData() method---- ");
	 * AssetDTO assetDtoObj = null; List<AssetDTO> assetDTOList = new ArrayList<>();
	 * 
	 * List<Asset> assets = assetService.findAllAssetsWithDocsTags().get();
	 * log.info("----- AssetResource-> assets : " + assets);
	 * 
	 * if (assets.isEmpty()) { return new ResponseEntity<>( new
	 * ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No records found.", null),
	 * HttpStatus.NO_CONTENT); }
	 * 
	 * Set<Tag> assetTagSet;
	 * 
	 * for (Asset asset : assets) { assetTagSet = asset.getTags();
	 * log.info("----- AssetResource-> assetTagSet : " + assetTagSet);
	 * 
	 * assetDtoObj = assetService.getAssetTagLiveDataNodeSrvr(asset, assetTagSet);
	 * log.info("----- AssetResource-> RETURn assetDtoObj :" + assetDtoObj);
	 * 
	 * assetDTOList.add(assetDtoObj); }
	 * 
	 * log.info("----- AssetResource-> assetDTOList : " + assetDTOList); return new
	 * ResponseEntity<>( new ApiResponse<>(HttpStatus.OK.value(),
	 * "Successfully fetched records", assetDTOList), HttpStatus.OK); }
	 */
    /**
     * {@code GET  /shared-tags-live-data} : get shared tags live-data from node server .
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and config in body.
     */
	/*
	 * @SuppressWarnings("null")
	 * 
	 * @GetMapping("/shared-tags-live-data") public
	 * ResponseEntity<ApiResponse<List<AssetDTO>>> getAssetSharedTagsLiveData(
	 * 
	 * @RequestParam String userNameTo) {
	 * log.info("--- AssetResource: GET getAssetSharedTagsLiveData() method---- ");
	 * List<AssetDTO> assetDTOList;
	 * 
	 * List<Asset> assets = assetService.findAllAssetsWithDocsTags().get();
	 * log.info("----- findAllAssetsWithDocsTags()-> assets : {}", assets);
	 * 
	 * if (assets.isEmpty()) { return new ResponseEntity<>( new
	 * ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No records found.", null),
	 * HttpStatus.OK); }
	 * 
	 * Set<Tag> assetTagSet = new HashSet<>();
	 * 
	 * assetDTOList = assetService.getSharedTagsLiveDataNodeSrvr(userNameTo,
	 * assetTagSet); log.info("----- AssetResource-> assetDTOList : " +
	 * assetDTOList);
	 * 
	 * if (assetDTOList.isEmpty())// i.e isComplete=true { return new
	 * ResponseEntity<>( new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
	 * "Reshare asset to see tag live data.", null), HttpStatus.OK); }
	 * 
	 * return new ResponseEntity<>( new ApiResponse<>(HttpStatus.OK.value(),
	 * "Successfully fetched records", assetDTOList), HttpStatus.OK); }
	 */
    
    /**
     * {@code GET  /assets/:id} : get the "id" asset.
     *
     * @param id the id of the asset to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the asset, or with status {@code 404 (Not Found)}.
     */
	/*
	 * @GetMapping("/assetkks-section-unit") public
	 * ResponseEntity<ApiResponse<AssetSectionUnitNameDTO>>
	 * getAssetKKSWithSectionUnitName(
	 * 
	 * @RequestParam String kksTag) {
	 * log.info("REST request to get Asset for kksTag: {}", kksTag);
	 * AssetSectionUnitNameDTO assetSectionUnitDtoObj =
	 * assetService.findAssetKKSWithSectionUnitName(kksTag);
	 * 
	 * if (Objects.isNull(assetSectionUnitDtoObj)) { return new
	 * ResponseEntity<>( new ApiResponse<>(HttpStatus.NO_CONTENT.value(),
	 * "No record found.", null), HttpStatus.OK); }
	 * 
	 * return new ResponseEntity<>( new ApiResponse<>(HttpStatus.OK.value(),
	 * "Successfully fetched record", assetSectionUnitDtoObj),
	 * HttpStatus.OK); }
	 */
}
