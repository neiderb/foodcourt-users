package com.foodcourt.users.infrastructure.adapters.restaurant.dto;

public record ErrorExternalResponse(
	int code,
	String message
) {}
