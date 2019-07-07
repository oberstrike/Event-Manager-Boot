package com.agil.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agil.model.Event;
import com.agil.model.Member;
import com.agil.services.EventService;
import com.agil.services.MemberService;
import com.agil.utility.OnEventIsActiveEvent;

@Controller
public class EventController {

	@Autowired
	private EventService eventService;

	@Autowired
	private MemberService memberService;
	
	private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping("/events")
	public String getEvents(Model model, Principal principal) {
		List<Event> events = eventService.findByMembers_Name(principal.getName());
		Collections.sort(events);

		model.addAttribute("events", events);

		return "/fragments/event :: showEvents";
	}

	@GetMapping("/event")
	public String getEvent(Event eventForm, Model model) {
		model.addAttribute("eventForm", eventForm);
		return "/fragments/general :: eventModalContent ";
	}

	@GetMapping("/event/{id}")
	public String getEventById(@PathVariable("id") String id, Model model, Principal principal) {
		if (id == null)
			return "redirect:/home";
		if (!NumberUtils.isDigits(id))
			return "redirect:/home";
		int index = Integer.valueOf(id);
		List<Event> events = eventService.findByMembers_Name(principal.getName());
		if (index < 0 || index >= events.size())
			return "";
		Collections.sort(events);
		Event event = events.get(index);
		model.addAttribute("event", event);
		return "/fragments/general :: event ";

	}
	
	@PostMapping("/event/add")
	public String addEvent(@Valid @ModelAttribute("event") Event event, BindingResult bindingResult, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		if(bindingResult.hasErrors()) {
			String message = "";
			for (ObjectError error : bindingResult.getAllErrors()) {
				message += error.getDefaultMessage() + "\n";
			}			
			redirectAttributes.addFlashAttribute("message", message);
			return "redirect:/home";	
		}
		Member member = memberService.findByUsername(principal.getName()).get();
		eventService.createAndSave(event, member);
		redirectAttributes.addFlashAttribute("message", "A new event was created successfully");
		return "redirect:/home";
	}

	@GetMapping("/event/remove/{id}")
	public String removeEventById(@PathVariable("id") String id, Model model, Principal principal) {
		boolean fail = false;
		fail = (id == null);
		if (!fail)
			fail = (!NumberUtils.isDigits(id));
		if (!fail) {
			Event event = eventService.findById(Long.parseLong(id)).orElse(null);
			if (event == null)
				return "redirect:/events";
			event.setFinished(true);
			eventService.save(event);
		}

		return "redirect:/events";
	}
	
	@PostMapping("/event")

	@Scheduled(fixedRate = 1000 * 60)
	@Transactional
	public void job() {
		logger.info("--- Starting Job ---");
		Date currentDate = new Date(System.currentTimeMillis());
		Date after = new Date(System.currentTimeMillis() + 1000 * 60);
		List<Event> events = eventService.findByStartDateBetween(currentDate, after);
		for (Event event : events) {

			eventPublisher.publishEvent(new OnEventIsActiveEvent(event));
		}
		logger.info("Events: " + events.size());
		logger.info("--- Ending Job ---");

	}

}
