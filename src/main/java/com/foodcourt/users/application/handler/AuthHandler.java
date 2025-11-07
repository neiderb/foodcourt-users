package com.foodcourt.users.application.handler;

import com.foodcourt.users.application.dto.request.LoginRequest;
import com.foodcourt.users.infrastructure.rest.dto.LoginResponse;

public interface AuthHandler {
	
	LoginResponse login(LoginRequest request);
	
}
