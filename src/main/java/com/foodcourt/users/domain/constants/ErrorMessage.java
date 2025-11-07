package com.foodcourt.users.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMessage {
	
	public static final String INVALID_ROLE = "The provided role is invalid";
	public static final String ROLE_NOT_FOUND = "The specified role was not found";
	
	public static final String USER_NOT_FOUND = "User not found";
	public static final String USER_WITH_THIS_DOCUMENT_NUMBER_ALREADY_EXISTS = "A user with this document number already exists";
	public static final String USER_WITH_THIS_EMAIL_ALREADY_EXISTS = "A user with this email already exists";
	
	public static final String INVALID_LOGIN_CREDENTIALS = "Invalid login credentials";
	public static final String INVALID_TOKEN = "The provided token is invalid";
	
	public static final String UNAUTHORIZED_ACTION = "You are not authorized to perform this action";
	
}
