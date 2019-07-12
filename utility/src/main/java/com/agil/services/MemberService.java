package com.agil.services;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.agil.dto.MemberDTO;
import com.agil.model.Member;
import com.agil.model.VerificationToken;

public interface MemberService {
	Optional<Member> findById(long id);

	void save(@Valid Member memberForm);

	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);

	List<Member> findAll();

	Member createAndRegister(@Valid MemberDTO memberForm);

	void changeMember(Member member, MemberDTO memberForm);

	void refresh(Member member);

	void createVerificationToken(Member member, String token);

	VerificationToken getToken(String token);
	
	List<Member> findByUserameStartingWithIgnoreCase(String username);
	
	
}
