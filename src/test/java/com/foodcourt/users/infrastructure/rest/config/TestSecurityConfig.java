package com.foodcourt.users.infrastructure.rest.config;

import com.foodcourt.users.infrastructure.rest.filters.JwtFilter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import static org.mockito.Mockito.mock;

@TestConfiguration
@Import(SecurityConfig.class)
public class TestSecurityConfig {
	
	@Bean
	public JwtFilter jwtFilter() {
		return mock(JwtFilter.class);
	}
}
