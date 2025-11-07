package com.foodcourt.users.domain.exception;

public class InvalidLoginException extends BusinessException {
	public InvalidLoginException(String message) {
		super(message);
	}
}
