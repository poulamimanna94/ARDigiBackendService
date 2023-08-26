package com.siemens.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.siemens.config.ApiResponse;
import com.siemens.constant.AuthoritiesConstants;
import com.siemens.constant.Constants;
import com.siemens.constant.KeyCloakConstants;
import com.siemens.domain.Permission;
import com.siemens.domain.RolePermission;
import com.siemens.domain.User;
import com.siemens.dto.KeyCloakRoleVMDTO;
import com.siemens.dto.KeyCloakUserDTO;
import com.siemens.dto.UserDTO;
import com.siemens.dto.UserPermissionDTO;
import com.siemens.exception.BadRequestAlertException;
import com.siemens.exception.EmailAlreadyUsedException;
import com.siemens.exception.LoginAlreadyUsedException;
import com.siemens.exception.MaxUserLimitException;
import com.siemens.mapper.KeyCloakRolePermissionMapper;
import com.siemens.mapper.KeyCloakUserMapper;
import com.siemens.repository.UserRepository;
import com.siemens.service.AuthService;
import com.siemens.service.ManageKeyCloakService;
import com.siemens.service.ManageKeyCloakUserService;
import com.siemens.service.PwdHashingTrippleDes;
import com.siemens.service.UserAuditService;
import com.siemens.service.UserService;
import com.siemens.utils.KeycloakSecurityUtils;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its
 * collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship
 * between User and Authority, and send everything to the client side: there
 * would be no View Model and DTO, a lot less code, and an outer-join which
 * would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities,
 * because people will quite often do relationships with the user, and we don't
 * want them to get the authorities all the time for nothing (for performance
 * reasons). This is the #1 goal: we should not impact our users' application
 * because of this use-case.</li>
 * <li>Not having an outer join causes n+1 requests to the database. This is not
 * a real issue as we have by default a second-level cache. This means on the
 * first HTTP call we do the n+1 requests, but then all authorities come from
 * the cache, so in fact it's much better than doing an outer join (which will
 * get lots of data from the database, for each HTTP call).</li>
 * <li>As this manages users, for security reasons, we'd rather have a DTO
 * layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this
 * case.
 */
@RestController
@RequestMapping(URLConstants.URL_USER_RESOURCE_ROOT)
public class UserResource
{
    public static final String USER_MANAGEMENT = "userManagement";
    
    @Autowired
    private PwdHashingTrippleDes pwdHash;
    
    @Autowired
    private UserAuditService userAuditService;
    
    private final ManageKeyCloakService manageKeyCloakService;
    
    private final ManageKeyCloakUserService manageKeyCloakUserService;
    
    private final KeyCloakUserMapper keyCloakUserMapper;
    
    private final KeyCloakRolePermissionMapper keyCloakRolePermissionMapper;
    
    private final AuthService authService;
    
    private final HttpServletRequest request;
    
    private final Logger log = LoggerFactory.getLogger(UserResource.class);
    
    @Value(Constants.APPLICATION_CLIENT_APP_NAME)
    private String applicationName;
    
    @Value(Constants.APPLICATION_USER_LIMIT)
    private int userLimit;
    
    private final UserService userService;
    
    private final UserRepository userRepository;
    
    public UserResource(ManageKeyCloakService manageKeyCloakService, ManageKeyCloakUserService manageKeyCloakUserService,
        KeyCloakUserMapper keyCloakUserMapper, KeyCloakRolePermissionMapper keyCloakRolePermissionMapper, AuthService authService,
        HttpServletRequest request, UserService userService, UserRepository userRepository)
    {
        this.manageKeyCloakService = manageKeyCloakService;
        this.manageKeyCloakUserService = manageKeyCloakUserService;
        this.keyCloakUserMapper = keyCloakUserMapper;
        this.keyCloakRolePermissionMapper = keyCloakRolePermissionMapper;
        this.authService = authService;
        this.request = request;
        this.userService = userService;
        this.userRepository = userRepository;
    }
    
