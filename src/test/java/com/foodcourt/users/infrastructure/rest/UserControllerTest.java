package com.foodcourt.users.infrastructure.rest;

import com.foodcourt.users.application.dto.request.UserRequest;
import com.foodcourt.users.application.dto.response.UserResponse;
import com.foodcourt.users.application.handler.UserHandler;
import com.foodcourt.users.infrastructure.rest.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.foodcourt.users.infrastructure.rest.constants.paths.UserPath.BASE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@TestPropertySource(properties = {
	"server.port=0"
})
@Import(SecurityConfig.class)
class UserControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private UserHandler userHandler;
	
	@Test
	void shouldCreateuser() throws Exception {
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
			.andExpect(status().isOk())
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
	
}
