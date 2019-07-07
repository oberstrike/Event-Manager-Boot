package com.agil.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agil.model.Event;
import com.agil.model.Member;
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
	
	@Override
	public void delete(Event event) {
		eventRepository.delete(event);
	}
	
	@Override
	public void deleteById(Long id) {
		eventRepository.deleteById(id);
	}

	@Override
	public Optional<Event> findById(long parseLong) {
		return eventRepository.findById(parseLong);
	}

	@Override
	public List<Event> findByStartDateBetween(Date currentDate, Date after) {
		return eventRepository.findByStartDateBetween(currentDate, after);
	}

	@Override
	public void createAndSave(@Valid Event event, Member member) {
		event.addMember(member);
		member.addEvent(event);
		save(event);
	}

}
