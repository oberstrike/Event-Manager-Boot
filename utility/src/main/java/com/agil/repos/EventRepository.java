package com.agil.repos;

import java.util.Date;
import java.util.List;

import javax.persistence.TemporalType;

import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agil.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

	List<Event> findByMembers_Username(String name);

	List<Event> findByStartDateBetween(Date date1, Date date2);
	
}
