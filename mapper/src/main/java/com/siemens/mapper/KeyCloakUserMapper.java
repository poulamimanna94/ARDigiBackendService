/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED.
 *
 * AR DIGI Companion
 *
 */

package com.siemens.mapper;

import com.siemens.dto.KeyCloakRoleVMDTO;
import com.siemens.dto.KeyCloakUserDTO;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface KeyCloakUserMapper
{
    KeyCloakUserDTO userRepresentationToKeyCloakUserDTO(UserRepresentation userRepresentation, List<KeyCloakRoleVMDTO> roles);
}

/*
 * Copyright (c) Siemens AG 2022 ALL RIGHTS RESERVED
 * AR DIGI Companion
 */
