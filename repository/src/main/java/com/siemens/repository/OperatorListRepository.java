package com.siemens.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.siemens.domain.OperatorListView;

/**
 * Spring Data SQL repository for the Unit entity.
 */
@Repository
public interface OperatorListRepository extends JpaRepository<OperatorListView, Long>
{
    
    @Query(value = "SELECT * FROM operatorlist_temp ORDER BY last_login DESC", nativeQuery = true)
    List<OperatorListView> findOperatorsWithAvg();
}
