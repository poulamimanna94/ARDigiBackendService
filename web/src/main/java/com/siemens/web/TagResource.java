package com.siemens.web;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.siemens.config.ApiResponse;
import com.siemens.constant.Constants;
import com.siemens.domain.SharedTags;
import com.siemens.domain.Tag;
import com.siemens.dto.SharedTagsDTO;
import com.siemens.dto.UserNotificationDTO;
import com.siemens.exception.BadRequestAlertException;
import com.siemens.repository.TagRepository;
import com.siemens.service.TagService;

/**
 * REST controller for managing {@link com.siemens.domain.Tag}.
 */
@RestController
@RequestMapping(URLConstants.URL_API_ROOT + "/asset")
public class TagResource
{
    
    private final Logger log = LoggerFactory.getLogger(TagResource.class);
    
    private static final String ROWS_AFFECTED = "Successfully modified record. No of rows affected:";
    private static final String SUCCESSFULLY_MODIFIED = "Successfully modified record.";
    
    private static final String ENTITY_NAME = "tag";
    
    private final TagService tagService;
    
    private final TagRepository tagRepository;
    
    public TagResource(TagService tagService, TagRepository tagRepository)
    {
        this.tagService = tagService;
        this.tagRepository = tagRepository;
    }
    
    /**
     * {@code PATCH  /tags/:id} : Partial updates given fields of an existing
     * tag, field will ignore if it is null
     *
     * @param id the id of the tag to save.
     * @param tag the tag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated tag, or with status {@code 400 (Bad Request)} if the
     *         tag is not valid, or with status {@code 404 (Not Found)} if the
     *         tag is not found, or with status
     *         {@code 500 (Internal Server Error)} if the tag couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tags/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<ApiResponse<Tag>> partialUpdateTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Tag tag) throws URISyntaxException
    {
        log.debug("REST request to partial update Tag partially : {}, {}", id, tag);
        
        if (tag.getId() == null)
        {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tag.getId()))
        {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        
        if (!tagRepository.existsById(id))
        {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        
        Optional<Tag> result = tagService.partialUpdate(tag);
        
        if (result.isEmpty())
        {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result.get()), HttpStatus.OK);
    }
    
    /**
     * {@code GET  /tags} : get all the tags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of tags in body.
     */
    @GetMapping("/{assetId}/tags")
    public List<Tag> getAllTags(@PathVariable(value = "assetId") Long assetId)
    {
        log.debug("REST request to get all Assets");
        return tagService.findAll(assetId).get();
    }
    
