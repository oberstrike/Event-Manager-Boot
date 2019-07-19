package com.agil.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.agil.model.Member;

public class EventDTO {
	
	private long id;

	@Size(min = 6, max = 16, message = "{event.name.notempty}")
	@NotNull(message = "{event.name.notempty}")
	private String name;
	
	@NotNull(message = "{event.date.badformat}")
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	private Date startDate;

	private Date rememberDate;
	
	private String creatorName;
	
	private boolean finished;
	
	private boolean remembered;
	
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



	public boolean isRemembered() {
		return remembered;
	}

	public void setRemembered(boolean remembered) {
		this.remembered = remembered;
	}

	public Date getRememberDate() {
		return rememberDate;
	}

	public void setRememberDate(Date rememberDate) {
		this.rememberDate = rememberDate;
	}

	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isCreator(String name) {
		return creatorName.equals(name);
	}

	public void setCreator(String creator) {
		this.creatorName = creator;
	}

	public String getCreator() {
		return this.creatorName;
	}
	
}
