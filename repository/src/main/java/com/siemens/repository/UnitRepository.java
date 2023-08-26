package com.siemens.repository;

import com.siemens.domain.Unit;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Unit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long>
{
    Optional<Unit> findByUnitName(String unitName);
}
