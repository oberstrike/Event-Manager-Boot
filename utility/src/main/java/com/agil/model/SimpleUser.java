package com.agil.model;

import java.util.Set;

import com.agil.utility.UserRole;;

public class SimpleUser {

	String email;
	String username;
	Set<UserRole> roles;
	
	public SimpleUser(String email, String username, Set<UserRole> roles) {
		super();
		this.email = email;
		this.username = username;
		this.roles = roles;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Set<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
	
	public String getRolesReadable() {
		String output = "";
		for(UserRole ur : getRoles()) {
			output += ur.getDescription() + ", ";
		}
		output = output.substring(0, output.length()-2);
		return output;
	}
	
}