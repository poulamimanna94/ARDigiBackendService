package com.siemens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.siemens.domain.SharedTags;

/**
 * Spring Data SQL repository for the Tag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SharedTagsRepository extends JpaRepository<SharedTags, Long>
{
    Optional<List<SharedTags>> findByAssetId(Long assetId);
    
    List<SharedTags> findByUserNameTo(String userNameTo);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE shared_tags SET has_read = true WHERE user_name_to = :userNameTo", nativeQuery = true)
    int markAllSharedTagsAsRead(@Param("userNameTo") String userNameTo);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE shared_tags SET has_read = true WHERE id = :id AND user_name_to = :userNameTo", nativeQuery = true)
    int markSharedTagAsRead(@Param("id") Long id, @Param("userNameTo") String userNameTo);
    
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE shared_tags SET is_complete = true WHERE asset_id = :assetId AND user_name_to = :userNameTo", nativeQuery = true)
    int markSharedTagsAsComplete(@Param("assetId") Long assetId, @Param("userNameTo") String userNameTo);
    
    @Query(value = "SELECT * FROM shared_tags WHERE asset_id = :assetId AND user_name_to = :userNameTo AND is_complete = false", nativeQuery = true)
    List<SharedTags> reshareTagCheck(@Param("assetId") Long assetId, @Param("userNameTo") String userNameTo);
    
    @Query(value = "SELECT * FROM shared_tags WHERE user_name_to = :userNameTo AND is_complete = false ORDER BY id DESC", nativeQuery = true)
    List<SharedTags> findSharedTagsForLiveData(@Param("userNameTo") String userNameTo);
}
