package com.agil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.agil.model.Event;
import com.agil.model.Member;
import com.agil.repos.EventRepository;
import com.agil.repos.MemberRepository;
import com.agil.utility.MemberRole;

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
	CommandLineRunner init(MemberRepository userRepository, EventRepository eventRepository) {
		return (args) -> {
			Member user = new Member(new HashSet<>(Arrays.asList(MemberRole.ROLE_USER, MemberRole.ROLE_ADMIN)),
					"oberstrike", encoder.encode("mewtu123"), "markus.juergens@gmx.de");
			
			for (int i = 0; i < 10; i++) {
				Event event = new Event("VWL-Klausur " + new Random().nextInt(100), new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2019-07-28 09:0" + 1 * i));
				eventRepository.save(event);
				user.addEvent(event);
			}

			
			user.getEvents().size();
			
			user.setEnabled(true);
			userRepository.save(user);

			user = new Member(new HashSet<>(Arrays.asList(MemberRole.ROLE_USER)), "markus", encoder.encode("mewtu123"),
					"oberstrike@gmx.de");
			user.setEnabled(true);
			userRepository.save(user);
		};

	}
	
	

}
