package com.foodcourt.users.application.handler.impl;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.domain.exception.InvalidRoleException;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import com.foodcourt.users.domain.ports.CreateUserPort;
import com.foodcourt.users.domain.ports.GetUserByIdPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHandlerImplTest {
	
	@InjectMocks
	private UserHandlerImpl userHandlerImpl;
	
	@Mock
	private CreateUserPort createUserPort;
	
	@Mock
	private GetUserByIdPort getUserByIdPort;
	
	private static final Long USER_ID = 1L;
	private static final String USER_NAME = "John";
	private static final String USER_LASTNAME = "Doe";
	private static final String USER_DOCUMENT_NUMBER = "123456789";
	private static final String USER_PHONE_NUMBER = "+573301234567";
	private static final LocalDate USER_BIRTHDATE = LocalDate.parse("1990-01-01");
	private static final String USER_EMAIL = "john.doe@mail.com";
	private static final String USER_PASSWORD = "pass123";
	private static final String USER_ROLE = "owner";
	
	@Test
	void shouldCreateUserSuccessfully() {
		UserRequest userRequest = validUserRequest();
		UserResponse expectedResponse = validUserResponse();
		
		when(createUserPort.execute(any(User.class))).thenAnswer(invocation -> {
			User userArg = invocation.getArgument(0);
			userArg.setId(USER_ID);
			return userArg;
		});
		
		UserResponse actualResponse = userHandlerImpl.createUser(userRequest);
		
		assertNotNull(actualResponse);
		assertEquals(expectedResponse.id(), actualResponse.id());
		assertEquals(expectedResponse.email(), actualResponse.email());
		assertEquals(expectedResponse.role(), actualResponse.role());
	}
	
	@Test
	void shouldThrowExceptionWhenRoleIsInvalid() {
		UserRequest userRequest = new UserRequest(
			USER_NAME,
			USER_LASTNAME,
			USER_DOCUMENT_NUMBER,
			USER_PHONE_NUMBER,
			USER_BIRTHDATE,
			USER_EMAIL,
			USER_PASSWORD,
			"invalid_role"
		);
		
		assertThrows(InvalidRoleException.class, () -> userHandlerImpl.createUser(userRequest));
	}
	
	@Test
	void shouldReturnUserOnGetById() {
		final Long userId = USER_ID;
		final User testUser = User.builder()
			.id(userId)
			.name(USER_NAME)
			.lastname(USER_LASTNAME)
			.documentNumber(USER_DOCUMENT_NUMBER)
			.phoneNumber(USER_PHONE_NUMBER)
			.birthdate(USER_BIRTHDATE)
			.email(USER_EMAIL)
			.password(USER_PASSWORD)
			.role(UserRole.OWNER)
			.build();
		
		when(getUserByIdPort.execute(userId)).thenReturn(testUser);
		
		UserResponse userResponse = userHandlerImpl.getUserById(userId);
		
		assertNotNull(userResponse);
		assertEquals(USER_ID, userResponse.id());
		assertEquals(USER_EMAIL, userResponse.email());
		assertEquals(USER_ROLE, userResponse.role());
	}
	
	private UserRequest validUserRequest() {
		return new UserRequest(
			USER_NAME,
			USER_LASTNAME,
			USER_DOCUMENT_NUMBER,
			USER_PHONE_NUMBER,
			USER_BIRTHDATE,
			USER_EMAIL,
			USER_PASSWORD,
			USER_ROLE
		);
	}
	
	private UserResponse validUserResponse() {
		return new UserResponse(
			USER_ID,
			USER_EMAIL,
			USER_ROLE
		);
	}
}
