package com.foodcourt.users.infrastructure.rest.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessage {
	
	public static final String GENERIC_ERROR = "An unexpected error occurred, please try again later";
	public static final String NOT_FOUND = "Resource not found";
	public static final String UNAUTHORIZED = "Unauthorized access";
	public static final String ACCESS_DENIED = "Access denied";
	
}
