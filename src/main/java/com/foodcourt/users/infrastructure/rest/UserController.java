package com.foodcourt.users.infrastructure.rest;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.application.handler.UserHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.foodcourt.users.infrastructure.rest.constants.paths.UserPath.BASE;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class UserController {
	
	private final UserHandler userHandler;
	
	@PostMapping
	ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
		return ResponseEntity.ok(userHandler.createUser(userRequest));
	}
	
}
