package com.foodcourt.users.domain.gateways;

import com.foodcourt.users.domain.model.User;

public interface UserRepositoryGateway {
	
	User save(User user);
	User findByDocumentNumber(String documentNumber);
	User findByEmail(String email);
	User findById(Long id);
	
}
