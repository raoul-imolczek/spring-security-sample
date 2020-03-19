package com.imolczek.training.spring.springsecuritysample.interfaces.rest.resources;

import java.util.List;

public class Whoami {

	private String firstName;
	private String lastName;
	private List<String> roles;
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public List<String> getRoles() {
		return roles;
	}
	
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
