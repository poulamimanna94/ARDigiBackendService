package com.siemens.repository;

import com.siemens.domain.Header;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Section entity.
 */
@Repository
public interface HeaderRepository extends JpaRepository<Header, Long>
{
    
    Optional<List<Header>> findByUnitId(Long unitId);
    
    Optional<Header> findByIdAndUnitId(Long id, Long unitId);
    
    void deleteByIdAndUnitId(Long id, Long unitId);
    
    Optional<Header> findBySectionName(String sectionName);
    
    Optional<Header> findBySectionNameAndUnitId(String sectionName, Long unitId);

    @Query(value = "SELECT section_name FROM section WHERE unit_id = :unitId", nativeQuery = true)
    List<String> findSectionNamesForUnitId(Long unitId);
    
}
