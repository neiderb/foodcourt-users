package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.InvalidUserException;
import com.foodcourt.users.domain.gateways.EncryptService;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.foodcourt.users.domain.constants.UserConstants.LEGAL_AGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {
	
	@InjectMocks
	private CreateUserUseCase createUserUseCase;
	
	@Mock
	private UserRepositoryGateway userRepositoryGateway;
	
	@Mock
	private EncryptService encryptService;
	
	@Test
	void shouldCreateUserSuccessfully() {
		User userToCreate = validUser();
		String encryptedPassword = "encryptedPassword123";
		User expectedUser = validUser();
		expectedUser.setId(1L);
		expectedUser.setPassword(encryptedPassword);
		
		when(userRepositoryGateway.save(userToCreate)).thenReturn(expectedUser);
		when(encryptService.encrypt(anyString())).thenReturn(encryptedPassword);
		
		User userCreated = createUserUseCase.execute(userToCreate);
		
		assertEquals(expectedUser.getName(), userCreated.getName());
		assertEquals(expectedUser.getLastname(), userCreated.getLastname());
		assertEquals(expectedUser.getDocumentNumber(), userCreated.getDocumentNumber());
		assertEquals(expectedUser.getPhoneNumber(), userCreated.getPhoneNumber());
		assertEquals(expectedUser.getBirthdate(), userCreated.getBirthdate());
		assertEquals(expectedUser.getEmail(), userCreated.getEmail());
		assertEquals(expectedUser.getPassword(), userCreated.getPassword());
		assertEquals(expectedUser.getRole(), userCreated.getRole());
	}
	
	@Test
	void shouldThrowExceptionWhenUserIsUnderage() {
		User underageUser = validUser();
		underageUser.setBirthdate(LocalDate.now().minusYears(LEGAL_AGE - 1));
		
		assertThrows(InvalidUserException.class, () -> createUserUseCase.execute(underageUser));
	}
	
	@Test
	void shouldThrowExceptionWhenDocumentNumberAlreadyExists() {
		User userToCreate = validUser();
		
		when(userRepositoryGateway.findByDocumentNumber(userToCreate.getDocumentNumber()))
			.thenReturn(new User());
		
		assertThrows(InvalidUserException.class, () -> createUserUseCase.execute(userToCreate));
	}
	
	@Test
	void shouldThrowExceptionWhenEmailAlreadyExists() {
		User userToCreate = validUser();
		
		when(userRepositoryGateway.findByEmail(userToCreate.getEmail()))
			.thenReturn(new User());
		
		assertThrows(InvalidUserException.class, () -> createUserUseCase.execute(userToCreate));
	}
	
	private User validUser() {
		return User.builder()
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
