package com.agil.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.agil.dto.MemberDTO;
import com.agil.model.Member;

public interface MemberService {
	Optional<Member> findById(long id);

	void save(@Valid Member memberForm);

	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);

	List<Member> findAll();

	void createAndRegister(@Valid MemberDTO memberForm);
}
