package com.agil.utility;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.agil.model.Event;
import com.agil.model.Member;

@Component
public class EventListener  implements ApplicationListener<OnEventIsActiveEvent>{

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnEventIsActiveEvent event) {
		sendMail(event);
	}

	private void sendMail(OnEventIsActiveEvent event) {
		Event e = event.getEvent();
		SimpleMailMessage email = new SimpleMailMessage();
		String[] to = e.getMembers().stream().map(Member::getEmail).toArray(String[]::new);

		email.setTo(to);
		email.setSubject(e.getName());
		email.setFrom("oberstrike@gmx.de");
		email.setText("Your Event is starting");
		
		mailSender.send(email);
		
	}

}
