package com.siemens.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.siemens.constant.Constants;

/**
 * A user.
 */
@Entity
@Table(name = "user_details")
// @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User extends AbstractAuditingEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_details_sequence_generator")
    @SequenceGenerator(name = "user_details_sequence_generator")
    private Long id;
    
    @NotNull
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String login;
    
    @Size(max = 50)
    @Column(name = "first_name", length = 50)
    private String firstName;
    
    @Size(max = 50)
    @Column(name = "last_name", length = 50)
    private String lastName;
    
    @Email
    @Size(min = 5, max = 254)
    @Column(length = 254, unique = true)
    private String email;
    
    @NotNull
    @Column(nullable = false)
    private boolean activated = false;
    
    @Size(min = 2, max = 10)
    @Column(name = "lang_key", length = 10)
    private String langKey;
    
    @Size(max = 256)
    @Column(name = "image_url", length = 256)
    private String imageUrl;
    
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;
    
    @Size(max = 20)
    @Column(name = "reset_key", length = 20)
    @JsonIgnore
    private String resetKey;
    
    @Column(name = "reset_date")
    private Instant resetDate = null;
    
    @Column(name = "department")
    private String department;
    
    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_authority", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "authority_name", referencedColumnName = "name") })
    // @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @BatchSize(size = 20)
    private Set<RolePermission> authorities = new HashSet<>();
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    @PrimaryKeyJoinColumn
    private CustomUserRolePermission customUserRolePermission;
    
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
    
    // Lowercase the login before saving it in database
    public void setLogin(String login)
    {
        this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
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
    
    public boolean getActivated()
    {
        return activated;
    }
    
    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }
    
    public String getActivationKey()
    {
        return activationKey;
    }
    
    public void setActivationKey(String activationKey)
    {
        this.activationKey = activationKey;
    }
    
    public String getResetKey()
    {
        return resetKey;
    }
    
    public void setResetKey(String resetKey)
    {
        this.resetKey = resetKey;
    }
    
    public Instant getResetDate()
    {
        return resetDate;
    }
    
    public void setResetDate(Instant resetDate)
    {
        this.resetDate = resetDate;
    }
    
    public String getLangKey()
    {
        return langKey;
    }
    
    public void setLangKey(String langKey)
    {
        this.langKey = langKey;
    }
    
    public Set<RolePermission> getAuthorities()
    {
        return authorities;
    }
    
    public void setAuthorities(Set<RolePermission> authorities)
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
    
    public CustomUserRolePermission getCustomUserRolePermission()
    {
        return customUserRolePermission;
    }
    
    public void setCustomUserRolePermission(CustomUserRolePermission customUserRolePermission)
    {
        this.customUserRolePermission = customUserRolePermission;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof User))
        {
            return false;
        }
        return id != null && id.equals(((User) o).id);
    }
    
    @Override
    public int hashCode()
    {
        return 31;
    }
    
    @Override
    public String toString()
    {
        return "User [id=" + id + ", login=" + login + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
            + ", activated=" + activated + ", langKey=" + langKey + ", imageUrl=" + imageUrl + ", activationKey=" + activationKey
            + ", resetKey=" + resetKey + ", resetDate=" + resetDate + ", department=" + department + ", authorities=" + authorities + "]";
    }
    
}
