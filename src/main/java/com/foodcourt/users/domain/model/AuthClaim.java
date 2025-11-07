package com.foodcourt.users.domain.model;

public enum AuthClaim {
	USER_ID("userId"),
	ROLE("role");
	
	public final String value;
	
	AuthClaim(String value) {
		this.value = value;
	}
}
