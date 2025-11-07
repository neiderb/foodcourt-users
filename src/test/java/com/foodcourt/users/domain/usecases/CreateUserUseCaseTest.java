package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.InvalidRoleException;
import com.foodcourt.users.domain.exception.InvalidUserException;
import com.foodcourt.users.domain.gateways.EncryptServiceGateway;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.foodcourt.users.domain.constants.UserRules.LEGAL_AGE;
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
	private EncryptServiceGateway encryptServiceGateway;
	
	@Test
	void shouldCreateUserSuccessfully() {
		final UserRole role = UserRole.OWNER;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		String encryptedPassword = "encryptedPassword123";
		User expectedUser = validUser();
		expectedUser.setId(1L);
		expectedUser.setPassword(encryptedPassword);
		expectedUser.setRole(role);
		
		when(userRepositoryGateway.save(userToCreate)).thenReturn(expectedUser);
		when(encryptServiceGateway.encrypt(anyString())).thenReturn(encryptedPassword);
		
		User userCreated = createUserUseCase.execute(userToCreate, UserRole.ADMIN);
		
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
		final UserRole role = UserRole.OWNER;
		User underageUser = validUser();
		underageUser.setRole(role);
		underageUser.setBirthdate(LocalDate.now().minusYears(LEGAL_AGE - 1));
		
		assertThrows(InvalidUserException.class, () -> createUserUseCase.execute(underageUser, UserRole.ADMIN));
	}
	
	@Test
	void shouldThrowExceptionWhenDocumentNumberAlreadyExists() {
		final UserRole role = UserRole.OWNER;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		
		when(userRepositoryGateway.findByDocumentNumber(userToCreate.getDocumentNumber()))
			.thenReturn(new User());
		
		assertThrows(InvalidUserException.class, () -> createUserUseCase.execute(userToCreate, UserRole.ADMIN));
	}
	
	@Test
	void shouldThrowExceptionWhenEmailAlreadyExists() {
		final UserRole role = UserRole.OWNER;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		
		when(userRepositoryGateway.findByEmail(userToCreate.getEmail()))
			.thenReturn(new User());
		
		assertThrows(InvalidUserException.class, () -> createUserUseCase.execute(userToCreate, UserRole.ADMIN));
	}
	
	@Test
	void shouldThrowExceptionWhenAnyoneTryToCreateAdmin() {
		final UserRole role = UserRole.ADMIN;
		User userToCreate = validUser();
		userToCreate.setRole(role);

		assertThrows(InvalidRoleException.class, () -> createUserUseCase.execute(userToCreate, UserRole.OWNER));
	}
	
	@Test
	void shouldThrowExceptionWhenCreateOwnerButNotAdmin() {
		final UserRole role = UserRole.OWNER;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		
		assertThrows(InvalidRoleException.class, () -> createUserUseCase.execute(userToCreate, UserRole.EMPLOYEE));
	}
	
	@Test
	void shouldThrowExceptionWhenCreateEmployeeButNotOwner() {
		final UserRole role = UserRole.EMPLOYEE;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		
		assertThrows(InvalidRoleException.class, () -> createUserUseCase.execute(userToCreate, UserRole.ADMIN));
	}
	
	@Test
	void shouldThrowExceptionWhenClientTryToCreateAnotherUser() {
		final UserRole role = UserRole.EMPLOYEE;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		
		assertThrows(InvalidRoleException.class, () -> createUserUseCase.execute(userToCreate, UserRole.CLIENT));
	}
	
	@Test
	void shouldThrowExceptionWhenNoRoleProvidedAndTryingToCreateNonClientUser() {
		final UserRole role = UserRole.EMPLOYEE;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		
		assertThrows(InvalidRoleException.class, () -> createUserUseCase.execute(userToCreate, null));
	}
	
	@Test
	void shouldCreateClientSuccessfullyWhenNoRoleProvided() {
		final UserRole role = UserRole.CLIENT;
		User userToCreate = validUser();
		userToCreate.setRole(role);
		String encryptedPassword = "encryptedPassword123";
		User expectedUser = validUser();
		expectedUser.setId(1L);
		expectedUser.setPassword(encryptedPassword);
		expectedUser.setRole(role);
		
		when(userRepositoryGateway.save(userToCreate)).thenReturn(expectedUser);
		when(encryptServiceGateway.encrypt(anyString())).thenReturn(encryptedPassword);
		
		User userCreated = createUserUseCase.execute(userToCreate, null);
		
		assertEquals(expectedUser.getName(), userCreated.getName());
		assertEquals(expectedUser.getLastname(), userCreated.getLastname());
		assertEquals(expectedUser.getDocumentNumber(), userCreated.getDocumentNumber());
		assertEquals(expectedUser.getPhoneNumber(), userCreated.getPhoneNumber());
		assertEquals(expectedUser.getBirthdate(), userCreated.getBirthdate());
		assertEquals(expectedUser.getEmail(), userCreated.getEmail());
		assertEquals(expectedUser.getPassword(), userCreated.getPassword());
		assertEquals(expectedUser.getRole(), userCreated.getRole());
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
			.build();
	}
	
}
