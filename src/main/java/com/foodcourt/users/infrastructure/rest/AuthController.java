package com.foodcourt.users.infrastructure.rest;

import com.foodcourt.users.application.dto.request.LoginRequest;
import com.foodcourt.users.application.handler.AuthHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.foodcourt.users.infrastructure.rest.constants.paths.AuthPath.BASE;
import static com.foodcourt.users.infrastructure.rest.constants.paths.AuthPath.LOGIN;
import static com.foodcourt.users.infrastructure.rest.docapi.AuthDocApi.*;

@Tag(name = TAG_AUTH)
@RestController
@RequiredArgsConstructor
@RequestMapping(BASE)
public class AuthController {
	
	private final AuthHandler authHandler;
	
	@Operation(summary = LOGIN_SUMMARY)
	@ApiResponse(
		responseCode = "200",
		description = LOGIN_DESCRIPTION,
		content = @Content(
			schema = @Schema(
				implementation = LoginResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	@PostMapping(LOGIN)
	ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
		return ResponseEntity.ok(authHandler.login(loginRequest));
	}
	
}
