package com.imolczek.training.spring.springsecuritysample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class OAuth2ResourceServerSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Value("${security.oauth2.resourceserver.jwk.jwk-set-uri}") String jwkSetUri;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter());
	}
	
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new MyIDPAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }	

    /**
     * 
     * @return
     */
	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri).build();
	}
	
}
