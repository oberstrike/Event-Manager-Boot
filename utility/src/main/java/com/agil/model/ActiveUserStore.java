package com.agil.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ActiveUserStore {
	
	private List<String> members;
	
	public ActiveUserStore() {
		members = new ArrayList<>();
	}
	
	public void addMember(String member) {
		members.add(member);
	}
	
	public void removeMember(String member) {
		members.remove(member);
	}
	
	
}
