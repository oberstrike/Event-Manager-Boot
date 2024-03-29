package com.agil.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agil.converter.DTOConverter;
import com.agil.dto.MemberDTO;
import com.agil.model.ActiveUserStore;
import com.agil.model.Member;
import com.agil.services.MemberService;
import com.agil.utility.MemberValidator;
import com.agil.utility.Message;
import com.agil.utility.MessageType;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberValidator memberValidator;

	@Autowired
	private ActiveUserStore activeUserStore;
	
	@Autowired
	@Qualifier("memberConverter")
	private DTOConverter converter;

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Such Member")
	public class MemberNotFoundException extends RuntimeException {
		private static final long serialVersionUID = 10443642453485954L;

		public MemberNotFoundException() {
			super("No such Member found");
		}

	}

	@GetMapping("/members")
	public String getMembers(@RequestParam(value = "name", required = false) String name, Model model) {
		if (name == null)
			model.addAttribute("members", memberService.findAll());
		else
			model.addAttribute("members", memberService.findByUserameStartingWithIgnoreCase(name));

		return "members";
	}

	@GetMapping("/member/{id}")
	public String getMember(@PathVariable("id") String id, Model model, MemberDTO memberDTO) {
		long lId = Long.parseLong(id);
		if (lId != 0) {
			Member member = memberService.findById(lId).orElseThrow(MemberNotFoundException::new);
			model.addAttribute("member", (MemberDTO) converter.convert(member));
		} else {
			model.addAttribute("member", memberDTO);
		}

		return "/fragments/member :: memberModalContent ";
	}

	// TODO Email not duplicated Error beim Updaten wegen validate
	@PostMapping("/member/{id}")
	public String updateMember(@Valid @ModelAttribute("member") MemberDTO memberForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		memberForm.setAgb(true);

		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", getMessageOutOfResult(bindingResult));
			return "redirect:/members";
		}

		Member member = memberService.findById(memberForm.getId()).orElse(null);
		if (member == null)
			return "redirect:/members";
		memberService.changeMember(member, memberForm);
		return "redirect:/members";
	}

	@PostMapping("/member/create")
	public String createMember(@Valid @ModelAttribute("member") MemberDTO memberForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		memberForm.setAgb(true);
		memberValidator.validate(memberForm, bindingResult);
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("message", getMessageOutOfResult(bindingResult));
			return "redirect:/members";
		}

		memberService.createAndRegister(memberForm);
		redirectAttributes.addFlashAttribute("message",
				Message.MessageBuilder.create(MessageType.SUCCESS, "A new member was created successfully"));
		return "redirect:/members";
	}
	
	@GetMapping("/member/remove/{id}")
	public String removeMember(@PathVariable("id") String id, Model model, RedirectAttributes redirectAttributes) {
		Optional<Member> optional = memberService.findById(Long.valueOf(id));
		if(!optional.isPresent())
		{
			redirectAttributes.addAttribute("message", Message.MessageBuilder.create(MessageType.DANGER, "No such member found"));
			return "redirect:/members";
		}
		memberService.delete(optional.get());
		return "redirect:/members";
	}
	
	@GetMapping("/users")
	public String getLoggedInUsers(Model model) {
		model.addAttribute("users", activeUserStore.getMembers());
		
		return "user";
	}
	
	private Message getMessageOutOfResult(BindingResult bindingResult) {
		String message = "";
		for (ObjectError error : bindingResult.getAllErrors()) {
			message += error.getCode();
		}
		return Message.MessageBuilder.create(MessageType.DANGER, message);
	}

}
