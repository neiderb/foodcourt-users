package com.foodcourt.users.domain.ports;

import com.foodcourt.users.domain.model.User;

public interface CreateUserPort {
	
	User execute(User user);
	
}
