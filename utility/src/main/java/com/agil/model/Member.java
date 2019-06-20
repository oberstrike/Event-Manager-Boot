package com.agil.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.agil.utility.UserRole;

@Entity
public class Member {

	public Member() {
		// TODO Auto-generated constructor stub
	}

	public Member(Set<UserRole> roles,
			@NotEmpty(message = "{username.notempty}") @Size(min = 6, max = 32, message = "{username.badformat}") String username,
			@NotEmpty(message = "{password.notempty}") String password,
			@Email @NotEmpty(message = "{email.notempty}") String email) {
		super();
		this.roles = roles;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name="enabled")
	private boolean enabled;

	@ElementCollection(targetClass = UserRole.class)
	@JoinTable(name = "memberRoles", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "role", nullable = true)
	@Enumerated(EnumType.STRING)
	private Set<UserRole> roles = new HashSet<>(Arrays.asList(UserRole.ROLE_USER));

	@NotEmpty(message = "{username.notempty}")
	@Size(min = 6, max = 32, message = "{username.badformat}")
	private String username;

	@NotEmpty(message = "{password.notempty}")
	private String password;

	@Email
	@NotEmpty(message = "{email.notempty}")
	private String email;

	@Transient
	@Size(min = 8, max = 32, message = "{password.badformat}")
	private String passwordConfirm;

	@Transient
	private boolean agb;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public Set<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public boolean isAgb() {
		return agb;
	}

	public void setAgb(boolean agb) {
		this.agb = agb;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	


}
