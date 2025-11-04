package com.foodcourt.users.infrastructure.rest.dto;

public record ErrorApiResponse(
	int code,
	String message
) {}
