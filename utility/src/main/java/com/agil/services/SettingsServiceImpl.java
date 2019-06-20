package com.agil.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

	@Value(value = "${config.allow.register}")
	private String allowRegistration;

	@Override
	public void setAllowRegistration(boolean allowRegistration) {
		this.allowRegistration = String.valueOf(allowRegistration);
	}

	@Override
	public boolean getAllowRegistration() {
		return Boolean.valueOf(this.allowRegistration);
	}

}
