package com.agil.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agil.model.Member;
import com.agil.services.MemberService;
import com.agil.services.SecurityService;
import com.agil.utility.MemberValidator;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberValidator memberValidator;
	@Autowired
	private SecurityService securityService;

	@GetMapping("/home")
	public String getHome(Model model) {
		return "index";
	}

	@GetMapping("/")
	public String getMain(Model model) {
		return "redirect:/home";
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
	public String login(Model model, String error, String logout) {

		if (logout != null)
			model.addAttribute("message", "you have been logged out successfull");
		return "login";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader(required = false) String referer) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegisterPage(Model model) {
		model.addAttribute("memberForm", new Member());
		return "register";
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
	public String registration(@Valid @ModelAttribute("memberForm") Member memberForm, BindingResult bindingResult,
			@RequestHeader(required = false) String referer) {
		memberValidator.validate(memberForm, bindingResult);
		String password = memberForm.getPassword();
		if (bindingResult.hasErrors()) {
			return "register";
		}
		memberService.save(memberForm);
		securityService.autoLogin(memberForm.getUsername(), password);

		return "redirect:/home";
	}

	@GetMapping("/members")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String getMembers(Model model) {
		model.addAttribute("members", memberService.findAll());
		return "members";
	}
	
	@GetMapping("/member/{id}")
	public String getMember(@PathVariable("id") String id, Model model) {
		Member member = memberService.findById(Long.parseLong(id)).get();
		model.addAttribute("member", member);
		return "/fragments/general :: modalContents ";
	}
	
}
