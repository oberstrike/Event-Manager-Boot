package com.agil.services;

import java.security.Principal;
import java.util.List;

import com.agil.model.Event;

public interface EventService {
	public void save(Event event);

	public List<Event> findByMembers_Name(String name);
	
}
