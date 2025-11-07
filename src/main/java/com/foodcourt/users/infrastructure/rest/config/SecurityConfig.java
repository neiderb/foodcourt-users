package com.foodcourt.users.infrastructure.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.users.infrastructure.rest.constants.paths.AuthPath;
import com.foodcourt.users.infrastructure.rest.constants.paths.UserPath;
import com.foodcourt.users.infrastructure.rest.dto.ErrorApiResponse;
import com.foodcourt.users.infrastructure.rest.filters.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.foodcourt.users.infrastructure.rest.constants.ErrorMessage.ACCESS_DENIED;
import static com.foodcourt.users.infrastructure.rest.constants.ErrorMessage.UNAUTHORIZED;
import static org.springframework.http.HttpMethod.POST;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtFilter jwtFilter;
	private final ObjectMapper objectMapper;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(AuthPath.BASE.concat(AuthPath.LOGIN)).permitAll()
				.requestMatchers(POST, UserPath.BASE).permitAll()
				.anyRequest().authenticated())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling(handling -> handling
				.authenticationEntryPoint(jwtAuthenticationEntryPoint())
				.accessDeniedHandler(jwtAccessDeniedHandler())
			);
			
		return http.build();
	}
	
	public AuthenticationEntryPoint jwtAuthenticationEntryPoint() {
		final int status = HttpStatus.UNAUTHORIZED.value();
		return (request, response, authException) -> {
			response.setStatus(status);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ErrorApiResponse errorData = new ErrorApiResponse(
				status,
				UNAUTHORIZED
			);
			
			objectMapper.writeValue(response.getOutputStream(), errorData);
		};
	}
	
	public AccessDeniedHandler jwtAccessDeniedHandler() {
		final int status = HttpStatus.FORBIDDEN.value();
		return (request, response, accessDeniedException) -> {
			response.setStatus(status);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ErrorApiResponse errorData = new ErrorApiResponse(
				status,
				ACCESS_DENIED
			);
			
			objectMapper.writeValue(response.getOutputStream(), errorData);
		};
	}
	
}
