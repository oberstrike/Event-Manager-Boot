package com.agil.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.agil.dto.EventDTO;
import com.agil.model.Event;
import com.agil.model.Member;

public interface EventService {
	public void save(Event event);

	void delete(Event event);

	void deleteById(Long id);

	public Optional<Event> findById(long parseLong);

	public List<Event> findByStartDateBetween(Date currentDate, Date after);
	
	public List<Event> findByRememberDateBetween(Date first, Date second);

	public void createAndSave( @Valid EventDTO event, Member member);

	Page<Event> findByMembers_Name(String name, Pageable pageable);

	Page<Event> findByNameStartingWithIgnoreCase(String name, Pageable pageable);

	void update(Event event, EventDTO eventDTO, List<Member> members);

	EventDTO convert(Event event);

}
