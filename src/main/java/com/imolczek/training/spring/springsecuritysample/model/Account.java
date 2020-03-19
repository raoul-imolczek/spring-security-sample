package com.imolczek.training.spring.springsecuritysample.model;

public class Account {

	private String accountNumber;
	private String accountHolderId;
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getAccountHolderId() {
		return accountHolderId;
	}
	
	public void setAccountHolderId(String accountHolderId) {
		this.accountHolderId = accountHolderId;
	}
	
}