    /**
     * {@code GET  /tags/:id} : get the "id" tag.
     *
     * @param id the id of the tag to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the tag, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{assetId}/tags/{id}")
    public ResponseEntity<ApiResponse<Tag>> getTag(
        @PathVariable(value = "assetId") Long assetId,
        @PathVariable Long id)
    {
        log.debug("REST request to get Tag : {}", id);
        Optional<Tag> tag = tagService.findOne(id, assetId);
        
        if (tag.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record modified.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, tag.get()),
            HttpStatus.OK);
    }
    
    /**
     * {@code DELETE  /tags} : delete tags & send req to Node OPC for tag subscription
     *
     * @param assetId the assetId of the tag to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{assetId}/tags")
    public ResponseEntity<ApiResponse<String>> deleteTags(
        @PathVariable(value = "assetId") Long assetId,
        @RequestParam List<Long> ids)
    {
        log.debug("REST request to delete Tag id(s) : {}", ids);
        
        ids.stream().forEach(
            tagId -> tagService.delete(tagId, assetId));
        
        String result = tagService.saveTagsForSubs();
        
        log.info("----- saveTagsForSubs() - result :" + result);
        
        if ((Constants.NODE_SERVER_ERROR).equalsIgnoreCase(result))
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error at server end..", result),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code POST  /tag} : Create new tags & send to Node OPC for subscription
     *
     * @param tag the tag to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new tag, or with status {@code 400 (Bad Request)} if
     *         the tag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/{assetId}/tags")
    public ResponseEntity<ApiResponse<String>> createTags(
        @PathVariable(value = "assetId") Long assetId,
        @RequestBody List<Tag> tags) throws URISyntaxException
    {
        log.info("REST request to save Tag : {}", tags);
        
        tags.stream().forEach(
            tag ->
            {
                if (tag.getId() != null)
                {
                    throw new BadRequestAlertException("A new tag cannot already have an ID", ENTITY_NAME, "idexists");
                }
                tagService.save(tag, assetId);
            });
        
        String result = tagService.saveTagsForSubs();
        
        log.info("----- saveTagsForSubs() - result :" + result);
        
        if ((Constants.NODE_SERVER_ERROR).equalsIgnoreCase(result))
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error at server end..", result),
                HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, result),
            HttpStatus.OK);
    }
    
    /**
     * {@code PUT  /tags} : Updates tags.
     *
     * @param assetId the assetId of the tag to save.
     * @param tag the tag to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated tag, or with status {@code 400 (Bad Request)} if the
     *         tag is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the tag couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{assetId}/tags")
    public ResponseEntity<ApiResponse<Void>> updateTags(
        @PathVariable(value = "assetId") Long assetId,
        @RequestBody List<Tag> tags) throws URISyntaxException
    {
        log.debug("REST request to update Tag : {}", tags);
        
        tags.stream().forEach(
            tag ->
            {
                if (tag.getId() == null)
                {
                    throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
                }
                if (!tagRepository.existsById(tag.getId()))
                {
                    throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
                }
                
                tagService.save(tag, assetId);
            });
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED,
                null),
            HttpStatus.OK);
    }
    
    /**
     * POST request to save shared tags
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * 
     */
    @PostMapping("/share-tags")
    public ResponseEntity<ApiResponse<SharedTagsDTO>> addSharedTags(
        @RequestParam Long assetId,
        @RequestParam String userNameFrom,
        @RequestParam String userNameTo) throws URISyntaxException
    {
        log.info("sharedTags() : POST request to save shared Tags :");
        
        SharedTags shTag = new SharedTags();
        shTag.setUserNameFrom(userNameFrom);
        shTag.setUserNameTo(userNameTo);
        
        SharedTagsDTO sTagDto = tagService.shareTagsPlantNavigation(assetId, shTag);
        log.info("----- sharedTags() - sTagDto :" + sTagDto);
        
        if (Objects.isNull(sTagDto))
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Tag already shared", sTagDto),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), SUCCESSFULLY_MODIFIED, sTagDto),
            HttpStatus.OK);
    }
    
    /**
     * {@code GET  /shared-tags/user-notifications} : get user notifications for shared tags
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of tags in body.
     */
    @GetMapping("/shared-tags/user-notifications")
    public ResponseEntity<ApiResponse<List<UserNotificationDTO>>> getUserNotifications(
        @RequestParam String userNameTo)
    {
        log.info("GET request to getUserNotifications() for user: {}", userNameTo);
        
        List<UserNotificationDTO> userNotifctnList = tagService.getUserNotificationSharedTag(userNameTo);
        log.info("---- userNotifctnList : {}", userNotifctnList);
        
        if (userNotifctnList.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), "No record found.", null),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "Successfully fetched record(s).", userNotifctnList),
            HttpStatus.OK);
    }
    
    /**
     * POST request to mark all shared tags as read for logged in user
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * 
     */
    @PostMapping("/shared-tags/mark-all-read")
    public ResponseEntity<ApiResponse<Integer>> sharedTagsHasReadAll(
        @RequestParam String userNameTo) throws URISyntaxException
    {
        log.info("sharedTagsHasRead() : POST request to mark all shared tags as read for userNameTo: {}", userNameTo);
        
        int rowsAffected = tagService.markAllSharedTagsAsRead(userNameTo);
        log.info(ROWS_AFFECTED + rowsAffected);
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), ROWS_AFFECTED, rowsAffected),
            HttpStatus.OK);
    }
    
    /**
     * POST request to mark a shared tag as read for logged in user
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * 
     */
    @PostMapping("/shared-tags/mark-one-read")
    public ResponseEntity<ApiResponse<Integer>> sharedTagsHasReadOne(
        @RequestParam Long id,
        @RequestParam String userNameTo) throws URISyntaxException
    {
        log.info("sharedTagsHasRead() : POST request to mark a shared tag as read for userNameTo: {}", userNameTo);
        
        int rowsAffected = tagService.markSharedTagAsRead(id, userNameTo);
        log.info(ROWS_AFFECTED + rowsAffected);
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), ROWS_AFFECTED, rowsAffected),
            HttpStatus.OK);
    }
    
    /**
     * POST request to mark shared asset as complete for logged in user
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonParseException
     * 
     */
    @PostMapping("/shared-tags/mark-complete")
    public ResponseEntity<ApiResponse<Integer>> sharedTagsIsComplete(
        @RequestParam Long assetId,
        @RequestParam String userNameTo) throws URISyntaxException
    {
        log.info("sharedTagsIsComplete() : POST request to mark shared tags as complete for userNameTo: {}", userNameTo);
        
        int rowsAffected = tagService.markSharedTagsAsComplete(assetId, userNameTo);
        log.info(ROWS_AFFECTED + rowsAffected);
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), ROWS_AFFECTED, rowsAffected),
            HttpStatus.OK);
    }
}
