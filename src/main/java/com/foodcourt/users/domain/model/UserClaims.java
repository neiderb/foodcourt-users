package com.foodcourt.users.domain.model;

public record UserClaims(
	Long id,
	String email,
	UserRole role,
	Long idRestaurant
) {}
