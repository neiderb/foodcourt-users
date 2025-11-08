package com.foodcourt.users.domain.constants;

import lombok.experimental.UtilityClass;

import static com.foodcourt.users.domain.constants.UserRules.PHONE_NUMBER_MAX_LENGTH;

@UtilityClass
public class ValidationMessage {
	
	public static final String NAME_REQUIRED = "Name is required";
	public static final String LASTNAME_REQUIRED = "Lastname is required";
	public static final String DOCUMENT_NUMBER_REQUIRED = "Document number is required";
	public static final String PHONE_NUMBER_REQUIRED = "Phone number is required";
	public static final String BIRTHDATE_REQUIRED = "Birthdate is required";
	public static final String EMAIL_REQUIRED = "Email is required";
	public static final String PASSWORD_REQUIRED = "Password is required";
	public static final String ROLE_REQUIRED = "Role is required";
	
	public static final String EMAIL_INVALID = "Email format is invalid";
	public static final String PHONE_NUMBER_AT_MOST = "Phone number must be at most " + PHONE_NUMBER_MAX_LENGTH + " characters long";
	public static final String PHONE_NUMBER_JUST_NUMBERS_AND_SYMBOL_PLUS_IS_PERMITTED = "Phone number must contain just numbers and the symbol + is permitted";
	public static final String DOCUMENT_NUMBER_JUST_NUMBERS = "Document number must contain just numbers";
	public static final String USER_MUST_BE_OF_LEGAL_AGE = "User must be of legal age";
	public static final String PAST_DATE_REQUIRED = "Birthdate must be a past date";
	public static final String RESTAURANT_ID_REQUIRED_FOR_EMPLOYEE = "Restaurant ID is required for employee role";
	public static final String EMPLOYEE_NEEDS_TO_BELONG_TO_SAME_RESTAURANT_AS_OWNER = "Employee must belong to the same restaurant as the owner creating them";
	
}
