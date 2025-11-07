package com.foodcourt.users.infrastructure.rest.feature.auth;

import com.foodcourt.users.application.dto.request.LoginRequest;
import com.foodcourt.users.application.handler.AuthHandler;
import com.foodcourt.users.infrastructure.rest.config.TestSecurityConfig;
import com.foodcourt.users.infrastructure.rest.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.foodcourt.users.infrastructure.rest.constants.paths.AuthPath.BASE;
import static com.foodcourt.users.infrastructure.rest.constants.paths.AuthPath.LOGIN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
	controllers = AuthController.class
)
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(
	properties = {
		"server.port=0"
	}
)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthHandler authHandler;

    @Test
    void shouldReturnTokenWhenLogin() throws Exception {
        final String token = "token-abc-123";

        String jsonBody = """
                {
                    "email": "john.doe@mail.com",
                    "password": "password123"
                }
            """;

        when(authHandler.login(any(LoginRequest.class))).thenReturn(new LoginResponse(token));

        mockMvc.perform(post(BASE.concat(LOGIN))
            .contentType(APPLICATION_JSON.toString())
            .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void shouldReturnBadRequestWhenLoginWithInvalidEmail() throws Exception {
        String jsonBody = """
                {
                    "email": "invalid-email",
                    "password": "password123"
                }
            """;

        mockMvc.perform(post(BASE.concat(LOGIN))
            .contentType(APPLICATION_JSON.toString())
            .content(jsonBody))
            .andExpect(status().isBadRequest());
    }
	
}

