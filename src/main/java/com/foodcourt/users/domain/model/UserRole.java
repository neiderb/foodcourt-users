package com.foodcourt.users.domain.model;

import com.foodcourt.users.domain.exception.InvalidRoleException;

import static com.foodcourt.users.domain.constants.ErrorMessages.INVALID_ROLE;

public enum UserRole {
	
	ADMIN,
	OWNER,
	EMPLOYEE,
	CLIENT;
	
	public static UserRole getRoleof(String role) {
		for (UserRole userRole : UserRole.values()) {
			if (userRole.name().equalsIgnoreCase(role)) {
				return userRole;
			}
		}
		throw new InvalidRoleException(INVALID_ROLE);
	}
	
}
