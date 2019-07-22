package com.agil.dto;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {


	private long id;

	@NotEmpty(message = "{username.notempty}")
	@Size(min = 6, max = 32, message = "{username.badformat}")
	private String username;

	@NotEmpty(message = "{password.notempty}")
	private String password;

	@Email
	@NotEmpty(message = "{email.notempty}")
	private String email;

	private boolean isAdmin;

	private boolean enabled;

	public boolean isAgb() {
		return agb;
	}

	@Transient
	@Size(min = 8, max = 32, message = "{password.badformat}")
	private String passwordConfirm;

	@Transient
	private boolean agb;

}
