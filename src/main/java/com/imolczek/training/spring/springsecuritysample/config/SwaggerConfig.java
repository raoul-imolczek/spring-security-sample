package com.imolczek.training.spring.springsecuritysample.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    
	@Bean
    public Docket api() { 
		/*
		List<AuthorizationScope> scopes = new ArrayList<>();
		scopes.add(new AuthorizationScope("SAY_HELLO", "Right to say hello"));
		scopes.add(new AuthorizationScope("openid", "OIDC"));
		
		List<GrantType> grantTypes = new ArrayList<>();
		
		String clientIdName = null;
		String clientSecretName = null;
		String tokenRequestUrl = "http://127.0.0.1:8080/auth/realms/master/protocol/openid-connect/auth";
		TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(tokenRequestUrl, clientIdName, clientSecretName);
		
		TokenEndpoint tokenEndpoint = new TokenEndpoint("http://127.0.0.1:8080/auth/realms/master/protocol/openid-connect/token", "jwt-token");
		grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));

		List<SecurityScheme> schemeList = new ArrayList<>();
		schemeList.add(new OAuth("sayhello_auth", scopes, grantTypes ));
		*/
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          .apis(RequestHandlerSelectors.any())              
          .paths(PathSelectors.any())                          
          .build();
          //.securitySchemes(schemeList);                                           
    }
}
