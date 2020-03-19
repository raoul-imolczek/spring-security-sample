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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imolczek.training.spring.springsecuritysample.exceptions.WrongAccountHolderException;
import com.imolczek.training.spring.springsecuritysample.interfaces.rest.resources.AccountResource;
import com.imolczek.training.spring.springsecuritysample.interfaces.rest.resources.Whoami;
import com.imolczek.training.spring.springsecuritysample.services.AccountsService;

@RestController
@RequestMapping("/sample/api/v1")
public class SampleController {

	private static Logger logger = LoggerFactory.getLogger(SampleController.class);

	private AccountsService accountsService;
	
	public SampleController(AccountsService accountsService) {
		this.accountsService = accountsService;
	}
	
	/**
	 * This method has no authorization requirement
	 * An anonymous user may call this method
	 * @return pong
	 */
	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
	
	/**
	 * Get info about the authenticated principal and his authorities 
	 * @param jwt The authentication principal, obtained from the JWT access token
	 * @param context The current security context
	 * @return The authenticated user, the active authorities
	 */
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/whoami")
	public Whoami index(@AuthenticationPrincipal Jwt jwt, @CurrentSecurityContext SecurityContext context) {
		Iterator<? extends GrantedAuthority> authorities = context.getAuthentication().getAuthorities().iterator();
		
		Whoami response = new Whoami();
		response.setFirstName(jwt.getClaimAsString("given_name"));
		response.setLastName(jwt.getClaimAsString("family_name"));

		List<String> roles = new ArrayList<>();
		while(authorities.hasNext()) {
			roles.add(authorities.next().getAuthority());
		}
		response.setRoles(roles);

		return response;
	}
	
	/**
	 * List accounts
	 * The API client requires the accounts:list scope
	 * The authenticated principal must have the customer role
	 * @param jwt The authentication principal, obtained from the JWT access token 
	 * @return List of accounts
	 */
	@PreAuthorize("hasAuthority('SCOPE_accounts:list') and hasAuthority('ROLE_customer')")
	@GetMapping("/accounts")
	public List<AccountResource> accounts(@AuthenticationPrincipal Jwt jwt) {
		List<AccountResource> accounts = accountsService.list(jwt.getClaimAsString("sub"));
		return accounts;
	}

	/**
	 * Account details
	 * The API client requires the accounts:details scope
	 * The authenticated principal must have the customer role
	 * @param jwt The authentication principal, obtained from the JWT access token
	 * @param accountNumber The account number
	 * @return List of accounts
	 */
	@PreAuthorize("hasAuthority('SCOPE_accounts:details') and hasAuthority('ROLE_customer')")
	@GetMapping("/accounts/{accountNumber}")
	public AccountResource accountDetails(@AuthenticationPrincipal Jwt jwt, @PathVariable String accountNumber) throws WrongAccountHolderException {
		return accountsService.details(accountNumber, jwt.getClaimAsString("sub"));
	}
	
}
