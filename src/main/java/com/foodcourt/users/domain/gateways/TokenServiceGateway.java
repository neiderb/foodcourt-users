package com.foodcourt.users.domain.gateways;

import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserClaims;

public interface TokenServiceGateway {
	
	String generateToken(User user);
	
	UserClaims parseToken(String token);
	
}
