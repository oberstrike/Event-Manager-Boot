package com.agil.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.util.StreamUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.agil.converter.DTOConverter;
import com.agil.dto.MemberDTO;
import com.agil.model.Member;
import com.agil.model.VerificationToken;
import com.agil.repos.MemberRepository;
import com.agil.repos.VerificationTokenRepository;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	public BCryptPasswordEncoder encoder;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private VerificationTokenRepository tokenRepository;
	@Autowired
	@Qualifier("memberConverter")
	private DTOConverter converter;

	@Override
	public Optional<Member> findById(long id) {
		return memberRepository.findById(id);
	}

	@Override
	public void save(@Valid Member memberForm) {
		memberForm.setPassword(encoder.encode(memberForm.getPassword()));
		memberRepository.save(memberForm);
	}

	@Override
	public Optional<Member> findByUsername(String username) {
		return memberRepository.findByUsername(username);
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return memberRepository.findByEmail(email);
	}

	@Override
	public List<Member> findAll() {
		return StreamUtils.createStreamFromIterator(memberRepository.findAll().iterator()).collect(Collectors.toList());
	}

	@Override
	public Member createAndRegister(@Valid MemberDTO memberForm) {
		Member member = (Member) converter.convert(memberForm);
		save(member);
		return member;
	}

	@Override
	public void changeMember(Member member, MemberDTO memberForm) {
		if (memberForm.getPassword().equals(memberForm.getPasswordConfirm()))
			;
		member.setPassword(memberForm.getPassword());
		if (!memberForm.getUsername().equals(member.getUsername()))
			if (!findByUsername(memberForm.getUsername()).isPresent())
				member.setUsername(memberForm.getUsername());
		if (member.isEnabled() != memberForm.isEnabled())
			member.setEnabled(memberForm.isEnabled());

		save(member);
	}

	@Override
	public void refresh(Member member) {
		memberRepository.save(member);
	}

	@Override
	public void createVerificationToken(Member member, String token) {
		VerificationToken mytoken = new VerificationToken(token, member);
		tokenRepository.save(mytoken);
	}

	@Override
	public VerificationToken getToken(String token) {
		return tokenRepository.findByToken(token);
	}

	@Override
	public List<Member> findByUserameStartingWithIgnoreCase(String username) {
		return memberRepository.findByUsernameStartingWithIgnoreCase(username);
	}

	@Override
	public void delete(Member entity) {
		memberRepository.delete(entity);
	}
	
	

}
