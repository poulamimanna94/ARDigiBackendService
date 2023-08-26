package com.siemens.mapper;

import org.keycloak.representations.idm.RoleRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.siemens.domain.Permission;
import com.siemens.dto.KeyCloakRoleVMDTO;

@Mapper(componentModel = "spring")
public interface KeyCloakRolePermissionMapper
{
    @Mapping(source = "roleRepresentation.id", target = "id")
    KeyCloakRoleVMDTO roleRepresentationTKeyCloakRoleVM(RoleRepresentation roleRepresentation, Permission permission);
}
