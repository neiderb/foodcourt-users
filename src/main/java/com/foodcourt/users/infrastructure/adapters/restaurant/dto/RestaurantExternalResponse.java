package com.foodcourt.users.infrastructure.adapters.restaurant.dto;

public record RestaurantExternalResponse(
	Long id,
	String name,
	String nit,
	String address,
	String phoneNumber,
	String urlLogo,
	Long ownerId
) {}
