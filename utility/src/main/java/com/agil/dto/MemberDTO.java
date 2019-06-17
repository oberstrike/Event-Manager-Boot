package com.agil.dto;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class MemberDTO {
	public MemberDTO(long id,
			@NotEmpty(message = "{username.notempty}") @Size(min = 6, max = 32, message = "{username.badformat}") String username,
			@NotEmpty(message = "{password.notempty}") String password,
			@Email @NotEmpty(message = "{email.notempty}") String email,
			@Size(min = 8, max = 32, message = "{password.badformat}") String passwordConfirm, boolean agb) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.passwordConfirm = passwordConfirm;
		this.agb = agb;
	}

	public MemberDTO() {
	
	}

	private long id;

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
}
