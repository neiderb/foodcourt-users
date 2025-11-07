package com.foodcourt.users.infrastructure.rest.feature.user;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.application.handler.UserHandler;
import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.domain.exception.UserNotFoundException;
import com.foodcourt.users.infrastructure.rest.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.foodcourt.users.infrastructure.rest.constants.paths.UserPath.BASE;
import static com.foodcourt.users.infrastructure.rest.constants.paths.UserPath.FIND_BY_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
	controllers = UserController.class
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
	properties = {
		"server.port=0"
	}
)
@Import(TestSecurityConfig.class)
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private UserHandler userHandler;
	
	@Test
	void shouldCreateUser() throws Exception {
		UserResponse mockResponse = new UserResponse(
			1L,
			"john.doe@mail.com",
			"owner"
		);
		
		String jsonBody = """
				{
					"name": "John",
					"lastname": "Doe",
					"documentNumber": "1111111112",
					"phoneNumber": "+573151234567",
					"birthdate": "1990-01-01",
					"email": "john.doe@mail.com",
					"password": "123456",
					"role": "owner"
				}
			""";
		
		when(userHandler.createUser(any(UserRequest.class))).thenReturn(mockResponse);
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(mockResponse.id()))
			.andExpect(jsonPath("$.email").value(mockResponse.email()))
			.andExpect(jsonPath("$.role").value(mockResponse.role()));
	}
	
	
	@Test
	void shouldReturnBadRequestWhenCreateUserWithInvalidDocumentNumber() throws Exception {
		String jsonBody = """
				{
					"name": "John",
					"lastname": "Doe",
					"documentNumber": "111111111X",
					"phoneNumber": "+573151234567",
					"birthdate": "1990-01-01",
					"email": "john.doe@mail.com",
					"password": "123456",
					"role": "owner"
				}
			""";
		
		mockMvc.perform(post(BASE)
			.contentType(MediaType.APPLICATION_JSON.toString())
			.content(jsonBody))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnUserWhenGetById() throws Exception {
		final Long userId = 1L;
		UserResponse mockResponse = new UserResponse(
			userId,
			"mock.mail@mail.com",
			"owner"
		);
		
		when(userHandler.getUserById(userId)).thenReturn(mockResponse);
		
		mockMvc.perform(get(BASE.concat(FIND_BY_ID), userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(mockResponse.id()))
			.andExpect(jsonPath("$.email").value(mockResponse.email()))
			.andExpect(jsonPath("$.role").value(mockResponse.role()));
	}
	
	@Test
	void shouldReturnNotFoundWhenGetByIdWithNonexistentUser() throws Exception {
		final Long userId = 999L;
		
		when(userHandler.getUserById(userId)).thenThrow(new UserNotFoundException("User not found"));
		
		mockMvc.perform(get(BASE.concat(FIND_BY_ID), userId))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	void shouldReturnInternalServerErrorWhenHandlerThrowsUnexpectedException() throws Exception {
		final Long userId = 1L;
		
		when(userHandler.getUserById(userId)).thenThrow(new RuntimeException("Unexpected error"));
		
		mockMvc.perform(get(BASE.concat(FIND_BY_ID), userId))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	void shouldReturnInternalServerErrorWhenHandlerThrowsTechnicalException() throws Exception {
		final Long userId = 1L;
		
		when(userHandler.getUserById(userId)).thenThrow(new TechnicalException("Technical error"));
		
		mockMvc.perform(get(BASE.concat(FIND_BY_ID), userId))
			.andExpect(status().isInternalServerError());
	}
	
	@Test
	void shouldReturnNotFoundWhenPathIsInvalid() throws Exception {
		mockMvc.perform(get("/invalid-path/1"))
			.andExpect(status().isNotFound());
	}
}
