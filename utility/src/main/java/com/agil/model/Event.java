package com.agil.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Event implements Comparable<Event> {
	public Event() {
		this.isFinished = false;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Size(min = 6, max = 16, message = "{event.name.notempty}")
	@NotNull(message = "{event.name.notempty}")
	private String name;

	@ManyToMany(mappedBy = "events")
	private Set<Member> members = new HashSet<Member>();

	private boolean isFinished;

	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	@NotNull(message = "{event.date.badformat}")
	private Date startDate;

	public String getName() {
		return name;
	}

	public Event(String name, Date startDate) {
		this();
		this.name = name;
		this.startDate = startDate;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public int compareTo(Event o) {
		return this.startDate.compareTo(o.startDate);
	}

	public void addMember(Member member) {
		this.members.add(member);

	}

	public Set<Member> getMembers() {
		return members;
	}

	public void setMembers(Set<Member> members) {
		this.members = members;
	}

}
