package com.foodcourt.users.application.dto.response;

public record UserResponse(
	Long id,
	String email,
	String role,
	String phone
) {}
