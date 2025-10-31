package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.ports.CreateUserPort;
import com.foodcourt.users.domain.model.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserPort {
	
	private final UserRepositoryGateway userRepositoryGateway;
	
	@Override
	public User execute(User user) {
		return userRepositoryGateway.save(user);
	}
}
