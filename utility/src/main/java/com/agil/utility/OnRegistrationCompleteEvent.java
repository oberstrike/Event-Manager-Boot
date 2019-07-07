package com.agil.utility;

import java.util.Locale;

import org.springframework.context.ApplicationEvent;

import com.agil.model.Member;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6402680591398689944L;
	private String appUrl;
	private Locale locale;
	private Member member;
	
	public OnRegistrationCompleteEvent(Object source) {
		super(source);
	}

	public OnRegistrationCompleteEvent(Member member, Locale locale, String appUrl) {
		super(member);
		
		this.member = member;
		this.locale = locale;
		this.appUrl = appUrl;
		
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
