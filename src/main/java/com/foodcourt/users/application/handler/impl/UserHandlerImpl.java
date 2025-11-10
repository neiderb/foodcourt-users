package com.foodcourt.users.application.handler.impl;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.application.handler.UserHandler;
import com.foodcourt.users.application.mappers.UserRequestMapper;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserClaims;
import com.foodcourt.users.domain.model.UserRole;
import com.foodcourt.users.domain.ports.CreateUserPort;
import com.foodcourt.users.domain.ports.GetUserByIdPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserHandlerImpl implements UserHandler {
	
	private final CreateUserPort createUserPort;
	private final GetUserByIdPort getUserByIdPort;
	
	@Override
	public UserResponse createUser(UserRequest userRequest) {
		log.trace("Creating user with email: {}", userRequest.email());
		User userToSave = UserRequestMapper.INSTANCE.toDomain(userRequest);
		userToSave.setRole(UserRole.getRoleof(userRequest.role()));
		UserClaims creatorClaims = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserClaims userClaims) {
			creatorClaims = userClaims;
		}
		
		log.trace("User creator role is: {}", creatorClaims != null ? creatorClaims.role() : "ANONYMOUS");
		User userSaved = createUserPort.execute(userToSave, creatorClaims);
		
		log.debug("Created user with ID: {}", userSaved.getId());
		return new UserResponse(
			userSaved.getId(),
			userSaved.getEmail(),
			userRequest.role().toLowerCase()
		);
	}
	
	@Override
	public UserResponse getUserById(Long id) {
		log.trace("Getting user by ID: {}", id);
		User user = getUserByIdPort.execute(id);
		
		log.debug("Found user with ID: {}", user.getId());
		return new UserResponse(
			user.getId(),
			user.getEmail(),
			user.getRole().name().toLowerCase()
		);
	}
}
