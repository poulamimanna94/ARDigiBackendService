package com.siemens.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.constant.AuthoritiesConstants;
import com.siemens.constant.Constants;
import com.siemens.constant.KeyCloakConstants;
import com.siemens.domain.Asset;
import com.siemens.domain.SharedTags;
import com.siemens.domain.Tag;
import com.siemens.domain.TagLiveData;
import com.siemens.domain.TagSubscriptionId;
import com.siemens.dto.AssetDTO;
//import com.siemens.dto.AssetSectionUnitNameDTO;
//import com.siemens.dto.AssetSubHeaderSectionUnitNameDTO;
import com.siemens.dto.TagDTO;
import com.siemens.mapper.AssetMapper;
import com.siemens.mapper.TagMapper;
import com.siemens.repository.AssetRepository;
//import com.siemens.repository.SectionRepository;
import com.siemens.repository.SharedTagsRepository;
import com.siemens.repository.SubHeaderRepository;
import com.siemens.service.AssetService;

/**
 * Service Implementation for managing {@link Asset}.
 */
@Service
@Transactional
public class AssetServiceImpl implements AssetService
{
    private final Logger log = LoggerFactory.getLogger(AssetServiceImpl.class);
    
    private final AssetRepository assetRepository;
    
	/*
	 * @Autowired private SectionRepository sectionRepository;
	 */
    
    @Autowired
    private SubHeaderRepository subHeaderRepository;
    
    @Autowired
    private SharedTagsRepository shTagRepository;
    
    @Value(Constants.API_OPC_CONNECT_BASEURL)
    StringBuilder adminUri;
    
    @Value(Constants.API_OPC_CONNECT_BASEURL)
    String baseUrl;
    
    @Autowired
    private TagMapper tagMapper;
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    private final Path root = Paths.get("opc-res");
    
    @Autowired
    TagSubscriptionId tagSubsIdObj;
    
    @Autowired
    private AssetMapper assetMapper;
    
    public AssetServiceImpl(AssetRepository assetRepository)
    {
        this.assetRepository = assetRepository;
    }
    
    @Override
    public Asset save(Asset asset, Long subHeaderId)
    {
        log.debug("Request to save Asset : {}", asset);
        
        return subHeaderRepository.findById(subHeaderId).map(subHeader ->
        {
            asset.setSubHeader(subHeader);
            asset.setLastModifiedDate(Instant.now());
            return assetRepository.save(asset);
        }).orElseThrow(() -> new NullPointerException());
    }
    
