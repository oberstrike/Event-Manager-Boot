package com.agil.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agil.converter.DTOConverter;
import com.agil.dto.EventDTO;
import com.agil.model.Event;
import com.agil.model.Member;
import com.agil.services.EventService;
import com.agil.services.MemberService;
import com.agil.utility.Message;
import com.agil.utility.MessageType;
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
	public String getEvents(@RequestParam(name = "name", required = false) String name,
			@RequestParam(value = "page", required = false) String page, Model model, Principal principal) {
		Page<Event> pageEvents = null;
		int p = 0;

		if (page != null)
			p = Integer.valueOf(page) - 1;
		if (name != null) {
			pageEvents = eventService.findByNameStartingWithIgnoreCase(name,
					PageRequest.of(p, 9, Sort.by("startDate").descending()));
			model.addAttribute("name", name);
		} else {
			pageEvents = eventService.findByMembers_Name(principal.getName(),
					PageRequest.of(p, 9, Sort.by("startDate").descending()));
		}

		int pageCount = pageEvents.getTotalPages();

		List<EventDTO> pages = pageEvents.getContent().stream().map(event -> {
			EventDTO eventDTO = eventService.convert(event);
			eventDTO.setCreator(event.getCreator().getUsername());
			return eventDTO;
		}).collect(Collectors.toList());

		model.addAttribute("pages", pageCount);
		model.addAttribute("page", p + 1);
		model.addAttribute("events", pages);
		model.addAttribute("username", principal.getName());
		return "/fragments/event :: showEvents";
	}

	@GetMapping("/event")
	public String getEvent(EventDTO eventForm, Model model) {
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
		List<Event> events = eventService.findByMembers_Name(principal.getName(), PageRequest.of(0, 9)).getContent();
		if (index < 0 || index >= events.size())
			return "";
		Collections.sort(events);
		Event event = events.get(index);

		model.addAttribute("event", eventService.convert(event));
		return "/fragments/general :: event ";

	}

	@PostMapping("/event/add")
	public String addEvent(@Valid @ModelAttribute("event") EventDTO event,
			@RequestParam("minutesToRemember") String minutes, BindingResult bindingResult, Model model,
			Principal principal, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", getMessageOutOfResult(bindingResult));
			return "redirect:/home";
		}
		if (minutes != null) {
			event.setRemembered(true);
			event.setRememberDate(new Date(event.getStartDate().getTime() - Long.parseLong(minutes) * 1000 * 60));
		} else {
			event.setRemembered(false);
		}
		event.setFinished(false);
		Member member = memberService.findByUsername(principal.getName()).get();
		eventService.createAndSave(event, member);
		redirectAttributes.addFlashAttribute("message",
				Message.MessageBuilder.create(MessageType.SUCCESS, "A new event was created successfully"));
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
			if (event == null || !event.isCreator(principal.getName()))
				return "redirect:/home";
			event.setFinished(true);
			eventService.save(event);
		}

		return "redirect:/events";
	}

	@GetMapping("/event/settings/{id}")
	public String getSettingsById(@PathVariable("id") String id, Model model, Principal principal, EventDTO eventDTO) {
		boolean fail = false;
		fail = (id == null);
		if (!fail)
			fail = (!NumberUtils.isDigits(id));
		if (!fail) {
			Event event = eventService.findById(Long.parseLong(id)).orElse(null);
			if (event == null || !event.isCreator(principal.getName()))
				return "redirect:/home";
			model.addAttribute("event", event);
			model.addAttribute("eventForm", eventDTO);
		}
		return "/fragments/event :: eventSettingsContent";
	}

	@PostMapping("/event/update/{id}")
	public String updateEvent(@RequestParam(value = "names[]", required = false) String[] names,
			@PathVariable("id") String id, @ModelAttribute("eventForm") EventDTO eventDTO, Model model,
			Principal principal) {
		boolean fail = false;
		fail = (id == null);
		if (!fail)
			fail = (!NumberUtils.isDigits(id));
		if (!fail) {
			Event event = eventService.findById(Long.parseLong(id)).orElse(null);

			List<Member> members = names != null
					? Arrays.asList(names).stream().map(memberService::findByUsername).filter(Optional::isPresent)
							.map(Optional::get).collect(Collectors.toList())
					: new ArrayList<Member>();
			if (event == null || !event.isCreator(principal.getName()))
				return "redirect:/home";
			eventService.update(event, eventDTO, members);
			for (Member member : members) {
				member.addEvent(event);
				memberService.refresh(member);
			}

		}
		return "redirect:/home";
	}

	private Message getMessageOutOfResult(BindingResult bindingResult) {
		String message = "";
		for (ObjectError error : bindingResult.getAllErrors()) {
			message += error.getCode();
		}
		return Message.MessageBuilder.create(MessageType.DANGER, message);
	}

	@Scheduled(fixedRate = 1000 * 60)
	@Transactional
	public void job() {
		logger.info("--- Starting Job ---");
		Date currentDate = new Date(System.currentTimeMillis());
		Date after = new Date(System.currentTimeMillis() + 1000 * 60);

		eventService.findByRememberDateBetween(currentDate, after).parallelStream().forEach(each -> {
			eventPublisher.publishEvent(new OnEventIsActiveEvent(each));
			logger.info(each.getName());
		});
		logger.info("--- Ending Job ---");
	}

}
