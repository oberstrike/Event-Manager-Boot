package com.agil.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agil.model.Event;
import com.agil.repos.EventRepository;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Override
	public void save(Event event) {
		eventRepository.save(event);
	}

	@Override
	public List<Event> findByMembers_Name(String name) {
		return eventRepository.findByMembers_Username(name);
	}

}
