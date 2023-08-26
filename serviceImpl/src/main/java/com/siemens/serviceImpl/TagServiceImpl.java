package com.siemens.serviceImpl;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.siemens.constant.AuthoritiesConstants;
import com.siemens.constant.Constants;
import com.siemens.domain.SharedTags;
import com.siemens.domain.Tag;
import com.siemens.domain.TagSubscriptionId;
import com.siemens.dto.SharedTagsDTO;
import com.siemens.dto.UserNotificationDTO;
import com.siemens.mapper.SharedTagsMapper;
import com.siemens.repository.AssetRepository;
import com.siemens.repository.SharedTagsRepository;
import com.siemens.repository.TagRepository;
import com.siemens.service.TagService;

/**
 * Service Implementation for managing {@link Tag}.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService
{
    
    private final Logger log = LoggerFactory.getLogger(TagServiceImpl.class);
    
    private final TagRepository tagRepository;
    
    private final SharedTagsRepository shTagRepository;
    
    @Autowired
    private AssetRepository assetRepository;
    
    @Value(Constants.API_OPC_CONNECT_BASEURL)
    StringBuilder adminUri;
    
    @Value(Constants.API_OPC_CONNECT_BASEURL)
    String baseUrl;
    
    @Autowired
    private SharedTagsMapper sTagMapper;
    
    ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    TagSubscriptionId tagSubsIdObj;
    
    public TagServiceImpl(TagRepository tagRepository, SharedTagsRepository shTagRepository)
    {
        this.tagRepository = tagRepository;
        this.shTagRepository = shTagRepository;
    }
    
    @Override
    public Tag save(Tag tag, Long assetId)
    {
        log.debug("Request to save Tag : {}", tag);
        
        return assetRepository
            .findById(assetId)
            .map(asset ->
            {
                tag.setAsset(asset);
                tag.setLastModifiedDate(Instant.now());
                return tagRepository.save(tag);
            })
            .orElseThrow(() -> new NullPointerException());
    }
    
    @Override
    public Optional<Tag> partialUpdate(Tag tag)
    {
        log.debug("Request to partially update Tag : {}", tag);
        
        return tagRepository
            .findById(tag.getId())
            .map(
                existingDoc ->
                {
                    if (tag.getDisplayName() != null)
                    {
                        existingDoc.setDisplayName(tag.getDisplayName());
                    }
                    if (tag.getDesc() != null)
                    {
                        existingDoc.setDesc(tag.getDesc());
                    }
                    if (tag.getUnits() != null)
                    {
                        existingDoc.setUnits(tag.getUnits());
                    }
                    if (tag.getLowLow() != null)
                    {
                        existingDoc.setLowLow(tag.getLowLow());
                    }
                    if (tag.getLow() != null)
                    {
                        existingDoc.setLow(tag.getLow());
                    }
                    if (tag.getHighHigh() != null)
                    {
                        existingDoc.setHighHigh(tag.getHighHigh());
                    }
                    if (tag.getHigh() != null)
                    {
                        existingDoc.setHigh(tag.getHigh());
                    }
                    
                    return existingDoc;
                })
            .map(tagRepository::save);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Tag> findAllTagsInSys()
    {
        log.debug("Request to get all Tags in system");
        return tagRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<List<Tag>> findAll(Long assetId)
    {
        log.debug("Request to get all Assets");
        return tagRepository.findByAssetId(assetId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Tag> findOne(Long id, Long assetId)
    {
        log.debug("Request to get Tag : {}", id);
        return tagRepository.findByIdAndAssetId(id, assetId);
    }
    
    @Override
    public void delete(Long id, Long assetId)
    {
        log.debug("Request to delete Tag : {}", id);
        tagRepository.deleteByIdAndAssetId(id, assetId);
    }
    
    @Override
    public void deleteByAssetId(Long assetId)
    {
        log.debug("Request to delete Tags : {}", assetId);
        tagRepository.deleteByAssetId(assetId);
    }
    
    // saveTagsForSubs(Tag tag, Long assetId)
    @Override
    public String saveTagsForSubs()
    {
        log.info("--- inside saveTagsForSubs() ---");
        
        adminUri = new StringBuilder();
        adminUri.append(baseUrl);
        
        String postUserReqParam = null;
        HttpResponse response = null;
        
        adminUri.append(Constants.FORWARDSLASH)
            .append(Constants.API_OPC_ADD_REMOVE_TAGS);
        
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(adminUri.toString());
            HttpPost post = getPostOPCApi(builder.build());
            
            List<Tag> allTags = findAllTagsInSys();
            
            List<TagSubscriptionId> subsIdList = new ArrayList<>();
            
            for (Tag tag_i : allTags)
            {
                tagSubsIdObj = new TagSubscriptionId();
                tagSubsIdObj.setSubscriptionName(tag_i.getSubscriptionName());
                subsIdList.add(tagSubsIdObj);
            }
            
            log.info("--- subsIdList : ---" + subsIdList.toString());
            
            postUserReqParam = objectMapper.writeValueAsString(subsIdList);
            HttpEntity stringEntity = new StringEntity(postUserReqParam, ContentType.APPLICATION_JSON);
            log.info("--- saveTagsForSubs() --- stringEntity:" + stringEntity);
            
            post.setEntity(stringEntity);
            log.info("--- saveTagsForSubs() --- post:" + post);
            
            response = invokePostOPCApi(post);
            int httpStatus = response.getStatusLine().getStatusCode();
            log.info("--- saveTagsForSubs() --- httpStatus:" + httpStatus);
            
            if ((Constants.HTTP_SUCCESS_CODE) == httpStatus)
            {
                return postUserReqParam;
            }
            else
            {
                return Constants.NODE_SERVER_ERROR;
            }
            
        }
        catch (URISyntaxException | IOException e)
        {
            log.error(e.getMessage());
        }
        
        return postUserReqParam;
    }
    
    public HttpPost getPostOPCApi(URI uri)
    {
        
        HttpPost post = new HttpPost(uri);
        post.setHeader(AuthoritiesConstants.USER_AGENT,
            AuthoritiesConstants.MOZILLA_WINDOWS_CHROME_SAFARI);
        
        return post;
    }
    
    public HttpResponse invokePostOPCApi(HttpPost post)
    {
        HttpResponse response = null;
        HttpClient client = HttpClientBuilder.create().build();
        
        try
        {
            response = client.execute(post);
            log.info("--- invokePostOPCApi() response:" + response);
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
        }
        
        return response;
    }
    
    @Override
    public SharedTagsDTO shareTagsPlantNavigation(Long assetId, SharedTags shTag)
    {
        log.info("shareTagsPlantNavigation(): Request to save SharedTags for userNameTo: {}", shTag.getUserNameTo());
        
        SharedTagsDTO sTagDtoObj = null;
        
        List<SharedTags> shTagsList = reshareTagCheck(assetId, shTag.getUserNameTo());
        log.info("shTagsList : {}", shTagsList);
        
        if (shTagsList.isEmpty())
        {
            log.info("Reshare possible....");
            
            SharedTags sTag = assetRepository
                .findById(assetId)
                .map(asset ->
                {
                    shTag.setAsset(asset);
                    shTag.setLastModifiedDate(Instant.now());
                    return shTagRepository.save(shTag);
                })
                .orElseThrow(() -> new NullPointerException());
            log.info("sTag : {}", sTag);
            
            sTagDtoObj = sTagMapper.sharedTagModelToDTO(sTag, assetId);
            log.info("Final sTagDtoObj : {}", sTagDtoObj);
            
            return sTagDtoObj;
        }
        
        return sTagDtoObj;
    }
    
    // to get details of all SharedTags in sys
    @Override
    @Transactional(readOnly = true)
    public List<SharedTags> findAllSharedTagsInSys()
    {
        log.debug("findAllSharedTagsInSys(): Request to get all SharedTags in system");
        return shTagRepository.findAll();
    }
    
    @Override
    public List<UserNotificationDTO> getUserNotificationSharedTag(String userNameTo)
    {
        log.info("----getUserNotificationSharedTag(): Request to get SharedTags User Notifications for userNameTo: {}", userNameTo);
        
        List<SharedTags> sTagsUserNameToList = findSharedTagsByUserNameTo(userNameTo);
        log.info("----sTagsUserNameToList : {}", sTagsUserNameToList);
        
        Long sTagAssetId;
        String sTagAssetKksTag;
        List<UserNotificationDTO> usrNotifyList = new ArrayList<>();
        
        for (SharedTags sTag : sTagsUserNameToList)
        {
            sTagAssetId = sTag.getAsset().getId();
            log.info("----sTagAssetId : {}", sTagAssetId);
            
            sTagAssetKksTag = sTag.getAsset().getKksTag();
            log.info("----sTagAssetKksTag : {}", sTagAssetKksTag);
            
            // only show notification for tags for which isComplete=false, hasRead=false
            log.info("sTag.getIsComplete(): {}", sTag.getIsComplete());
            log.info("sTag.getHasRead(): {}", sTag.getHasRead());
            
            if (sTag.getIsComplete() == false && sTag.getHasRead() == false)
            {
                UserNotificationDTO usrNotfctnDtoObj = sTagMapper.sharedTagsToUserNotificationDTO(sTag, sTagAssetId, sTagAssetKksTag);
                log.info("----usrNotfctnDtoObj : {}", usrNotfctnDtoObj);
                
                usrNotifyList.add(usrNotfctnDtoObj);
            }
        }
        log.info("----FINAL usrNotifyList : {}", usrNotifyList);
        
        return usrNotifyList;
    }
    
    @Override
    public Optional<List<SharedTags>> findSharedTagsByAssetId(Long assetId)
    {
        log.info("----findSharedTagsByAssetId(): Request to get SharedTags for assetId: {}", assetId);
        
        return shTagRepository.findByAssetId(assetId);
    }
    
    @Override
    public List<SharedTags> findSharedTagsByUserNameTo(String userNameTo)
    {
        log.info("----findSharedTagsByUserNameTo(): Request to get SharedTags for userNameTo: {}", userNameTo);
        
        return shTagRepository.findByUserNameTo(userNameTo);
    }
    
    // demo
    @Override
    @Transactional(readOnly = true)
    public List<Tag> findTagsNativeSql()
    {
        log.debug("Request to get all Tags using native SQL");
        return tagRepository.findAllTagsNative();
    }
    
    @Override
    public int markAllSharedTagsAsRead(String userNameTo)
    {
        log.debug("Request to mark shared tags as read using native SQL");
        return shTagRepository.markAllSharedTagsAsRead(userNameTo);
    }
    
    @Override
    public int markSharedTagAsRead(Long id, String userNameTo)
    {
        log.debug("Request to mark a shared tag as read using native SQL");
        return shTagRepository.markSharedTagAsRead(id, userNameTo);
    }
    
    @Override
    public int markSharedTagsAsComplete(Long assetId, String userNameTo)
    {
        log.debug("Request to mark shared tags as complete using native SQL");
        return shTagRepository.markSharedTagsAsComplete(assetId, userNameTo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SharedTags> reshareTagCheck(Long assetId, String userNameTo)
    {
        log.debug("Request to check if a tag is already shared using native SQL");
        return shTagRepository.reshareTagCheck(assetId, userNameTo);
    }
}
