package com.siemens.dto;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.siemens.constant.Constants;
import com.siemens.domain.RolePermission;
import com.siemens.domain.User;

/**
 * A DTO representing a user, with his authorities.
 */
public class UserDTO
{
    
    private Long id;
    
    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;
    
    @Size(max = 50)
    private String firstName;
    
    @Size(max = 50)
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    private String email;
    
    @Size(max = 256)
    private String imageUrl;
    
    private boolean activated = false;
    
    @Size(min = 2, max = 10)
    private String langKey;
    
    private String authRoleId;
    
    private String createdBy;
    
    private Instant createdDate;
    
    private String lastModifiedBy;
    
    private Instant lastModifiedDate;
    
    private Set<String> authorities;
    
    private String password;
    
    private String keyCloakId;
    
    private String department;
    
    private Boolean customPermission;
    
    public UserDTO()
    {
        // Empty constructor needed for Jackson.
    }
    
    public UserDTO(User user)
    {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.department = user.getDepartment();
        this.authorities = user.getAuthorities().stream()
            .map(RolePermission::getName)
            .collect(Collectors.toSet());
        this.customPermission = Objects.nonNull(user.getCustomUserRolePermission());
    }
    
    public UserDTO(User user, String keycloakId)
    {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.activated = user.getActivated();
        this.imageUrl = user.getImageUrl();
        this.langKey = user.getLangKey();
        this.createdBy = user.getCreatedBy();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
        this.department = user.getDepartment();
        this.authorities = user.getAuthorities().stream()
            .map(RolePermission::getName)
            .collect(Collectors.toSet());
        this.customPermission = Objects.nonNull(user.getCustomUserRolePermission());
        this.keyCloakId = keycloakId;
    }
    
    public Long getId()
    {
        return id;
    }
    
    public void setId(Long id)
    {
        this.id = id;
    }
    
    public String getLogin()
    {
        return login;
    }
    
    public void setLogin(String login)
    {
        this.login = login;
    }
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    public String getLastName()
    {
        return lastName;
    }
    
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    public String getEmail()
    {
        return email;
    }
    
    public void setEmail(String email)
    {
        this.email = email;
    }
    
    public String getImageUrl()
    {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }
    
    public boolean isActivated()
    {
        return activated;
    }
    
    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }
    
    public String getLangKey()
    {
        return langKey;
    }
    
    public void setLangKey(String langKey)
    {
        this.langKey = langKey;
    }
    
    public String getCreatedBy()
    {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }
    
    public Instant getCreatedDate()
    {
        return createdDate;
    }
    
    public void setCreatedDate(Instant createdDate)
    {
        this.createdDate = createdDate;
    }
    
    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }
    
    public void setLastModifiedBy(String lastModifiedBy)
    {
        this.lastModifiedBy = lastModifiedBy;
    }
    
    public Instant getLastModifiedDate()
    {
        return lastModifiedDate;
    }
    
    public void setLastModifiedDate(Instant lastModifiedDate)
    {
        this.lastModifiedDate = lastModifiedDate;
    }
    
    public Set<String> getAuthorities()
    {
        return authorities;
    }
    
    public void setAuthorities(Set<String> authorities)
    {
        this.authorities = authorities;
    }
    
    public String getDepartment()
    {
        return department;
    }
    
    public void setDepartment(String department)
    {
        this.department = department;
    }
    
    @Override
    public String toString()
    {
        return "UserDTO [id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
            + ", imageUrl=" + imageUrl + ", activated=" + activated + ", langKey=" + langKey + ", authRoleId=" + authRoleId + ", createdBy="
            + createdBy + ", createdDate=" + createdDate + ", lastModifiedBy=" + lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate
            + ", authorities=" + authorities + ", password=" + password + ", keyCloakId=" + keyCloakId + ", department=" + department + "]";
    }
    
    /**
     * @return String return the authRoleId
     */
    public String getAuthRoleId()
    {
        return authRoleId;
    }
    
    /**
     * @param authRoleId the authRoleId to set
     */
    public void setAuthRoleId(String authRoleId)
    {
        this.authRoleId = authRoleId;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getKeyCloakId()
    {
        return keyCloakId;
    }
    
    public void setKeyCloakId(String keyCloakid)
    {
        this.keyCloakId = keyCloakid;
    }
    
    public Boolean isCustomPermission()
    {
        return customPermission;
    }
    
    public void setCustomPermission(Boolean customPermission)
    {
        this.customPermission = customPermission;
    }
    
}
