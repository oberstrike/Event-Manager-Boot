package com.agil;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.agil.model.Member;
import com.agil.repos.MemberRepository;
import com.agil.utility.UserRole;

@SpringBootApplication
public class UtilityApplication extends SpringBootServletInitializer {
	@Autowired
	public BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(UtilityApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(UtilityApplication.class);
	}

	@Bean
	CommandLineRunner init(MemberRepository userRepository) {
		return (args) -> {
			Member user = new Member(new HashSet<>(Arrays.asList(UserRole.ROLE_USER, UserRole.ROLE_ADMIN)),
					"oberstrike", encoder.encode("mewtu123"), "markus.juergens@gmx.de");
			user.setEnabled(true);
			userRepository.save(user);

			user = new Member(new HashSet<>(Arrays.asList(UserRole.ROLE_USER)), "markus", encoder.encode("mewtu123"),
					"oberstrike@gmx.de");
			user.setEnabled(true);
			userRepository.save(user);
		};

	}

}
