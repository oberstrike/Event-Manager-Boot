package com.agil.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.StreamUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.agil.model.Member;
import com.agil.repos.MemberRepository;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	public BCryptPasswordEncoder encoder;
	@Autowired
	private MemberRepository userRepo;

	@Override
	public Optional<Member> findById(long id) {
		return userRepo.findById(id);
	}

	@Override
	public void save(@Valid Member memberForm) {
		memberForm.setPassword(encoder.encode(memberForm.getPassword()));
		userRepo.save(memberForm);
	}

	@Override
	public Optional<Member> findByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public Optional<Member> findByEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Override
	public List<Member> findAll() {
		return StreamUtils.createStreamFromIterator(userRepo.findAll().iterator()).collect(Collectors.toList());
	}

}
