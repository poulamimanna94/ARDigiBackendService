package com.siemens.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.siemens.domain.RolePermission;

/**
 * Spring Data JPA repository for the {@link RolePermission} entity.
 */
public interface AuthorityRepository extends JpaRepository<RolePermission, String>
{
}
