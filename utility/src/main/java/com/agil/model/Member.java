package com.agil.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.agil.utility.MemberRole;

@Entity
public class Member {

	public Member() {
		this.enabled = false;
}

	public Member(Set<MemberRole> roles,
			@NotEmpty(message = "{username.notempty}") @Size(min = 6, max = 32, message = "{username.badformat}") String username,
			@NotEmpty(message = "{password.notempty}") String password,
			@Email @NotEmpty(message = "{email.notempty}") String email) {
		super();
		this.roles = roles;
		this.username = username;
		this.password = password;
		this.email = email;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "enabled")
	private boolean enabled;

	@ElementCollection(targetClass = MemberRole.class)
	@JoinTable(name = "memberRoles", joinColumns = @JoinColumn(name = "id"))
	@Column(name = "role", nullable = true)
	@Enumerated(EnumType.STRING)
	private Set<MemberRole> roles = new HashSet<>(Arrays.asList(MemberRole.ROLE_USER));

	@ManyToMany
	@JoinTable(name = "member_event", joinColumns = @JoinColumn(name = "member_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
	private Set<Event> events = new HashSet<>();

	private String username;

	private String password;

	private String email;

	private boolean agb;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Set<MemberRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<MemberRole> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public boolean isAgb() {
		return agb;
	}

	public void setAgb(boolean agb) {
		this.agb = agb;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAdmin() {
		return this.roles == null ? false : roles.contains(MemberRole.ROLE_ADMIN);
	}

	public Set<Event> getEvents() {
		return events;
	}

	public List<Event> getSortedEvents() {
		List<Event> list = new ArrayList<Event>(events);
		return list;

	}

	public void setEvents(Set<Event> events) {
		this.events = events;
	}

	public void addEvent(Event event) {
		event.addMember(this);
		this.events.add(event);
	}

	public void removeEvent(Event event) {
		events.remove(event);
	}
	
}
