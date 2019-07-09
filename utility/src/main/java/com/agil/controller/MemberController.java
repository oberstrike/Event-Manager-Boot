package com.agil.controller;

import java.security.Principal;
import java.util.Calendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agil.dto.MemberDTO;
import com.agil.model.Member;
import com.agil.model.VerificationToken;
import com.agil.services.MemberService;
import com.agil.services.SettingsService;
import com.agil.utility.MemberValidator;
import com.agil.utility.OnRegistrationCompleteEvent;

@Controller
@PreAuthorize("hasRole('ROLE_ANONYMOUS')")
public class MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberValidator memberValidator;

	@Autowired
	private SettingsService settingsService;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@RequestMapping(value = "/login", method = { RequestMethod.GET })
	public String login(Model model, String error, String logout, Principal principal,
			@RequestHeader(required = false) String referer) {
		if (principal != null)
			return "redirect:/home";

		if (logout != null)
			model.addAttribute("message", "you have been logged out successfull");
		return "login";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String getRegisterPage(Model model) {
		if (!settingsService.getAllowRegistration())
			return "redirect:/login";

		model.addAttribute("memberForm", new MemberDTO());
		return "register";
	}

	@PostMapping("/register")
	public String registration(@Valid @ModelAttribute("memberForm") MemberDTO memberForm, BindingResult bindingResult,
			@RequestHeader(required = false) String referer, WebRequest request, Model model,
			RedirectAttributes attributes) {
		if (!settingsService.getAllowRegistration())
			return "redirect:/login";
		memberValidator.validate(memberForm, bindingResult);
		String password = memberForm.getPassword();
		String confirmPassword = memberForm.getPasswordConfirm();
		if (!password.equals(confirmPassword))
			bindingResult.addError(new ObjectError("password", "password.notequal"));
		if (bindingResult.hasErrors()) {
			model.addAttribute("message", getMessageOutOfResult(bindingResult));
			return "register";
		}
		Member member = memberService.createAndRegister(memberForm);
		try {
			String appUrl = request.getContextPath();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(member, request.getLocale(), appUrl));
		} catch (Exception e) {
			e.printStackTrace();
		}

		attributes.addFlashAttribute("message", "Check your emails");
		return "redirect:/login";
	}

	@GetMapping("/registrationConfirm")
	public String confirmRegistration(WebRequest request, Model model, @RequestParam("token") String token,
			RedirectAttributes attributes) {
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

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Member")
	public class MemberNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 10443642453485954L;

		public MemberNotFoundException() {
			super("No such Member found");
		}

	}

	private String getMessageOutOfResult(BindingResult bindingResult) {
		String message = "";
		for (ObjectError error : bindingResult.getAllErrors()) {
			message += error.getCode();
		}
		return message;
	}

}
