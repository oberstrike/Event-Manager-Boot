package com.agil.utility;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.agil.model.Member;
import com.agil.services.MemberService;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private MessageSource messages;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void onApplicationEvent(OnRegistrationCompleteEvent event) {
		this.confirmRegistration(event);
	}

	private void confirmRegistration(OnRegistrationCompleteEvent event) {
		Member member = event.getMember();
		String token = UUID.randomUUID().toString();
		memberService.createVerificationToken(member, token);
		
		String recipientAddress = member.getEmail();
		String subject = "Registration Confirmation";
		String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
		String message = "To confirm use this link:";
		
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(recipientAddress);
		email.setSubject(subject);
		email.setFrom("oberstrike@gmx.de");
		email.setText(message + " " + "http://localhost:8080" + confirmationUrl);
	
		try {
			mailSender.send(email);
				
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
