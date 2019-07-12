package com.agil.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agil.model.Member;
import com.agil.services.MemberService;
import com.agil.utility.Message;
import com.agil.utility.MessageType;

@Controller
public class MainController {

	@Autowired
	private MemberService memberService;

	@GetMapping("/home")
	public String getHome(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "page", required = false) Integer page, Model model, Principal principal) {
		Member member = memberService.findByUsername(principal.getName()).get();
		model.addAttribute("member", member);
		
		if (name != null)
			model.addAttribute("name", name);
		if(page != null)
			model.addAttribute("page", page);

		return "index";
	}

	@GetMapping("/")
	public String getMain(Model model) {
		return "redirect:/home";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(required = false) String referer, RedirectAttributes redirectAttributes) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}

		redirectAttributes.addFlashAttribute("message",
				Message.MessageBuilder.create(MessageType.SUCCESS, "You have been successfully logged out"));
		return "redirect:/login?logout";
	}

	@GetMapping("/search")
	public String getSearchModal(Model model) {
		return "/fragments/general :: searchModalContent";
	}

	@PostMapping("/search/{type}")
	public String getSearch(@PathVariable("type") String type, @RequestParam("searchForm") String searchForm,
			Model model) {
		if (searchForm == null)
			return type != null ? "redirect:/members" : "redirect:/home";
		if (type == null)
			return "redirect:/home";
		else {
			if (type.equals("home"))
				return "redirect:/home?name=" + searchForm;
			else
				return "redirect:/members?name=" + searchForm;
		}

	}

}
