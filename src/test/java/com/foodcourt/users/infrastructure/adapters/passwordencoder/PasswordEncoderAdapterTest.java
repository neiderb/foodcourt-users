package com.foodcourt.users.infrastructure.adapters.passwordencoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordEncoderAdapterTest {

    @InjectMocks
    private PasswordEncoderAdapter passwordEncoderAdapter;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldEncryptValueUsingPasswordEncoder() {
        final String raw = "mySecret";
        final String encoded = "encodedSecret";

        when(passwordEncoder.encode(raw)).thenReturn(encoded);

        String result = passwordEncoderAdapter.encrypt(raw);

        assertNotNull(result);
        assertEquals(encoded, result);
        verify(passwordEncoder).encode(raw);
    }

    @Test
    void shouldReturnTrueWhenPasswordMatches() {
        final String raw = "password123";
        final String encoded = "encodedPassword123";

        when(passwordEncoder.matches(raw, encoded)).thenReturn(true);

        Boolean matches = passwordEncoderAdapter.verify(raw, encoded);

        assertTrue(matches);
        verify(passwordEncoder).matches(raw, encoded);
    }

    @Test
    void shouldReturnFalseWhenPasswordDoesNotMatch() {
        final String raw = "password123";
        final String encoded = "otherEncoded";

        when(passwordEncoder.matches(raw, encoded)).thenReturn(false);

        Boolean matches = passwordEncoderAdapter.verify(raw, encoded);

        assertFalse(matches);
        verify(passwordEncoder).matches(raw, encoded);
    }

}

