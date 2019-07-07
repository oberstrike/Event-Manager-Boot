package com.agil.utility;

import org.springframework.context.ApplicationEvent;

import com.agil.model.Event;

public class OnEventIsActiveEvent extends ApplicationEvent {

	private static final long serialVersionUID = 8421592341910188070L;
	
	private Event event;
	
	public OnEventIsActiveEvent(Event source) {
		super(source);
		setEvent(source);
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	

}
