package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.UserNotFoundException;
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
class GetUserByIdUseCaseTest {
	
	@InjectMocks
	private GetUserByIdUseCase getUserByIdUseCase;
	
	@Mock
	private UserRepositoryGateway userRepositoryGateway;
	
	@Test
	void shouldGetUserById() {
		final Long id = 1L;
		User testUser = User.builder()
			.id(id)
			.name("Test")
			.lastname("User")
			.documentNumber("111111111")
			.phoneNumber("+1234567890")
			.birthdate(LocalDate.parse("1990-01-01"))
			.email("any_mail@mail.com")
			.password("securePassword123")
			.role(UserRole.OWNER)
			.build();
		
		when(userRepositoryGateway.findById(id)).thenReturn(testUser);
		
		User retrievedUser = getUserByIdUseCase.execute(id);
		
		assertNotNull(retrievedUser);
		assertEquals(id, retrievedUser.getId());
		assertEquals(testUser.getName(), retrievedUser.getName());
		assertEquals(testUser.getLastname(), retrievedUser.getLastname());
		assertEquals(testUser.getDocumentNumber(), retrievedUser.getDocumentNumber());
		assertEquals(testUser.getPhoneNumber(), retrievedUser.getPhoneNumber());
		assertEquals(testUser.getBirthdate(), retrievedUser.getBirthdate());
		assertEquals(testUser.getEmail(), retrievedUser.getEmail());
		assertEquals(testUser.getPassword(), retrievedUser.getPassword());
		assertEquals(testUser.getRole(), retrievedUser.getRole());
	}
	
	@Test
	void shouldThrowExceptionIfUserNotFound() {
		final Long id = 2L;
		
		when(userRepositoryGateway.findById(id)).thenReturn(null);
		
		assertThrows(UserNotFoundException.class, () -> getUserByIdUseCase.execute(id));
	}
	
}
