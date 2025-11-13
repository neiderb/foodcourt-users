package com.foodcourt.users.domain.model;

public enum AuthClaim {
	USER_ID("userId"),
	ROLE("role"),
	RESTAURANT_ID("restaurantId");
	
	public final String value;
	
	AuthClaim(String value) {
		this.value = value;
	}
}
