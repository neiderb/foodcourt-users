package com.foodcourt.users.domain.ports;

import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserClaims;

public interface CreateUserPort {
	
	User execute(User userToCreate, UserClaims creatorClaims);
	
}
