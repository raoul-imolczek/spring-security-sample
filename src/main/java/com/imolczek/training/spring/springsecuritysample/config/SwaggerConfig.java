package com.imolczek.training.spring.springsecuritysample.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    
	@Value("${security.oauth2.client.userAuthorizationUri}") String authUrl;
	@Value("${security.oauth2.client.accessTokenUri}") String tokenUrl;
	
	@Bean
    public Docket api() { 
		List<AuthorizationScope> scopes = new ArrayList<>();
		scopes.add(new AuthorizationScope("accounts:list", "Right to list accounts"));
		scopes.add(new AuthorizationScope("accounts:details", "Right to consult accounts details"));
		
		List<GrantType> grantTypes = new ArrayList<>();
		
		String clientIdName = null;
		String clientSecretName = null;
		TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(authUrl, clientIdName, clientSecretName);
		
		TokenEndpoint tokenEndpoint = new TokenEndpoint(tokenUrl, "jwt-token");
		grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));

		List<SecurityScheme> schemeList = new ArrayList<>();
		schemeList.add(new OAuth("bank_auth", scopes, grantTypes));

        return new Docket(DocumentationType.SWAGGER_2)  
        		.ignoredParameterTypes(AuthenticationPrincipal.class)
        		.ignoredParameterTypes(CurrentSecurityContext.class)
        		.select()               
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        		.paths(PathSelectors.any())
        		.build()
    			.securitySchemes(schemeList);                                           
    }
}
