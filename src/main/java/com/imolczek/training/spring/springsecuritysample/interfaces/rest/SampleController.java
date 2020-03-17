package com.imolczek.training.spring.springsecuritysample.interfaces.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample/api/v1")
public class SampleController {

	private static Logger logger = LoggerFactory.getLogger(SampleController.class);
	
	@GetMapping("/whoami")
	public String index(@AuthenticationPrincipal Jwt jwt, @CurrentSecurityContext SecurityContext context) {
		Iterator<? extends GrantedAuthority> authorities = context.getAuthentication().getAuthorities().iterator();
		while(authorities.hasNext()) {
			logger.info(authorities.next().getAuthority());
		}
		return String.format("Hello, %s!", jwt.getClaimAsString("name"));
	}
	
	@PreAuthorize("hasAuthority('SCOPE_accounts:list') and hasAuthority('ROLE_customer')")
	@GetMapping("/accounts")
	public List<String> accounts(@AuthenticationPrincipal Jwt jwt) {
		List<String> accounts = new ArrayList<>();
		accounts.add("1234567890");
		accounts.add("ABCD123456");
		return accounts;
	}

	@PreAuthorize("hasAuthority('SCOPE_accounts:details') and hasAuthority('ROLE_customer')")
	@GetMapping("/accounts/{accountNumber}")
	public String accountDetails(@AuthenticationPrincipal Jwt jwt, String accountNumber) {
		return String.format("Account details for account number %s", accountNumber);
	}
	
}
