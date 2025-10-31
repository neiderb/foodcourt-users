package com.foodcourt.users.infrastructure.rest;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
	
	@PostMapping
	ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
		
		// TODO: Comunicar con el handler para crear el usuario
		return ResponseEntity.ok(
			new UserResponse(
				1L,
				userRequest.email(),
				userRequest.role()
			)
		);
	}
}
