package com.siemens.service;

import java.util.List;
import java.util.Optional;

import com.siemens.domain.SharedTags;
import com.siemens.domain.Tag;
import com.siemens.dto.SharedTagsDTO;
import com.siemens.dto.UserNotificationDTO;

/**
 * Service Interface for managing {@link Tag}.
 */
public interface TagService
{
    /**
     * Save a tag.
     *
     * @param tag the entity to save.
     * @return the persisted entity.
     */
    Tag save(Tag tag, Long assetId);
    
    /**
     * Partially updates a tag.
     *
     * @param tag the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Tag> partialUpdate(Tag tag);
    
    /**
     * Get all the tags by assetId.
     *
     * @return the list of entities.
     */
    Optional<List<Tag>> findAll(Long assetId);
    
    /**
     * Get the "id" tag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Tag> findOne(Long id, Long assetId);
    
    /**
     * Delete the "id" tag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id, Long assetId);
    
    /**
     * Delete multiple tag by "assetId".
     *
     * @param assetId the assetId of the entity.
     */
    void deleteByAssetId(Long assetId);
    
    /**
     * save Tags to subscribe
     *
     * @param assetId the assetId of the entity.
     */
    String saveTagsForSubs();
    
    /**
     * Get all the tags saved in sys.
     *
     * @return the list of entities.
     */
    List<Tag> findAllTagsInSys();
    
    /**
     * Share the tag from Plant Navigation section
     * 
     * @param assetId
     * @param shTag
     * 
     * @return the shared tag response
     */
    SharedTagsDTO shareTagsPlantNavigation(Long assetId, SharedTags shTag);
    
    /**
     * Get all the shared tags saved in sys.
     *
     * @return the list of entities.
     */
    List<SharedTags> findAllSharedTagsInSys();
    
    /**
     * Get user notification of shared tags
     * 
     * @param userNameTo
     * 
     * @return user notifications dto
     */
    List<UserNotificationDTO> getUserNotificationSharedTag(String userNameTo);
    
    /**
     * Get all the shared tags for an assetId.
     * 
     * @param assetId
     *
     * @return the list of entities.
     */
    Optional<List<SharedTags>> findSharedTagsByAssetId(Long assetId);
    
    /**
     * Get all the shared tags for userNameTo(shared user).
     * 
     * @param userNameTo
     *
     * @return the list of entities.
     */
    List<SharedTags> findSharedTagsByUserNameTo(String userNameTo);
    
    /**
     * Get all tag using native query -demo
     *
     * @param tag the entity to update partially.
     * @return the persisted entity.
     */
    List<Tag> findTagsNativeSql();
    
    /**
     * Mark all shared tags as read for logged-in user.
     *
     * @param assetId shared assetId
     * @param userNameTo logged-in user
     * @return no of rows affected in db
     */
    int markAllSharedTagsAsRead(String userNameTo);
    
    /**
     * Mark a shared tag as read for logged-in user.
     *
     * @param id shared tag id
     * @param userNameTo logged-in user
     * @return no of rows affected in db
     */
    int markSharedTagAsRead(Long id, String userNameTo);
    
    /**
     * Mark shared asset as complete for logged-in user.
     *
     * @param assetId shared assetId
     * @param userNameTo logged-in user
     * @return no of rows affected in db
     */
    int markSharedTagsAsComplete(Long assetId, String userNameTo);
    
    List<SharedTags> reshareTagCheck(Long assetId, String userNameTo);
}
