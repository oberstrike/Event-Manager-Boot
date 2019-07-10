package com.agil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.agil.services.SettingsService;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class SettingsController {

	@Autowired
	private SettingsService settingsService;

	@GetMapping("/settings")
	public String getSettings(Model model) {
		model.addAttribute("registration", settingsService.getAllowRegistration());
		return "settings";
	}

	@PostMapping("/settings/update")
	public String updateSettings(@RequestParam(name = "registration", required = false) Boolean registration,
			Model model) {
		if (registration == null)
			registration = false;
		settingsService.setAllowRegistration(registration);
		return "settings";
	}

}