    @Override
    public Optional<Asset> partialUpdate(Asset asset)
    {
        log.debug("Request to partially update Asset : {}", asset);
        
        return assetRepository
            .findById(asset.getId())
            .map(
                existingAsset ->
                {
                    if (asset.getAssetType() != null)
                    {
                        existingAsset.setAssetType(asset.getAssetType());
                    }
                    if (asset.getElevation() != null)
                    {
                        existingAsset.setElevation(asset.getElevation());
                    }
                    if (asset.getX() != null)
                    {
                        existingAsset.setX(asset.getX());
                    }
                    if (asset.getY() != null)
                    {
                        existingAsset.setY(asset.getY());
                    }
                    if (asset.getZ() != null)
                    {
                        existingAsset.setZ(asset.getZ());
                    }
                    if (asset.getCreatedDate() != null)
                    {
                        existingAsset.setCreatedDate(asset.getCreatedDate());
                    }
                    if (asset.getLastModifiedDate() != null)
                    {
                        existingAsset.setLastModifiedDate(Instant.now());
                    }
                    
                    return existingAsset;
                })
            .map(assetRepository::save);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<List<Asset>> findAll(Long subHeaderId)
    {
        log.debug("Request to get all Assets");
        return assetRepository.findBySubHeaderId(subHeaderId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Asset> findOne(Long id, Long subHeaderId)
    {
        log.debug("Request to get Asset : {}", id);
        return assetRepository.findByIdAndSubHeaderId(id, subHeaderId);
    }
    
    @Override
    public void delete(Long id, Long subHeaderId)
    {
        log.debug("Request to delete Asset : {}", id);
        assetRepository.deleteByIdAndSubHeaderId(id, subHeaderId);
    }
    
    @Override
    public Optional<List<Asset>> findAllAssetsWithDocsTags()
    {
        log.debug("Request to get all Assets with its Docs & Tags");
        return Optional.of(assetRepository.findAll());
    }
    
    @Override
    public AssetDTO getAssetTagLiveDataNodeSrvr(Asset asset, Set<Tag> assetTagSet)
    {
        log.info("Inside getAssetTagLiveDataNodeSrvr() :");
        
        adminUri = new StringBuilder();
        adminUri.append(baseUrl);
        
        HttpResponse response;
        
        Map<String, TagLiveData> tagLDList = null;
        
        AssetDTO assetDtoObj = null;
        
        String statusCode = "";
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(Constants.API_OPC_GET_TAG_LIVEDATA);
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            response = invokeGetOPCApi(builder.build());
            log.info("returned response from invokeGetOPCApi() :" + response);
            
            InputStream reqContent;
            reqContent = response.getEntity().getContent();
            log.info("reqContent: " + reqContent.toString());
            
            tagLDList = objectMapper.readValue(reqContent, new TypeReference<Map<String, TagLiveData>>()
            {
            });
            log.info("tagLDList: " + tagLDList);
            
            if (!Files.exists(root))
            {
                Files.createDirectory(root);
            }
            objectMapper.writeValue(new File("opc-res/opc-tag-live-data-formatted.json"), tagLDList);
            
            // List<Tag> allTagsList = findAllTagsInSys(); -- using set<tag> param
            
            List<TagDTO> tagDtoList = new ArrayList<>();
            Double tagLiveValue;
            List<String> statusCodeList = new ArrayList<>();
            
            for (Tag tag_t : assetTagSet)
            {
                TagLiveData tagLiveDataObj = tagLDList.get(tag_t.getSubscriptionName());
                log.info("------ tag_t -> tagLiveDataObj: " + tagLiveDataObj);
                
                tagLiveValue = tagLiveDataObj.getValue();
                log.info("------ tagLiveValue USING tagLDList: " + tagLiveValue);
                
                if (Objects.nonNull(tagLiveDataObj) && Objects.nonNull(tagLiveValue))
                {
                    log.info("----tagLiveDataObj: " + tagLiveDataObj + " for key: " + tag_t.getSubscriptionName());
                    
                    statusCode = getTagAlarmStatusCode(tag_t, tagLiveValue);
                    log.info("------ statusCode: " + statusCode);
                    
                    TagDTO tagDtoObj = tagMapper.tagModelToDTO(tag_t, tagLiveDataObj, statusCode);
                    log.info("------ tagDtoObj: " + tagDtoObj);
                    
                    tagDtoList.add(tagDtoObj);
                    statusCodeList.add(statusCode);
                }
            }
            log.info("------ tagDtoList: " + tagDtoList);
            log.info("------ statusCodeList: " + statusCodeList);
            
            String assetStatusCode = "";
            
            if (statusCodeList.isEmpty() || tagDtoList.isEmpty())
                assetStatusCode = Constants.ALARM_NORMAL;
            if (statusCodeList.contains(Constants.ALARM_NORMAL)
                && (!statusCodeList.contains(Constants.ALARM_DANGER) || !statusCodeList.contains(Constants.ALARM_WARNING)))
                assetStatusCode = Constants.ALARM_NORMAL;
            if (statusCodeList.contains(Constants.ALARM_WARNING) && !statusCodeList.contains(Constants.ALARM_DANGER))
                assetStatusCode = Constants.ALARM_WARNING;
            if (statusCodeList.contains(Constants.ALARM_DANGER))
                assetStatusCode = Constants.ALARM_DANGER;
            
            log.info("------ ASSET assetStatusCode: " + assetStatusCode);
            
            assetDtoObj = assetMapper.assetModelToDTO(asset, tagDtoList, assetStatusCode);
            log.info("------AssetServiceImpl, assetDtoObj: " + assetDtoObj);
            // } // end of else
        }
        catch (URISyntaxException | UnsupportedOperationException | IOException e)
        {
            log.info(e.getMessage());
        }
        return assetDtoObj;
    }
    
    public HttpResponse invokeGetOPCApi(URI uri)
    {
        HttpResponse response = null;
        
        try
        {
            HttpClient client = HttpClientBuilder.create().build();
            
            HttpGet get = new HttpGet(uri);
            get.setHeader(AuthoritiesConstants.USER_AGENT,
                KeyCloakConstants.USER_AGENTS);
            
            response = client.execute(get);
        }
        catch (IOException e)
        {
            log.debug(e.getMessage());
            
        }
        
        return response;
    }
    
    private String getTagAlarmStatusCode(Tag tag_t, Double tagLiveValue)
    {
        if (tagLiveValue <= tag_t.getLowLow() || tagLiveValue >= tag_t.getHighHigh())
            return Constants.ALARM_DANGER;
        if (tagLiveValue <= tag_t.getLow() || tagLiveValue >= tag_t.getHigh())
            return Constants.ALARM_WARNING;
        if (tagLiveValue > tag_t.getLow() || tagLiveValue < tag_t.getHigh())
            return Constants.ALARM_NORMAL;
        else
            return "";
    }
    
    @Override
    public List<AssetDTO> getSharedTagsLiveDataNodeSrvr(String userNameTo, Set<Tag> assetTagSet)
    {
        log.info("Inside getSharedTagsLiveDataNodeSrvr() :");
        
        adminUri = new StringBuilder();
        adminUri.append(baseUrl);
        
        HttpResponse response;
        
        Map<String, TagLiveData> tagLDList = null;
        
        List<AssetDTO> assetDTOList = new ArrayList<>();
        AssetDTO assetDtoObj = null;
        
        String statusCode = "";
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(Constants.API_OPC_GET_TAG_LIVEDATA);
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            response = invokeGetOPCApi(builder.build());
            log.info("returned response from invokeGetOPCApi() :" + response);
            
            InputStream reqContent;
            reqContent = response.getEntity().getContent();
            log.info("reqContent: " + reqContent.toString());
            
            tagLDList = objectMapper.readValue(reqContent, new TypeReference<Map<String, TagLiveData>>()
            {
            });
            log.info("tagLDList: " + tagLDList);
            
            // dift approach for ths use case
            List<SharedTags> sTagsUserNameToList = findSharedTagsForLiveData(userNameTo);// shared tags for which isComplete=false
            log.info("----sTagsUserNameToList : {}", sTagsUserNameToList);
            
            Long sTagAssetId;
            Asset sTagAsset;
            
            List<TagDTO> tagDtoList;
            Double tagLiveValue;
            List<String> statusCodeList;
            
            for (SharedTags sTag : sTagsUserNameToList)
            {
                sTagAssetId = sTag.getAsset().getId();
                log.info("----sTagAssetId : {}", sTagAssetId);
                
                sTagAsset = sTag.getAsset();
                log.info("----sTagAsset : {}", sTagAsset);
                log.info("sTag.getIsComplete(): {}", sTag.getIsComplete());
                
                assetTagSet = sTag.getAsset().getTags();
                log.info("------> assetTagSet : " + assetTagSet);
                
                tagDtoList = new ArrayList<>();
                statusCodeList = new ArrayList<>();
                
                for (Tag tag_t : assetTagSet)
                {
                    TagLiveData tagLiveDataObj = tagLDList.get(tag_t.getSubscriptionName());
                    log.info("------ tag_t -> tagLiveDataObj: " + tagLiveDataObj);
                    
                    tagLiveValue = tagLiveDataObj.getValue();
                    log.info("------ tagLiveValue USING tagLDList: " + tagLiveValue);
                    
                    if (Objects.nonNull(tagLiveDataObj) && Objects.nonNull(tagLiveValue))
                    {
                        log.info("----tagLiveDataObj: " + tagLiveDataObj + " for key: " + tag_t.getSubscriptionName());
                        
                        statusCode = getTagAlarmStatusCode(tag_t, tagLiveValue);
                        log.info("------ statusCode: " + statusCode);
                        
                        TagDTO tagDtoObj = tagMapper.tagModelToDTO(tag_t, tagLiveDataObj, statusCode);
                        log.info("------ tagDtoObj: " + tagDtoObj);
                        
                        tagDtoList.add(tagDtoObj);
                        statusCodeList.add(statusCode);
                    }
                }
                log.info("------ tagDtoList: " + tagDtoList);
                log.info("------ statusCodeList: " + statusCodeList);
                
                String assetStatusCode = "";
                
                if (statusCodeList.isEmpty() || tagDtoList.isEmpty())
                    assetStatusCode = Constants.ALARM_NORMAL;
                if (statusCodeList.contains(Constants.ALARM_NORMAL)
                    && (!statusCodeList.contains(Constants.ALARM_DANGER) || !statusCodeList.contains(Constants.ALARM_WARNING)))
                    assetStatusCode = Constants.ALARM_NORMAL;
                if (statusCodeList.contains(Constants.ALARM_WARNING) && !statusCodeList.contains(Constants.ALARM_DANGER))
                    assetStatusCode = Constants.ALARM_WARNING;
                if (statusCodeList.contains(Constants.ALARM_DANGER))
                    assetStatusCode = Constants.ALARM_DANGER;
                
                log.info("------ ASSET assetStatusCode: " + assetStatusCode);
                
                assetDtoObj = assetMapper.assetModelToDTO(sTagAsset, tagDtoList, assetStatusCode);
                log.info("------AssetServiceImpl, assetDtoObj: " + assetDtoObj);
                
                assetDTOList.add(assetDtoObj);
                
            }
            log.info("Final assetDTOList : " + assetDTOList);
        }
        catch (URISyntaxException | UnsupportedOperationException | IOException e)
        {
            log.info(e.getMessage());
        }
        return assetDTOList;// retrun its list from here
    }
    
    // rqd for abv use case
    public List<SharedTags> findSharedTagsForLiveData(String userNameTo)
    {
        log.info("----findSharedTagsForLiveData(): Request to get SharedTags for userNameTo: {}", userNameTo);
        
        return shTagRepository.findSharedTagsForLiveData(userNameTo);
    }
    
	/*
	 * public Optional<Asset> findByNameAndSectionId(String assetsKKSTags, Long
	 * sectionId) { log.debug("Request to get Asset : {}", assetsKKSTags);
	 * Optional<Asset> unitData =
	 * assetRepository.findBykksTagAndSectionId(assetsKKSTags, sectionId);
	 * 
	 * return unitData; }
	 */
    
	/*
	 * @Override public AssetSectionUnitNameDTO
	 * findAssetKKSWithSectionUnitName(String kksTag) {
	 * log.debug("Request to get Asset, their section and unit for kksTag: {}",
	 * kksTag); AssetSectionUnitNameDTO assetDtlsDtoObj = null;
	 * 
	 * Asset asset = assetRepository.findByKksTag(kksTag); if
	 * (Objects.nonNull(asset)) { String subHeaderName =
	 * asset.getSubHeader().getSubHeaderName(); String sectionName =
	 * asset.getSubHeader().getSection().getSectionName(); String unitName =
	 * asset.getSubHeader().getSection().getUnit().getUnitName();
	 * 
	 * log.info("asset: {}", asset); log.info("subHeaderName: {}", subHeaderName);
	 * log.info("sectionName: {}", sectionName); log.info("unitName: {}", unitName);
	 * 
	 * assetDtlsDtoObj = assetMapper.assetToSectionUnitNameDTO(asset,
	 * sectionName, unitName); } return assetDtlsDtoObj; }
	 */
}
