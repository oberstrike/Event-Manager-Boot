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
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Event implements Comparable<Event> {
	public Event() {
		this.finished = false;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Size(min = 6, max = 16, message = "{event.name.notempty}")
	@NotNull(message = "{event.name.notempty}")
	private String name;

	@ManyToMany(mappedBy = "events")
	private Set<Member> members = new HashSet<Member>();

	@ManyToOne(fetch = FetchType.LAZY)
	private Member creator;

	private boolean finished;
	
	private boolean remembered;

	@NotNull(message = "{event.date.badformat}")
	@DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
	private Date startDate;
	
	private Date rememberDate;


	public Event(String name, Date startDate) {
		this();
		this.name = name;
		this.startDate = startDate;
	}


	@Override
	public int compareTo(Event o) {
		return this.startDate.compareTo(o.startDate);
	}

	public void addMember(Member member) {
		this.members.add(member);

	}

	public boolean isCreator(String name2) {
		return this.creator.getUsername().equals(name2);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + (finished ? 1231 : 1237);
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rememberDate == null) ? 0 : rememberDate.hashCode());
		result = prime * result + (remembered ? 1231 : 1237);
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (finished != other.finished)
			return false;
		if (id != other.id)
			return false;
		if (members == null) {
			if (other.members != null)
				return false;
		} else if (!members.equals(other.members))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rememberDate == null) {
			if (other.rememberDate != null)
				return false;
		} else if (!rememberDate.equals(other.rememberDate))
			return false;
		if (remembered != other.remembered)
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

}
