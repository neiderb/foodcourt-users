package com.foodcourt.users.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import static com.foodcourt.users.domain.constants.ValidationMessage.*;
import static com.foodcourt.users.domain.constants.ValidationMessage.PASSWORD_REQUIRED;

public record LoginRequest(
	@NotNull(message = EMAIL_REQUIRED)
	@Email(message = EMAIL_INVALID)
	String email,
	
	@NotNull(message = PASSWORD_REQUIRED)
	@NotBlank(message = PASSWORD_REQUIRED)
	String password
) {}