    /**
     * {@code POST  /users} : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link. The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new user, or with status {@code 400 (Bad Request)} if the
     *         login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or
     *             email is already in use.
     */
    @PostMapping(URLConstants.URL_USERS_ROOT)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SIE_ADMIN
        + "\") ")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException
    {
        log.debug("REST request to save User : {}", userDTO);
        
        if (userDTO.getId() != null)
        {
            throw new BadRequestAlertException("A new user cannot already have an ID", USER_MANAGEMENT, "idexists");
        }
        else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent())
        {
            throw new LoginAlreadyUsedException();
        }
        else if (userLimit <= userRepository.findAll().size())
        {
            throw new MaxUserLimitException();
        }
        else
        {
            User newUser = userService.createUser(userDTO);
            
            try
            {
                log.info("password from request: {}", userDTO.getPassword());
                String textPwd = pwdHash.decrypt(userDTO.getPassword());
                log.info("textPwd: {}", textPwd);
                if (Objects.isNull(textPwd))
                {
                    throw new BadRequestAlertException("Password not encoded", USER_MANAGEMENT, "pwdissue");
                }
                manageKeyCloakUserService.createKeyCloakUser(getJwtToken(), newUser, userDTO.getAuthRoleId(),
                    userDTO.getAuthorities().stream().findFirst().orElse(""), textPwd);
            }
            catch (Exception e)
            {
                throw new BadRequestAlertException("Error in keycloak", "KeyCloak", "Server");
            }
            return ResponseEntity.created(new URI(URLConstants.URL_API_USERS + newUser.getLogin()))
                .body(newUser);
        }
    }
    
    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping(URLConstants.URL_USERS)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SIE_ADMIN
        + "\") ")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO)
    {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = null;
        // userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        // if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
        // throw new EmailAlreadyUsedException();
        // }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId())))
        {
            // throw new LoginAlreadyUsedException();
            userDTO.setId(existingUser.get().getId());
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);
        
        try
        {
            log.info("password from request: {}", userDTO.getPassword());
            String textPwd = pwdHash.decrypt(userDTO.getPassword());
            log.info("textPwd: {}", textPwd);
            if (Objects.isNull(textPwd))
            {
                throw new BadRequestAlertException("Password not encoded", USER_MANAGEMENT, "pwdissue");
            }
            manageKeyCloakUserService.updateKeyCloakUser(getJwtToken(), userDTO.getKeyCloakId(), userDTO.getEmail(),
                textPwd, userDTO.getAuthRoleId(),
                userDTO.getAuthorities().stream().findFirst().orElse(""));
        }
        catch (Exception e)
        {
            throw new BadRequestAlertException("Error in keycloak", "KeyCloak", "Server");
        }
        
        return updatedUser.map(result ->
        {
            return ResponseEntity.ok(result);
        }).orElse(ResponseEntity.noContent().build());
        
        // return ResponseUtil.wrapOrNotFound(updatedUser,
        // HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getLogin()));
    }
    
    @PutMapping(URLConstants.URL_UPDATE_USER_ROLE_PERMISSION)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SIE_ADMIN
        + "\") ")
    public ResponseEntity<UserPermissionDTO> updateUserRolePermission(@Valid @RequestBody UserPermissionDTO userPermissionDTO)
    {
        Optional<User> existingUser = userRepository.findOneByLogin(userPermissionDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userPermissionDTO.getId())))
        {
            userPermissionDTO.setId(existingUser.get().getId());
        }
        Optional<UserPermissionDTO> updatedUser = userService.updateUserRolePermission(userPermissionDTO);
        
        return updatedUser.map(result ->
        {
            return ResponseEntity.ok(result);
        }).orElse(ResponseEntity.noContent().build());
        
        // return ResponseUtil.wrapOrNotFound(updatedUser,
        // HeaderUtil.createAlert(applicationName, "userManagement.updated", userPermissionDTO.getLogin()));
    }
    
    @GetMapping(URLConstants.URL_GET_USER_ROLE_PERMISSION)
    public ResponseEntity<UserPermissionDTO> getUserRolePermission(@RequestParam String login)
    {
        log.debug("REST request to get User : {}", login);
        
        // Optional<User> optionalUser = userService.getUserWithAuthoritiesByLogin(login);
        //
        // if()
        
        return userService.getUserWithAuthoritiesByLogin(login)
            .map(result ->
            {
                return ResponseEntity.ok(new UserPermissionDTO(result));
            }).orElse(ResponseEntity.noContent().build());
        
        // .orElseThrow(() -> new BadRequestAlertException("A new user cannot already have an ID", USER_MANAGEMENT, "idexists"));
        
        // return ResponseUtil.wrapOrNotFound(
        // userService.getUserWithAuthoritiesByLogin(login)
        // .map(UserPermissionDTO::new));
    }
    
    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    @GetMapping(URLConstants.URL_USERS_AUTHORITIES)
    // @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities()
    {
        return userService.getAuthorities();
    }
    
    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status
     *         {@code 404 (Not Found)}.
     */
    @GetMapping(URLConstants.URL_GET_USER)
    public ResponseEntity<UserDTO> getUser(@RequestParam String login)
    {
        log.debug("REST request to get User : {}", login);
        
        return userService.getUserWithAuthoritiesByLogin(login).map(result ->
        {
            return ResponseEntity.ok(new UserDTO(result));
        }).orElse(ResponseEntity.noContent().build());
        
        // return ResponseUtil.wrapOrNotFound(
        // userService.getUserWithAuthoritiesByLogin(login)
        // .map(UserDTO::new));
    }
    
    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping(URLConstants.URL_DELETE_USER)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SIE_ADMIN
        + "\") ")
    public ResponseEntity<Void> deleteUser(@PathVariable String login, @RequestParam(required = true) String userId)
    {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        manageKeyCloakUserService.deleteKeyCloakUser(getJwtToken(), userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping(URLConstants.URL_USERS_AUTH_PROVIDERS)
    public List<KeyCloakUserDTO> getAuthProviderAllUsers()
    {
        List<UserRepresentation> userRepresentationsList = manageKeyCloakService.getKeyCloakUsers(getJwtToken());
        List<KeyCloakUserDTO> userDTOList = new ArrayList<>();
        String sbAuthName = "";
        List<RoleRepresentation> roleRepresentations;
        List<KeyCloakRoleVMDTO> rolePermissionRepresentations = new ArrayList<>();
        
        if (Objects.isNull(userRepresentationsList))
        {
            return new ArrayList<KeyCloakUserDTO>();
        }
        
        try
        {
            userRepresentationsList.removeIf(user -> user.getUsername().equals(KeyCloakConstants.USERNAME_INITIAL));
        }
        catch (NullPointerException e)
        {
            log.error(e.getMessage());
        }
        
        Optional<User> existingUser = null;
        
        for (UserRepresentation userRepresentation : userRepresentationsList)
        {
            roleRepresentations = manageKeyCloakUserService.getUserAssignedRole(getJwtToken(), userRepresentation.getId());
            if (!roleRepresentations.isEmpty())
            {
                try
                {
                    roleRepresentations.removeIf(roleName -> roleName.getName().equals(KeyCloakConstants.ROLE_NAME_UMA_PROTECTION));
                }
                catch (NullPointerException e)
                {
                    log.error(e.getMessage());
                }
                
                rolePermissionRepresentations = getRolePermissionRepresentation(authService, roleRepresentations,
                    keyCloakRolePermissionMapper, sbAuthName);
                
            }
            
            KeyCloakUserDTO keyCloakUserDTO = keyCloakUserMapper.userRepresentationToKeyCloakUserDTO(userRepresentation,
                rolePermissionRepresentations);
            existingUser = userRepository.findOneByLogin(userRepresentation.getUsername().toLowerCase());
            
            if (existingUser.isPresent())
            {
                keyCloakUserDTO.setDepartment(existingUser.get().getDepartment());
            }
            
            userDTOList.add(keyCloakUserDTO);
        }
        
        return userDTOList;
    }
    
    @GetMapping(URLConstants.URL_USERS_AUTH_PROVIDERS_ROLES)
    public List<KeyCloakRoleVMDTO> getAuthProviderAllRoles(Principal principal)
    {
        List<RoleRepresentation> roleRepresentations = manageKeyCloakService.getKeyCloakClientRoles(getJwtToken());
        List<KeyCloakRoleVMDTO> rolePermissionRepresentations;
        String sbAuthName = "";
        
        try
        {
            roleRepresentations.removeIf(roleName -> roleName.getName().equals(KeyCloakConstants.ROLE_NAME_UMA_PROTECTION));
            
            JwtAuthenticationToken token = (JwtAuthenticationToken) principal;
            
            SimpleGrantedAuthority grantedAuthority = (SimpleGrantedAuthority) token.getAuthorities().stream().findFirst().orElse(null);
            
            // Remove Sie Admin role if current user is not Siemens Admin
            if (Objects.nonNull(grantedAuthority != null ? grantedAuthority.getAuthority() : null)
                && !grantedAuthority.getAuthority().equals(Constants.ROLE + AuthoritiesConstants.SIE_ADMIN))
            {
                roleRepresentations.removeIf(roleName -> roleName.getName().equals(AuthoritiesConstants.SIE_ADMIN));
            }
            
        }
        catch (NullPointerException e)
        {
            log.error(e.getMessage());
        }
        
        rolePermissionRepresentations = getRolePermissionRepresentation(authService, roleRepresentations, keyCloakRolePermissionMapper,
            sbAuthName);
        
        return rolePermissionRepresentations;
    }
    
    @GetMapping(URLConstants.URL_USERS_ROLE_PERMISSIONS)
    public List<RolePermission> getRolePermissions(@RequestParam String authName)
    {
        return authService.getAuthorityPermissions(KeycloakSecurityUtils.getAuthName(authName));
    }
    
    private JwtAuthenticationToken getSecurityContext()
    {
        return (JwtAuthenticationToken) request.getUserPrincipal();
    }
    
    private Jwt getJwtToken()
    {
        return (Jwt) getSecurityContext().getToken();
    }
    
    @DeleteMapping(URLConstants.URL_DEL_MULTIPLE_USERS)
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\") or hasRole(\"" + AuthoritiesConstants.SIE_ADMIN
        + "\") ")
    public ResponseEntity<Void> deleteMultipleUser(@RequestParam(required = true) List<String> userLoginDetails,
        @RequestParam(required = true) List<String> userKeycloakDetails)
    {
        log.debug("REST request to delete User: {}", userLoginDetails.toString());
        
        for (String userLogin : userLoginDetails)
            userService.deleteUser(userLogin);
        
        for (String userKeycloak : userKeycloakDetails)
            manageKeyCloakUserService.deleteKeyCloakUser(getJwtToken(), userKeycloak);
        
        return ResponseEntity.noContent()
            .build();
    }
    
    private List<KeyCloakRoleVMDTO> getRolePermissionRepresentation(AuthService authService, List<RoleRepresentation> roleRepresentations,
        KeyCloakRolePermissionMapper keyCloakRolePermissionMapper, String sbAuthName)
    {
        List<KeyCloakRoleVMDTO> rolePermissionRepresentations = new ArrayList<>();
        Permission permission = null;
        for (RoleRepresentation roleRepresentation : roleRepresentations)
        {
            
            if (!roleRepresentation.getName().contains(Constants.ROLE))
            {
                sbAuthName = Constants.ROLE + roleRepresentation.getName();
                
                List<RolePermission> rolePermission = authService.getAuthorityPermissions(sbAuthName);
                if (!rolePermission.isEmpty())
                {
                    permission = rolePermission.get(0).getPermission();
                }
                
                rolePermissionRepresentations
                    .add(keyCloakRolePermissionMapper.roleRepresentationTKeyCloakRoleVM(roleRepresentation, permission));
            }
        }
        return rolePermissionRepresentations;
    }
    
    /**
     * Fetch all the user name
     * 
     * @return List<String>
     */
    @GetMapping(URLConstants.URL_GET_ALL_USER)
    public ResponseEntity<ApiResponse<List<String>>> getAllUser()
    {
        Optional<List<String>> allUsersList = userService.getAllUser();
        
        if (allUsersList.isEmpty())
        {
            return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.OK.value(), URLConstants.NO_RECORD_FOUND, allUsersList.get()),
                HttpStatus.OK);
        }
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), URLConstants.RECORD_FOUND, allUsersList.get()),
            HttpStatus.OK);
    }
    
    /**
     * {@code POST  /users/logout} : logout current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     * 
     */
    @PostMapping(URLConstants.URL_LOGOUT_USER)
    public ResponseEntity<ApiResponse<String>> logoutUser(@Valid @RequestBody UserDTO userDTO)
    {
        log.info("....inside logoutUser()");
        
        String userName = userDTO.getLogin();
        log.info("userName: {}", userName);
        
        int rowAffected = userAuditService.setManualLogout(userName);
        log.info("rowsAffected: {}", rowAffected);
        
        manageKeyCloakUserService.logoutKeyCloakUser(getJwtToken(), userDTO.getKeyCloakId());
        
        return new ResponseEntity<>(
            new ApiResponse<>(HttpStatus.OK.value(), "User logs out successfully.", ""),
            HttpStatus.OK);
    }
}
