package com.agil.config;

import java.util.List;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.stereotype.Component;

import com.agil.model.ActiveUserStore;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Component
public class LoggedUser implements HttpSessionBindingListener {

	private String username;
	private ActiveUserStore activeUserStore;

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		List<String> users = activeUserStore.getMembers();
		LoggedUser user = (LoggedUser) event.getValue();
		if (!users.contains(user.username)) {
			users.add(user.username);
		}
		activeUserStore.setMembers(users);
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		List<String> users = activeUserStore.getMembers();
		LoggedUser user = (LoggedUser) event.getValue();
		if (users.contains(user.username)) {
			users.remove(user.username);
		}
		activeUserStore.setMembers(users);
	}

}
