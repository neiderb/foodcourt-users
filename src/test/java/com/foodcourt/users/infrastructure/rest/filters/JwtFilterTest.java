package com.foodcourt.users.infrastructure.rest.filters;

import com.foodcourt.users.domain.exception.InvalidTokenException;
import com.foodcourt.users.domain.gateways.TokenServiceGateway;
import com.foodcourt.users.domain.model.UserClaims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static com.foodcourt.users.domain.model.UserRole.OWNER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @InjectMocks
    private JwtFilter jwtFilter;

    @Mock
    private TokenServiceGateway tokenServiceGateway;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;
	
	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final String ROLE_PREFIX = "ROLE_";

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSetAuthenticationWhenValidBearerToken() throws ServletException, IOException {
        final String token = "valid-token";
        final String bearerHeader = BEARER_PREFIX.concat(token);
        final UserClaims claims = new UserClaims(1L, "john.doe@mail.com", OWNER, 100L);

        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(bearerHeader);
        when(tokenServiceGateway.parseToken(token)).thenReturn(claims);

        jwtFilter.doFilter(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals(claims, auth.getPrincipal());
        assertEquals(token, auth.getCredentials());
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(ROLE_PREFIX.concat(OWNER.name()))));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldNotSetAuthenticationWhenHeaderMissing() throws ServletException, IOException {
        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(null);

        jwtFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldPropagateExceptionWhenTokenInvalid() throws ServletException, IOException {
        final String token = "bad-token";
        final String bearerHeader = BEARER_PREFIX.concat(token);

        when(request.getHeader(AUTHORIZATION_HEADER)).thenReturn(bearerHeader);
        when(tokenServiceGateway.parseToken(token)).thenThrow(new InvalidTokenException("invalid"));

        assertThrows(InvalidTokenException.class, () -> jwtFilter.doFilter(request, response, filterChain));

        verify(filterChain, never()).doFilter(request, response);
    }

}

