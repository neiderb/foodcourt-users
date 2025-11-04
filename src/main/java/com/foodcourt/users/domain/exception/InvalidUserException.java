package com.foodcourt.users.domain.exception;

public class InvalidUserException extends BusinessException {
	public InvalidUserException(String message) {
		super(message);
	}
}
