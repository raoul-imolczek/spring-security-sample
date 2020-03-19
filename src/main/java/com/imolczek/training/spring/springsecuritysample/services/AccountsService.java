package com.imolczek.training.spring.springsecuritysample.services;

import java.util.List;

import com.imolczek.training.spring.springsecuritysample.exceptions.WrongAccountHolderException;
import com.imolczek.training.spring.springsecuritysample.interfaces.rest.resources.AccountResource;

public interface AccountsService {

	public List<AccountResource> list(String accountHolderId);

	public AccountResource details(String accountNumber, String accountHolderId) throws WrongAccountHolderException;
	
}
