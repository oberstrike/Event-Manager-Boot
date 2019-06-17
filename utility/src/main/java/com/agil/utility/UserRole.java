package com.agil.utility;

public enum UserRole {
	ROLE_ADMIN("Administrator"), 
	ROLE_USER("Benutzer");
	
	private String description;
	
	private UserRole(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
}
}
