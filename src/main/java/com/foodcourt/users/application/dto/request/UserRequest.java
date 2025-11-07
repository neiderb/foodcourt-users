package com.foodcourt.users.application.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

import static com.foodcourt.users.domain.constants.Regex.JUST_NUMBERS;
import static com.foodcourt.users.domain.constants.Regex.JUST_NUMBERS_AND_SYMBOL_PLUS;
import static com.foodcourt.users.domain.constants.UserRules.PHONE_NUMBER_MAX_LENGTH;
import static com.foodcourt.users.domain.constants.ValidationMessage.*;

public record UserRequest(
	
	@NotNull(message = NAME_REQUIRED)
	@NotBlank(message = NAME_REQUIRED)
	String name,
	
	@NotNull(message = LASTNAME_REQUIRED)
	@NotBlank(message = LASTNAME_REQUIRED)
	String lastname,
	
	@NotNull(message = DOCUMENT_NUMBER_REQUIRED)
	@NotBlank(message = DOCUMENT_NUMBER_REQUIRED)
	@Pattern(regexp = JUST_NUMBERS, message = DOCUMENT_NUMBER_JUST_NUMBERS)
	String documentNumber,
	
	@NotNull(message = PHONE_NUMBER_REQUIRED)
	@NotBlank(message = PHONE_NUMBER_REQUIRED)
	@Size(max = PHONE_NUMBER_MAX_LENGTH, message = PHONE_NUMBER_AT_MOST)
	@Pattern(regexp = JUST_NUMBERS_AND_SYMBOL_PLUS, message = PHONE_NUMBER_JUST_NUMBERS_AND_SYMBOL_PLUS_IS_PERMITTED)
	String phoneNumber,
	
	@NotNull(message = BIRTHDATE_REQUIRED)
	@Past(message = PAST_DATE_REQUIRED)
	LocalDate birthdate,
	
	@NotNull(message = EMAIL_REQUIRED)
	@Email(message = EMAIL_INVALID)
	String email,
	
	@NotNull(message = PASSWORD_REQUIRED)
	@NotBlank(message = PASSWORD_REQUIRED)
	String password,
	
	@NotNull(message = ROLE_REQUIRED)
	@NotBlank(message = ROLE_REQUIRED)
	String role
	
) {}
