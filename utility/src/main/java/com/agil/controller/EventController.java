package com.agil.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.agil.model.Event;
import com.agil.model.Member;
import com.agil.services.EventService;

@Controller
public class EventController {

	@Autowired
	private EventService eventService;
	
	@GetMapping("/event")
	public String getEvent(Model model) {
		return "/fragments/general :: eventModalContent ";
	}
	
	@GetMapping("/event/{id}")
	public String getEventById(@PathVariable("id") String id, Model model, Principal principal) {
		if(id == null)
			return "redirect:/home";
		if(!NumberUtils.isDigits(id))
			return "redirect:/home";
		int index = Integer.valueOf(id);
		List<Event> events =  eventService.findByMembers_Name(principal.getName());
		if(index < 0 || index >= events.size())
			return "redirect:/home";
		Collections.sort(events);	
		Event event = events.get(index);
		model.addAttribute("event", event);
		return "/fragments/general :: event ";
		
	}
	
	
	

}
