package com.foodcourt.users.domain.ports;

import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;

public interface CreateUserPort {
	
	User execute(User userToCreate, UserRole roleCreator);
	
}
