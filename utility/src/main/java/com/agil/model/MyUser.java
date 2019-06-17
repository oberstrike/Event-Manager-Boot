package com.agil.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class MyUser extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5010761307397329186L;

	private SimpleUser user;
	
	public MyUser(Member user, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(user.getUsername(), user.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.user = new SimpleUser(user.getEmail(), user.getUsername(),user.getRoles());
	}

	public SimpleUser getUser() {
		return user;
	}	

}