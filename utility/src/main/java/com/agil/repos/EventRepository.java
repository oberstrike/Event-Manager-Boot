package com.agil.repos;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agil.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

	Page<Event> findByMembers_UsernameAndFinishedFalse(String name, Pageable pageable);

	List<Event> findByStartDateBetween(Date date1, Date date2);
	
	Page<Event> findByNameStartingWithIgnoreCaseAndFinishedFalse(String name, Pageable pageable);

	List<Event> findByRememberDateBetweenAndFinishedFalse(Date first, Date second);
}
