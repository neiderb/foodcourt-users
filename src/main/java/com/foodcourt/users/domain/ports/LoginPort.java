package com.foodcourt.users.domain.ports;

public interface LoginPort {
	
	String login(String email, String password);
	
}
