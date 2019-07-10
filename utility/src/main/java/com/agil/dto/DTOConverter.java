package com.agil.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.agil.model.Event;
import com.agil.model.Member;


@Component
public class DTOConverter {

	public DTOConverter() {
		
	}
	@Autowired
	private  Member member;

	@Autowired
	private  MemberDTO memberDTO;

	@Autowired
	private  Event event;

	@Autowired
	private  EventDTO eventDTO;
	
	
	public  EventDTO convertToEventDTO(Event event) {
		eventDTO.setName(event.getName());
		eventDTO.setStartDate(event.getStartDate());
		eventDTO.setId(event.getId());
		return eventDTO;
	}
	
	public  Event convertToEvent(EventDTO eventDTO) {
		event.setId(eventDTO.getId());
		event.setName(eventDTO.getName());
		event.setStartDate(eventDTO.getStartDate());
		return event;
	}
	
	public  MemberDTO convertToMemberDTO(Member member) {
		memberDTO.setId(member.getId());
		memberDTO.setAgb(member.isAgb());
		memberDTO.setEmail(member.getEmail());
		memberDTO.setUsername(member.getUsername());
		memberDTO.setIsAdmin(member.isAdmin());
		memberDTO.setPassword(member.getPassword());
		return memberDTO;
	}
	
	public  Member convertToMember(MemberDTO memberDTO) {
		member.setId(memberDTO.getId());
		member.setAgb(memberDTO.isAgb());
		member.setEmail(memberDTO.getEmail());
		member.setUsername(memberDTO.getUsername());
		member.setPassword(memberDTO.getPassword());
		return member;
	}




	
	
	
}
