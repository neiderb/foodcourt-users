package com.foodcourt.users.application.handler.impl;

import com.foodcourt.users.application.dto.request.LoginRequest;
import com.foodcourt.users.domain.exception.InvalidLoginException;
import com.foodcourt.users.domain.ports.LoginPort;
import com.foodcourt.users.infrastructure.rest.dto.LoginResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthHandlerImplTest {

    @InjectMocks
    private AuthHandlerImpl authHandlerImpl;

    @Mock
    private LoginPort loginPort;

    @Test
    void shouldLoginSuccessfully() {
        final String email = "john.doe@mail.com";
        final String password = "password123";
        final String token = "token-xyz-123";

        LoginRequest request = new LoginRequest(email, password);

        when(loginPort.login(email, password)).thenReturn(token);

        LoginResponse response = authHandlerImpl.login(request);

        assertNotNull(response);
        assertEquals(token, response.token());
    }

    @Test
    void shouldPropagateInvalidLoginException() {
        final String email = "bad@mail.com";
        final String password = "wrong";

        LoginRequest request = new LoginRequest(email, password);

        when(loginPort.login(email, password)).thenThrow(new InvalidLoginException("Invalid credentials"));

        assertThrows(InvalidLoginException.class, () -> authHandlerImpl.login(request));
    }

}
