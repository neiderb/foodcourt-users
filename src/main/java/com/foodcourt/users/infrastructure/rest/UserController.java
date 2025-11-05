package com.foodcourt.users.infrastructure.rest;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.application.handler.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.foodcourt.users.infrastructure.rest.constants.paths.UserPath.BASE;
import static com.foodcourt.users.infrastructure.rest.constants.paths.UserPath.FIND_BY_ID;
import static com.foodcourt.users.infrastructure.rest.docapi.UserDocApi.*;

@Tag(name = TAG_USER)
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class UserController {
	
	private final UserHandler userHandler;
	
	@Operation(summary = CREATE_USER_SUMMARY)
	@ApiResponse(
		responseCode = "201",
		description = CREATE_USER_DESCRIPTION,
		content = @Content(
			schema = @Schema(
				implementation = UserResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	@PostMapping
	ResponseEntity<UserResponse> createUser(@RequestBody @Valid UserRequest userRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(userHandler.createUser(userRequest));
	}
	
	@Operation(summary = GET_USER_BY_ID_SUMMARY)
	@ApiResponse(
		responseCode = "200",
		description = GET_USER_BY_ID_DESCRIPTION,
		content = @Content(
			schema = @Schema(
				implementation = UserResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	@GetMapping(FIND_BY_ID)
	ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
		return ResponseEntity.ok(userHandler.getUserById(id));
	}
	
}
