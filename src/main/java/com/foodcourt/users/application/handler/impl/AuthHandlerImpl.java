package com.foodcourt.users.application.handler.impl;

import com.foodcourt.users.application.dto.request.LoginRequest;
import com.foodcourt.users.application.handler.AuthHandler;
import com.foodcourt.users.domain.ports.LoginPort;
import com.foodcourt.users.infrastructure.rest.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthHandlerImpl implements AuthHandler {
	
	private final LoginPort loginPort;
	
	@Override
	public LoginResponse login(LoginRequest request) {
		return new LoginResponse(
			loginPort.login(request.email(), request.password())
		);
	}
	
}
