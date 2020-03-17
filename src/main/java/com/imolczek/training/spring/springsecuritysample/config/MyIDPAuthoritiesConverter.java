package com.imolczek.training.spring.springsecuritysample.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * This class allows to map authorities (scopes and roles for example) from
 * JWT claims
 * You may adapt this converter depending on where the OpenID Provider
 * puts the claims
 * If you don't provide such Converter, Spring by default loads the list of
 * authorities with the scopes found inside of either the scope or scp claim
 * 
 * @author Fabian Bouché
 *
 */
public class MyIDPAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(final Jwt jwt) {
        
    	List<GrantedAuthority> rolesCollection =
    			((List<String>) jwt.getClaim("roles")).stream()
                .map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        List<GrantedAuthority> scopesCollection =
        		new ArrayList<String>(
        				Arrays.asList(
        						((String) jwt.getClaim("scope")).split(" ")
        						)
        				).stream()
                .map(scopeName -> "SCOPE_" + scopeName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        List<GrantedAuthority> grantedAuthorities = rolesCollection;
        grantedAuthorities.addAll(scopesCollection);
        
        return grantedAuthorities;
    }
    
}
