package com.agil.repos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agil.model.Member;

@Repository
public interface MemberRepository extends CrudRepository<Member, Long> {

	Optional<Member> findByUsername(String username);

	Optional<Member> findByEmail(String email);

}