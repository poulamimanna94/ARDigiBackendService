package com.siemens.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.siemens.domain.SubHeader;

/**
 * Spring Data SQL repository for the SubHeader entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubHeaderRepository extends JpaRepository<SubHeader, Long>
{
    
    Optional<List<SubHeader>> findBySectionId(Long sectionId);
    
    Optional<SubHeader> findByIdAndSectionId(Long id, Long sectionId);
    
    void deleteByIdAndSectionId(Long id, Long sectionId);

}
