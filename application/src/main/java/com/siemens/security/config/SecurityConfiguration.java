package com.siemens.security.config;

import com.siemens.config.ApplicationProperties;
import com.siemens.constant.AuthoritiesConstants;
import com.siemens.security.filter.RestApiAccessDeniedHandler;
import com.siemens.security.filter.RestApiAuthenticationEntryPoint;
import com.siemens.security.oauth2.AudienceValidator;
import com.siemens.security.oauth2.JwtGrantedAuthorityConverter;
import com.siemens.security.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.HashSet;
import java.util.Set;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
    private final CorsFilter corsFilter;
    
    @Value("${spring.security.oauth2.client.provider.oidc.issuer-uri}")
    private String issuerUri;
    
    private final SecurityProblemSupport problemSupport;
    
    private final ApplicationProperties applicationProperties;
    
    public SecurityConfiguration(CorsFilter corsFilter, SecurityProblemSupport problemSupport, ApplicationProperties applicationProperties)
    {
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.applicationProperties = applicationProperties;
    }
    
    @Override
    public void configure(WebSecurity web)
    {
        log.info("in side ->configure(WebSecurity web) method");
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/swagger-ui.html")
            .antMatchers("/api/user-auth/authorize")
            .antMatchers("/api/i/system-config/logo/download/**")
            .antMatchers("/test/**");
    }
    
    @Override
    public void configure(HttpSecurity http) throws Exception
    {
        log.info("in side ->configure(HttpSecurity http) method");
        // @formatter:off
        // super.configure(http);
        http
            .csrf()
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            // .disable()
            .and()
            .addFilterBefore(corsFilter, CsrfFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
            .and()
            .headers()
            .contentSecurityPolicy(
                "default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
            .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
            .featurePolicy(
                "geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
            .and()
            .frameOptions()
            .deny()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/auth-info").permitAll()
            .antMatchers("/api/**").authenticated()
            // .antMatchers("/api/user-auth/authorize").permitAll()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(authenticationEntryPoint())
            .and()
            .oauth2Login()
            .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(authenticationConverter())
            .and()
            .and()
            .oauth2Client();
        // @formatter:on
    }
    
    @Bean
    AuthenticationEntryPoint authenticationEntryPoint()
    {
        return new RestApiAuthenticationEntryPoint();
    }
    
    @Bean
    RestApiAccessDeniedHandler accessDeniedHandler()
    {
        return new RestApiAccessDeniedHandler();
    }
    
    Converter<Jwt, AbstractAuthenticationToken> authenticationConverter()
    {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthorityConverter());
        return jwtAuthenticationConverter;
    }
    
    /**
     * Map authorities from "groups" or "roles" claim in ID Token.
     *
     * @return a {@link GrantedAuthoritiesMapper} that maps groups from
     *         the IdP to Spring Security Authorities.
     */
    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapper()
    {
        return authorities ->
        {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            
            authorities.forEach(authority ->
            {
                // Check for OidcUserAuthority because Spring Security 5.2 returns
                // each scope as a GrantedAuthority, which we don't care about.
                if (authority instanceof OidcUserAuthority)
                {
                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                    mappedAuthorities.addAll(SecurityUtils.extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
                }
            });
            return mappedAuthorities;
        };
    }
    
    @Bean
    JwtDecoder jwtDecoder()
    {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuerUri);
        
        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(applicationProperties.getAudience());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        
        jwtDecoder.setJwtValidator(withAudience);
        
        return jwtDecoder;
    }
}
