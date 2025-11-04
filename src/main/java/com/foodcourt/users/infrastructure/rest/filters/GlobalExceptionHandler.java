package com.foodcourt.users.infrastructure.rest.filters;

import com.foodcourt.users.domain.exception.BusinessException;
import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.infrastructure.rest.dto.ErrorApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.foodcourt.users.domain.constants.ErrorMessage.GENERIC_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BusinessException.class)
	ResponseEntity<ErrorApiResponse> handleBusinessException(BusinessException ex) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		ErrorApiResponse response = new ErrorApiResponse(
			status.value(),
			ex.getMessage()
		);
		
		return ResponseEntity.status(status).body(response);
	}
	
	@ExceptionHandler(TechnicalException.class)
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
	
	@ExceptionHandler(Exception.class)
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
