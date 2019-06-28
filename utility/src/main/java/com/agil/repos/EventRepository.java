package com.agil.repos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.agil.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

}
