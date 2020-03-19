package com.imolczek.training.spring.springsecuritysample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * See documentation here: https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/builders/HttpSecurity.html#oauth2ResourceServer-org.springframework.security.config.Customizer-
 * The prePostEnabled attribute is what allows me to use @PreAuthorize annotations in the REST
 * controller to authorize requests depending on the scope or the roles 
 * @author Fabian Bouché
 *
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${security.oauth2.resourceserver.jwk.jwk-set-uri}") String jwkSetUri;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Enabling OAuth 2.0 Resource server security with JWT Token validation
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());

	}
	
	/**
	 * The way scopes and roles are defined in JWT tokens is not 100% standard
	 * Thus, you'll often have to specify your own conversion rules
	 * What we mean here with conversion, is how we map claims in JWT tokens
	 * as "Authorities" bound to the security context
	 * The mapped Authorities can be used in the @PreAuthorize annotations
	 * inside of the REST Controller
	 * @return the JwtAuthenticationConverter
	 */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new MyIDPAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }	

    /**
     * Provision of a JwtDecoder bean that uses the IDP configuration from properties to
     * decode the JWT access tokens provided alongside requests
     * @return A JWTDecoder
     */
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
	}
	
}
