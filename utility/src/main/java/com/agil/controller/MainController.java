package com.agil.controller;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.agil.model.Event;
import com.agil.model.Member;
import com.agil.repos.MemberRepository;

@Controller
public class MainController {

	@Autowired
	private MemberRepository memberRepository;

	@GetMapping("/search")
	public String getSearchModal(Model model) {
		return "/fragments/general :: searchModalContent";
	}

	@PostMapping("/search")
	public String getSearch(Model model, @RequestParam("searchForm") String searchForm,
			@RequestHeader(required = false) String referer, Principal principal) {
		if (searchForm == null)
			if (referer.contains("members"))
				return "redirect:/members";
			else
				return "redirect:/home";
		if (referer == null)
			return "redirect:/home";

		return "redirect:/home";
	}

	@GetMapping("/event")
	public String getMember(Model model) {
		return "/fragments/general :: eventModalContent ";
	}
}
