package com.foodcourt.users.domain.exception.restaurant;

import com.foodcourt.users.domain.exception.BusinessException;

public class RestaurantNotFoundException extends BusinessException {
	public RestaurantNotFoundException(String message) {
		super(message);
	}
}
