package com.agil.controller;

import java.security.Principal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.agil.dto.MemberDTO;
import com.agil.model.Event;
import com.agil.model.Member;
import com.agil.model.VerificationToken;
import com.agil.services.EventService;
import com.agil.services.MemberService;
import com.agil.services.SecurityService;
import com.agil.services.SettingsService;
import com.agil.utility.MemberValidator;
import com.agil.utility.OnRegistrationCompleteEvent;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberValidator memberValidator;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping("/home")
	public String getHome(Model model, Principal principal) {
		Member member = memberService.findByUsername(principal.getName()).get();
		model.addAttribute("member", member);
		return "index";
	}

	@GetMapping("/")
	public String getMain(Model model, RedirectAttributes redirectAttributes) {
		return "redirect:/home";
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public String login(Model model, String error, String logout, Principal principal, @RequestHeader(required = false) String referer) {
		if(principal != null)
			return "redirect:/home";
		
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
		if (!settingsService.getAllowRegistration())
			return "redirect:/login";

		model.addAttribute("memberForm", new MemberDTO());
		return "register";
	}

	@PostMapping("/register")
	@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
	public String registration(@Valid @ModelAttribute("memberForm") MemberDTO memberForm, BindingResult bindingResult,
			@RequestHeader(required = false) String referer, WebRequest request, Model model, RedirectAttributes attributes) {
		if (!settingsService.getAllowRegistration())
			return "redirect:/login";

		memberValidator.validate(memberForm, bindingResult);
//		String password = memberForm.getPassword();
		if (bindingResult.hasErrors()) {
			return "register";
		}
		Member member = memberService.createAndRegister(memberForm);
		try {
			String appUrl = request.getContextPath();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(member, request.getLocale(), appUrl));
		} catch (Exception e) {
			// TODO: handle exception
		}

		// securityService.autoLogin(memberForm.getUsername(), password);
		attributes.addFlashAttribute("message", "Check your emails");
		return "redirect:/login";
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
		model.addAttribute("member", new MemberDTO(member));
		return "/fragments/general :: memberModalContent ";
	}

	@PostMapping("/member/{id}")
	public String updateMember(@Valid @ModelAttribute("member") MemberDTO memberForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "redirect:/members";

		Member member = memberService.findById(memberForm.getId()).orElse(null);
		if (member == null)
			return "redirect:/members";
		memberService.changeMember(member, memberForm);
		return "redirect:/members";
	}

	@GetMapping("/registrationConfirm")
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token, RedirectAttributes attributes) {
		VerificationToken myToken = memberService.getToken(token);
		if (myToken == null) {
			attributes.addFlashAttribute("message", "Unknown token");
			return "redirect:/login";
		}

		Member member = myToken.getMember();
		Calendar cal = Calendar.getInstance();
		if ((myToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			attributes.addFlashAttribute("message", "Your token is expired");
			return "redirect:/login";
		}

		member.setEnabled(true);
		memberService.refresh(member);
		attributes.addFlashAttribute("message", "Your account is activated!");
		
		return "redirect:/login";
	}

}
