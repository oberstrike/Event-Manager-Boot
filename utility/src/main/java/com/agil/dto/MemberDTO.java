package com.agil.dto;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.agil.model.Member;

public class MemberDTO {

	public MemberDTO() {

	}

	public MemberDTO(Member member) {
		username = member.getUsername();
		email = member.getEmail();
		isAdmin = member.isAdmin();
		
	}

	@NotEmpty(message = "{username.notempty}")
	@Size(min = 6, max = 32, message = "{username.badformat}")
	private String username;

	@NotEmpty(message = "{password.notempty}")
	private String password;

	@Email
	@NotEmpty(message = "{email.notempty}")
	private String email;
	
	private boolean isAdmin;
	
	public boolean isAdmin() {
		return this.isAdmin;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public boolean isAgb() {
		return agb;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public void setAgb(boolean agb) {
		this.agb = agb;
	}

	@Transient
	@Size(min = 8, max = 32, message = "{password.badformat}")
	private String passwordConfirm;

	@Transient
	private boolean agb;

}
