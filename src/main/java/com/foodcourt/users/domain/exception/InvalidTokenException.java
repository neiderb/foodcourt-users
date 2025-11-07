package com.foodcourt.users.domain.exception;

public class InvalidTokenException extends BusinessException {
	public InvalidTokenException(String message) {
		super(message);
	}
}
