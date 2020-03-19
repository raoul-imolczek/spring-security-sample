package com.imolczek.training.spring.springsecuritysample.services.mock;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.imolczek.training.spring.springsecuritysample.exceptions.WrongAccountHolderException;
import com.imolczek.training.spring.springsecuritysample.interfaces.rest.resources.AccountResource;
import com.imolczek.training.spring.springsecuritysample.services.AccountsService;

@Service
public class AccountsServiceMock implements AccountsService {

	@Override
	public List<AccountResource> list(String accountHolderId) {
		
		List<AccountResource> accounts = new ArrayList<>();

		AccountResource accountA = new AccountResource();
		accountA.setAccountNumber("12345");
		accounts.add(accountA);
		
		AccountResource accountB = new AccountResource();
		accountB.setAccountNumber("ABCDE");
		accounts.add(accountB);
		
		return accounts;
	}

	@Override
	public AccountResource details(String accountNumber, String accountHolderId) throws WrongAccountHolderException {

		if(accountNumber.endsWith("9")) throw new WrongAccountHolderException();
		
		AccountResource account = new AccountResource();
		account.setAccountNumber(accountNumber);
		
		return account;
	}

}
