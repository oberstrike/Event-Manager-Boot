package com.agil.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.agil.converter.DTOConverter;
import com.agil.dto.EventDTO;
import com.agil.model.Event;
import com.agil.model.Member;
import com.agil.repos.EventRepository;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	@Qualifier("eventConverter")
	private DTOConverter converter;

	@Override
	public void save(Event event) {
		eventRepository.save(event);
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
	public void createAndSave(@Valid EventDTO eventDTO, Member member) {
		Event event = (Event) converter.convert(eventDTO);
		event.addMember(member);
		event.setCreator(member);
		member.addEvent(event);
		save(event);
	}

	@Override
	public Page<Event> findByNameStartingWithIgnoreCase(String name, Pageable pageable) {
		return eventRepository.findByNameStartingWithIgnoreCaseAndFinishedFalse(name, pageable);
	}

	@Override
	public Page<Event> findByMembers_Name(String name, Pageable pageable) {
		return eventRepository.findByMembers_UsernameAndFinishedFalse(name, pageable);
	}

	@Override
	public List<Event> findByRememberDateBetween(Date first, Date second) {
		return eventRepository.findByRememberDateBetweenAndFinishedFalse(first, second);
	}

	@Override
	public void update(Event event, EventDTO eventDTO, List<Member> members) {
		if (event.getRememberDate() != null && eventDTO != null) {
			Date startDate = event.getStartDate();
			Date rememberDate = event.getRememberDate();
			long diff = startDate.getTime() + (startDate.getTime() - rememberDate.getTime());
			event.setRememberDate(new Date(diff));
		}
		if(eventDTO.getName() != null)
			event.setName(eventDTO.getName());
		if(eventDTO.getStartDate() != null)
			event.setStartDate(eventDTO.getStartDate());
		eventRepository.save(event);
	}

	@Override
	public EventDTO convert(Event event) {
		EventDTO eventDTO = (EventDTO) converter.convert(event);
		return eventDTO;
	}

}
