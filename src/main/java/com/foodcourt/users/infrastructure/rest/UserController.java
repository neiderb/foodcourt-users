package com.foodcourt.users.infrastructure.rest;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.application.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.foodcourt.users.infrastructure.rest.constants.paths.UserPath.BASE;
import static com.foodcourt.users.infrastructure.rest.docapi.UserDocApi.*;

@Tag(name = TAG_USER)
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class UserController {
	
	private final UserHandler userHandler;
	
	@Operation(summary = CREATE_USER_SUMMARY)
	@ApiResponse(responseCode = "201", description = CREATE_USER_DESCRIPTION, content = @Content)
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
		return ResponseEntity.ok(userHandler.createUser(userRequest));
	}
	
}
