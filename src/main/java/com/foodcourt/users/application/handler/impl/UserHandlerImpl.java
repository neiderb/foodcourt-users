package com.foodcourt.users.application.handler.impl;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.application.handler.UserHandler;
import com.foodcourt.users.application.mappers.UserRequestMapper;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import com.foodcourt.users.domain.ports.CreateUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserHandlerImpl implements UserHandler {
	
	private final CreateUserPort createUserPort;
	
	@Override
	public UserResponse createUser(UserRequest userRequest) {
		User userToSave = UserRequestMapper.INSTANCE.toDomain(userRequest);
		userToSave.setRole(UserRole.getRoleof(userRequest.role()));
		
		User userSaved = createUserPort.execute(userToSave);
		
		return new UserResponse(
			userSaved.getId(),
			userSaved.getEmail(),
			userRequest.role()
		);
	}
	
}
