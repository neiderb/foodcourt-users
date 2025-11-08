package com.foodcourt.users.domain.exception;

public class UserIsNotOwnerRestaurantException extends BusinessException {
	public UserIsNotOwnerRestaurantException(String message) {
		super(message);
	}
}
