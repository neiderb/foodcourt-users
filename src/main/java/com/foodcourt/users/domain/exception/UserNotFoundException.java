package com.foodcourt.users.domain.exception;

public class UserNotFoundException extends BusinessException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
