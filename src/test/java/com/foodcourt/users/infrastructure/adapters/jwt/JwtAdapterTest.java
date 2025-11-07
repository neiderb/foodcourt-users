package com.foodcourt.users.infrastructure.adapters.jwt;

import com.foodcourt.users.domain.exception.InvalidTokenException;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserClaims;
import com.foodcourt.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtAdapterTest {

    private static final String SECRET = Base64.getEncoder().encodeToString("01234567890123456789012345678901".getBytes());

    @Test
    void shouldGenerateAndParseToken() {
        JwtAdapter jwtAdapter = new JwtAdapter(SECRET, 1000L * 60L);

        User user = User.builder()
            .id(1L)
            .email("john.doe@mail.com")
            .role(UserRole.OWNER)
            .build();

        String token = jwtAdapter.generateToken(user);
        assertNotNull(token);

        UserClaims claims = jwtAdapter.parseToken(token);
        assertNotNull(claims);
        assertEquals(user.getId(), claims.id());
        assertEquals(user.getEmail(), claims.email());
        assertEquals(user.getRole(), claims.role());
    }

    @Test
    void shouldThrowInvalidTokenExceptionForMalformedToken() {
        JwtAdapter jwtAdapter = new JwtAdapter(SECRET, 1000L * 60L);

        String badToken = "this.is.not.a.valid.token";

        assertThrows(InvalidTokenException.class, () -> jwtAdapter.parseToken(badToken));
    }

    @Test
    void shouldThrowInvalidTokenExceptionForExpiredToken() {
        JwtAdapter jwtAdapter = new JwtAdapter(SECRET, -1L);

        User user = User.builder()
            .id(2L)
            .email("expired@mail.com")
            .role(UserRole.CLIENT)
            .build();

        String token = jwtAdapter.generateToken(user);
        assertNotNull(token);
		
        assertThrows(InvalidTokenException.class, () -> jwtAdapter.parseToken(token));
    }

}
