package com.foodcourt.users.infrastructure.rest.filters;

import com.foodcourt.users.domain.exception.BusinessException;
import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.infrastructure.rest.dto.ErrorApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

import static com.foodcourt.users.infrastructure.rest.constants.ErrorMessage.GENERIC_ERROR;
import static com.foodcourt.users.infrastructure.rest.constants.ErrorMessage.NOT_FOUND;
import static com.foodcourt.users.infrastructure.rest.docapi.GlobalDocApi.*;

@Slf4j
@RestControllerAdvice
public class ControllerExceptionHandler {
	
	
	@ExceptionHandler(BusinessException.class)
	@ApiResponse(
		responseCode = "400",
		description = DESCRIPTION_BAD_REQUEST,
		content = @Content(
			schema = @Schema(
				implementation = ErrorApiResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	ResponseEntity<ErrorApiResponse> handleBusinessException(BusinessException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		ErrorApiResponse response = new ErrorApiResponse(
			status.value(),
			ex.getMessage()
		);
		
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(TechnicalException.class)
	@ApiResponse(
		responseCode = "500",
		description = DESCRIPTION_INTERNAL_ERROR,
		content = @Content(
			schema = @Schema(
				implementation = ErrorApiResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	ResponseEntity<ErrorApiResponse> handleTechnicalException(TechnicalException ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		log.error(
			"Technical exception occurred: ",
			ex
		);
		
		ErrorApiResponse response = new ErrorApiResponse(
			status.value(),
			ex.getMessage()
		);
		
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ApiResponse(
		responseCode = "400",
		description = DESCRIPTION_BAD_REQUEST,
		content = @Content(
			schema = @Schema(
				implementation = ErrorApiResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	ResponseEntity<ErrorApiResponse> handleValidationException(MethodArgumentNotValidException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		String errorMessage = String.join(
			"; ",
			ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toSet())
		);
		
		ErrorApiResponse response = new ErrorApiResponse(
			status.value(),
			errorMessage
		);
		
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
	@ApiResponse(
		responseCode = "404",
		description = DESCRIPTION_RESOURCE_NOT_FOUND,
		content = @Content(
			schema = @Schema(
				implementation = ErrorApiResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	ResponseEntity<ErrorApiResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		log.warn(
			"Resource not found: ",
			ex
		);
		
		ErrorApiResponse response = new ErrorApiResponse(
			status.value(),
			NOT_FOUND
		);
		
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(Exception.class)
	@ApiResponse(
		responseCode = "500",
		description = DESCRIPTION_INTERNAL_ERROR,
		content = @Content(
			schema = @Schema(
				implementation = ErrorApiResponse.class
			),
			mediaType = MediaType.APPLICATION_JSON_VALUE
		)
	)
	ResponseEntity<ErrorApiResponse> handleGenericException(Exception ex) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		
		log.error(
			"Unexpected exception occurred: ",
			ex
		);
		
		ErrorApiResponse response = new ErrorApiResponse(
			status.value(),
			GENERIC_ERROR
		);
		
		return ResponseEntity.status(status).body(response);
	}
	
}
