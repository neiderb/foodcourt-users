package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.InvalidLoginException;
import com.foodcourt.users.domain.gateways.EncryptServiceGateway;
import com.foodcourt.users.domain.gateways.TokenServiceGateway;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {
	
	@InjectMocks
	private LoginUseCase loginUseCase;
	
	@Mock
	private UserRepositoryGateway userRepositoryGateway;
	
	@Mock
	private EncryptServiceGateway encryptServiceGateway;
	
	@Mock
	private TokenServiceGateway tokenServiceGateway;
	
	@Test
	void shouldLoginSuccessfully() {
		final String email = "john.doe@mail.com";
		final String rawPassword = "password123";
		final String encryptedPassword = "encryptedPassword123";
		final String expectedToken = "token-abc-123";
		User existingUser = validUser();
		existingUser.setPassword(encryptedPassword);
		when(userRepositoryGateway.findByEmail(email)).thenReturn(existingUser);
		when(encryptServiceGateway.verify(rawPassword, encryptedPassword)).thenReturn(true);
		when(tokenServiceGateway.generateToken(existingUser)).thenReturn(expectedToken);
		
		String token = loginUseCase.login(email, rawPassword);
		
		assertNotNull(token);
		assertEquals(expectedToken, token);
	}
	
	@Test
	void shouldThrowExceptionWhenEmailNotFound() {
		final String email = "notfound@mail.com";
		final String rawPassword = "anyPassword";
		
		when(userRepositoryGateway.findByEmail(email)).thenReturn(null);
		
		assertThrows(InvalidLoginException.class, () -> loginUseCase.login(email, rawPassword));
	}
	
	@Test
	void shouldThrowExceptionWhenPasswordDoesNotMatch() {
		final String email = "john.doe@mail.com";
		final String rawPassword = "wrongPassword";
		final String encryptedPassword = "encryptedPassword123";
		User existingUser = validUser();
		existingUser.setPassword(encryptedPassword);
		
		when(userRepositoryGateway.findByEmail(email)).thenReturn(existingUser);
		when(encryptServiceGateway.verify(rawPassword, encryptedPassword)).thenReturn(false);
		
		assertThrows(InvalidLoginException.class, () -> loginUseCase.login(email, rawPassword));
	}
	
	private User validUser() {
		return User.builder()
			.id(1L)
			.name("John")
			.lastname("Doe")
			.documentNumber("123456789")
			.phoneNumber("3156969584")
			.birthdate(LocalDate.parse("1990-01-01"))
			.email("john.doe@mail.com")
			.password("password123")
			.role(UserRole.OWNER)
			.build();
	}
	
}
