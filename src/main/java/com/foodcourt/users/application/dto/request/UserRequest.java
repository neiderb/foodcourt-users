package com.foodcourt.users.application.dto.request;

public record UserRequest(
	String name,
	String lastname,
	String documentNumber,
	String phoneNumber,
	String birthdate,
	String email,
	String password,
	String role
) {
}
