package com.agil.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class EventDTO {
	
	private long id;

	@Size(min = 6, max = 16, message = "{event.name.notempty}")
	@NotNull(message = "{event.name.notempty}")
	private String name;
	
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "{event.date.badformat}")
	private Date startDate;

	public String getName() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
