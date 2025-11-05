package com.foodcourt.users.application.handler;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;

public interface UserHandler {
	
	UserResponse createUser(UserRequest userRequest);
	
	UserResponse getUserById(Long id);
	
}
