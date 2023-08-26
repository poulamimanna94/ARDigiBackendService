package com.siemens.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.siemens.constant.Constants;
import com.siemens.constant.KeyCloakConstants;
import com.siemens.domain.CustomPermission;
import com.siemens.domain.CustomUserRolePermission;
import com.siemens.domain.RolePermission;
import com.siemens.domain.User;
import com.siemens.dto.UserDTO;
import com.siemens.dto.UserPermissionDTO;
import com.siemens.exception.EmailAlreadyUsedException;
import com.siemens.exception.UsernameAlreadyUsedException;
import com.siemens.repository.AuthorityRepository;
import com.siemens.repository.UserRepository;
import com.siemens.security.utils.SecurityUtils;
import com.siemens.utils.KeycloakSecurityUtils;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService
{
    private static final String CHANGED_INFO_USER = "Changed Information for User: {}";
    
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    
    private final AuthorityRepository authorityRepository;
    
    private final CacheManager cacheManager;
    
    public UserService(UserRepository userRepository, AuthorityRepository authorityRepository, CacheManager cacheManager)
    {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }
    
    public Optional<User> activateRegistration(String key)
    {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user ->
            {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }
    
    public User registerUser(UserDTO userDTO, String password)
    {
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser ->
        {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed)
            {
                throw new UsernameAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser ->
        {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed)
            {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = KeycloakSecurityUtils.createNewUser(userDTO);
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey("");
        Set<RolePermission> authorities = new HashSet<>();
        authorityRepository.findById(Constants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    
    private boolean removeNonActivatedUser(User existingUser)
    {
        if (existingUser.getActivated())
        {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);
        return true;
    }
    
    public User createUser(UserDTO userDTO)
    {
        User user = KeycloakSecurityUtils.createNewUser(userDTO);
        if (userDTO.getLangKey() == null)
        {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        }
        else
        {
            user.setLangKey(userDTO.getLangKey());
        }
        user.setResetKey("");
        user.setResetDate(Instant.now());
        user.setActivated(true);
        
        user.setCreatedBy("");
        
        if (userDTO.getAuthorities() != null)
        {
            Set<RolePermission> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }
    
    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO)
    {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user ->
            {
                this.clearUserCaches(user);
                if (userDTO.getEmail() != null)
                {
                    user.setEmail(userDTO.getEmail().toLowerCase());
                }
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                user.setLastModifiedDate(Instant.now());
                user.setDepartment(userDTO.getDepartment());
                Set<RolePermission> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug(CHANGED_INFO_USER, user);
                return user;
            })
            .map(UserDTO::new);
    }
    
    public Optional<UserPermissionDTO> updateUserRolePermission(UserPermissionDTO userPermissionDTO)
    {
        return Optional.of(userRepository
            .findById(userPermissionDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user ->
            {
                this.clearUserCaches(user);
                Set<RolePermission> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userPermissionDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                user.setCustomUserRolePermission(getCustomUserRolePermission(user, managedAuthorities, userPermissionDTO));
                this.clearUserCaches(user);
                log.debug(CHANGED_INFO_USER, user);
                return user;
            })
            .map(UserPermissionDTO::new);
    }
    
    private CustomUserRolePermission getCustomUserRolePermission(User user, Set<RolePermission> managedAuthorities,
        UserPermissionDTO userPermissionDTO)
    {
        CustomUserRolePermission customUserRolePermission = Objects.nonNull(user.getCustomUserRolePermission())
            ? user.getCustomUserRolePermission()
            : new CustomUserRolePermission();
        Optional<RolePermission> optionalRolePermission = managedAuthorities.stream().findFirst();
        if (optionalRolePermission.isPresent())
        {
            RolePermission rolePermission = optionalRolePermission.get();
            customUserRolePermission.setUser(user);
            customUserRolePermission.setRolePermission(rolePermission);
            customUserRolePermission.setCustomPermission(createCustomPermission(userPermissionDTO));
        }
        return customUserRolePermission;
    }
    
    private CustomPermission createCustomPermission(UserPermissionDTO userPermissionDTO)
    {
        CustomPermission customPermission = new CustomPermission();
        customPermission.setArchiveConfig(userPermissionDTO.getArchiveConfigPermission());
        customPermission.setConnectivityConfig(userPermissionDTO.getConnectivityConfigPermission());
        customPermission.setMonitorDashboard(userPermissionDTO.getMonitorDashboardPermission());
        customPermission.setOpcUaServerConfig(userPermissionDTO.getOpcUaServerConfigPermission());
        return customPermission;
    }
    
    public void deleteUser(String login)
    {
        userRepository.findOneByLogin(login).ifPresent(user ->
        {
            userRepository.delete(user);
            this.clearUserCaches(user);
            log.debug("Deleted User: {}", user);
        });
    }
    
    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName last name of user.
     * @param email email id of user.
     * @param langKey language key.
     * @param imageUrl image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl)
    {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user ->
            {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                if (email != null)
                {
                    user.setEmail(email.toLowerCase());
                }
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug(CHANGED_INFO_USER, user);
            });
    }
    
    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword)
    {
        // to do code
    }
    
    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable)
    {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login)
    {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }
    
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities()
    {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }
    
    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers()
    {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user ->
            {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }
    
    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities()
    {
        return authorityRepository.findAll().stream().map(RolePermission::getName).collect(Collectors.toList());
    }
    
    private void clearUserCaches(User user)
    {
        Objects.requireNonNull(cacheManager.getCache(Constants.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        if (user.getEmail() != null)
        {
            Objects.requireNonNull(cacheManager.getCache(Constants.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
        }
    }
    
    private User syncUserWithIdP(Map<String, Object> details, User user)
    {
        // save authorities in to sync user roles/groups between IdP and JHipster's local database
        Collection<String> dbAuthorities = getAuthorities();
        Collection<String> userAuthorities = user.getAuthorities().stream().map(RolePermission::getName).collect(Collectors.toList());
        for (String authority : userAuthorities)
        {
            if (!dbAuthorities.contains(authority))
            {
                log.debug("Saving authority '{}' in local database", authority);
                RolePermission authorityToSave = new RolePermission();
                authorityToSave.setName(authority);
                authorityRepository.save(authorityToSave);
            }
        }
        // save account in to sync users between IdP and JHipster's local database
        Optional<User> existingUser = userRepository.findOneByLogin(user.getLogin());
        if (existingUser.isPresent())
        {
            // if IdP sends last updated information, use it to determine if an update should happen
            if (details.get("updated_at") != null)
            {
                Instant dbModifiedDate = existingUser.get().getLastModifiedDate();
                Instant idpModifiedDate = (Instant) details.get("updated_at");
                if (idpModifiedDate.isAfter(dbModifiedDate))
                {
                    log.debug("Updating user '{}' in local database", user.getLogin());
                    updateUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                        user.getLangKey(), user.getImageUrl());
                }
                // no last updated info, blindly update
            }
            else
            {
                log.debug("Updating user '{}' in local database", user.getLogin());
                updateUser(user.getFirstName(), user.getLastName(), user.getEmail(),
                    user.getLangKey(), user.getImageUrl());
            }
        }
        else
        {
            log.debug("Saving user '{}' in local database", user.getLogin());
            userRepository.save(user);
            this.clearUserCaches(user);
        }
        return user;
    }
    
    /**
     * Returns the user from an OAuth 2.0 login or resource server with JWT.
     * Synchronizes the user in the local repository.
     *
     * @param authToken the authentication token.
     * @return the user from the authentication.
     */
    // @Transactional(readOnly = true)
    public UserDTO getUserFromAuthentication(AbstractAuthenticationToken authToken)
    {
        Map<String, Object> attributes = new HashMap<>();
        String keycloakId = null;
        
        if (authToken instanceof JwtAuthenticationToken)
        {
            attributes = ((JwtAuthenticationToken) authToken).getTokenAttributes();
        }
        User user = getUser(attributes);
        user.setAuthorities(authToken.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(authority ->
            {
                RolePermission auth = new RolePermission();
                auth.setName(authority);
                return auth;
            })
            .collect(Collectors.toSet()));
        
        // keycloak UserId
        if (attributes.get(KeyCloakConstants.SUB) != null)
        {
            keycloakId = (String) attributes.get(KeyCloakConstants.SUB);
        }
        
        return new UserDTO(syncUserWithIdP(attributes, user), keycloakId);
    }
    
    public AccessToken getUserKeyCloakAccessToken(KeycloakAuthenticationToken token)
    {
        return token.getAccount().getKeycloakSecurityContext().getToken();
    }
    
    public KeycloakSecurityContext getUserKeycloakSecurityContext(KeycloakAuthenticationToken token)
    {
        return token.getAccount().getKeycloakSecurityContext();
    }
    
    private static User getUser(Map<String, Object> details)
    {
        User user = new User();
        // handle resource server JWT, where sub claim is email and uid is ID
        if (details.get(KeyCloakConstants.UID) != null)
        {
            user.setLogin((String) details.get(KeyCloakConstants.SUB));
        }
        if (details.get(KeyCloakConstants.PREFERRED_USERNAME) != null)
        {
            user.setLogin(((String) details.get(KeyCloakConstants.PREFERRED_USERNAME)).toLowerCase());
        }
        
        if (details.get(KeyCloakConstants.GIVEN_NAME) != null)
        {
            user.setFirstName((String) details.get(KeyCloakConstants.GIVEN_NAME));
        }
        if (details.get(KeyCloakConstants.FAMILY_NAME) != null)
        {
            user.setLastName((String) details.get(KeyCloakConstants.FAMILY_NAME));
        }
        if (details.get(KeyCloakConstants.EMAIL_VERIFIED) != null)
        {
            user.setActivated((Boolean) details.get(KeyCloakConstants.EMAIL_VERIFIED));
        }
        if (details.get(KeyCloakConstants.EMAIL) != null)
        {
            user.setEmail(((String) details.get(KeyCloakConstants.EMAIL)).toLowerCase());
        }
        if (details.get(KeyCloakConstants.LANG_KEY) != null)
        {
            user.setLangKey((String) details.get(KeyCloakConstants.LANG_KEY));
        }
        else if (details.get(KeyCloakConstants.LOCALE) != null)
        {
            // trim off country code if it exists
            String locale = (String) details.get(KeyCloakConstants.LOCALE);
            if (locale.contains(Constants.UNDERSCORE))
            {
                locale = locale.substring(0, locale.indexOf(Constants.UNDERSCORE_CHAR));
            }
            else if (locale.contains(Constants.DASH))
            {
                locale = locale.substring(0, locale.indexOf(Constants.DASH_CHAR));
            }
            user.setLangKey(locale.toLowerCase());
        }
        else
        {
            user.setLangKey(Constants.DEFAULT_LANGUAGE);
        }
        if (details.get(Constants.PICTURE) != null)
        {
            user.setImageUrl((String) details.get(Constants.PICTURE));
        }
        user.setActivated(true);
        user.setCreatedBy("");
        return user;
    }
    
    public Optional<UserDTO> updateLeve1RoleUserPermission(UserDTO userDTO)
    {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(UserDTO::new);
    }
    
    @Transactional(readOnly = true)
    public Optional<List<String>> getAllUser()
    {
        return Optional.of(userRepository.findAll().stream().map(User::getLogin).collect(Collectors.toList()));
    }
}
