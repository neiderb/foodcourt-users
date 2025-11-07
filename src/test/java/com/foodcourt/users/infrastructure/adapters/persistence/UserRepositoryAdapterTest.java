package com.foodcourt.users.infrastructure.adapters.persistence;

import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import com.foodcourt.users.infrastructure.adapters.persistence.entities.RoleData;
import com.foodcourt.users.infrastructure.adapters.persistence.entities.UserData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.foodcourt.users.domain.constants.UserRules.LEGAL_AGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {
	
	@InjectMocks
	private UserRepositoryAdapter userRepositoryAdapter;
	
	@Mock
	private UserJpaRepository userJpaRepository;
	
	@Mock
	private RoleJpaRepository roleJpaRepository;
	
	private static final Long ROLE_ID_OWNER = 1L;
	private static final String USER_NAME = "John";
	private static final String USER_LASTNAME = "Doe";
	private static final String USER_DOCUMENT_NUMBER = "123456789";
	private static final String USER_PHONE_NUMBER = "+573301234567";
	private static final LocalDate USER_BIRTHDATE = LocalDate.now().minusYears(LEGAL_AGE + 5);
	private static final String USER_EMAIL = "john_doe@mail.com";
	private static final String USER_PASSWORD = "password123";
	private static final UserRole USER_ROLE = UserRole.OWNER;
	
	@Test
	void shouldSaveUserSuccessfully() {
		User userToSave = validUser();
		userToSave.setRole(USER_ROLE);
		RoleData roleData = validRoleOwnerData();
		UserData testUserData = validUserData();
		testUserData.setRole(roleData);
		
		when(roleJpaRepository.findByName(userToSave.getRole().name())).thenReturn(roleData);
		when(userJpaRepository.save(any(UserData.class))).thenReturn(testUserData);
		
		User savedUser = userRepositoryAdapter.save(userToSave);
		
		assertNotNull(savedUser);
		assertNotNull(savedUser.getId());
		assertEquals(USER_NAME, savedUser.getName());
		assertEquals(USER_LASTNAME, savedUser.getLastname());
		assertEquals(USER_DOCUMENT_NUMBER, savedUser.getDocumentNumber());
		assertEquals(USER_PHONE_NUMBER, savedUser.getPhoneNumber());
		assertEquals(USER_BIRTHDATE, savedUser.getBirthdate());
		assertEquals(USER_EMAIL, savedUser.getEmail());
		assertEquals(USER_PASSWORD, savedUser.getPassword());
		assertEquals(userToSave.getRole(), savedUser.getRole());
		
		verify(roleJpaRepository, times(1)).findByName(userToSave.getRole().name());
	}
	
	@Test
	void shouldThrowExceptionWhenRoleNotFoundOnSave() {
		User userToSave = validUser();
		userToSave.setRole(USER_ROLE);
		
		when(roleJpaRepository.findByName(userToSave.getRole().name())).thenReturn(null);
		
		assertThrows(TechnicalException.class, () -> userRepositoryAdapter.save(userToSave));
		
		verify(roleJpaRepository, times(1)).findByName(userToSave.getRole().name());
		verify(userJpaRepository, never()).save(any(UserData.class));
	}
	
	@Test
	void shouldReturnUserWhenFindByDocumentNumberExists() {
		RoleData roleData = validRoleOwnerData();
		UserData testUserData = validUserData();
		testUserData.setRole(roleData);
		
		when(userJpaRepository.findByDocumentNumber(USER_DOCUMENT_NUMBER)).thenReturn(testUserData);
		
		User foundUser = userRepositoryAdapter.findByDocumentNumber(USER_DOCUMENT_NUMBER);
		
		assertNotNull(foundUser);
		assertEquals(USER_NAME, foundUser.getName());
		assertEquals(USER_LASTNAME, foundUser.getLastname());
		assertEquals(USER_DOCUMENT_NUMBER, foundUser.getDocumentNumber());
		assertEquals(USER_PHONE_NUMBER, foundUser.getPhoneNumber());
		assertEquals(USER_BIRTHDATE, foundUser.getBirthdate());
		assertEquals(USER_EMAIL, foundUser.getEmail());
		assertEquals(USER_PASSWORD, foundUser.getPassword());
		assertEquals(USER_ROLE, foundUser.getRole());
	}
	
	@Test
	void shouldReturnNullWhenFindByDocumentNumberNotExists() {
		when(userJpaRepository.findByDocumentNumber(USER_DOCUMENT_NUMBER)).thenReturn(null);
		
		User foundUser = userRepositoryAdapter.findByDocumentNumber(USER_DOCUMENT_NUMBER);
		
		assertNull(foundUser);
	}
	
	@Test
	void shouldReturnUserWhenFindByEmailExists() {
		RoleData roleData = validRoleOwnerData();
		UserData testUserData = validUserData();
		testUserData.setRole(roleData);
		
		when(userJpaRepository.findByEmail(USER_EMAIL)).thenReturn(testUserData);
		
		User foundUser = userRepositoryAdapter.findByEmail(USER_EMAIL);
		
		assertNotNull(foundUser);
		assertEquals(USER_NAME, foundUser.getName());
		assertEquals(USER_LASTNAME, foundUser.getLastname());
		assertEquals(USER_DOCUMENT_NUMBER, foundUser.getDocumentNumber());
		assertEquals(USER_PHONE_NUMBER, foundUser.getPhoneNumber());
		assertEquals(USER_BIRTHDATE, foundUser.getBirthdate());
		assertEquals(USER_EMAIL, foundUser.getEmail());
		assertEquals(USER_PASSWORD, foundUser.getPassword());
		assertEquals(USER_ROLE, foundUser.getRole());
	}
	
	@Test
	void shouldReturnNullWhenFindByEmailNotExists() {
		when(userJpaRepository.findByEmail(USER_EMAIL)).thenReturn(null);
		
		User foundUser = userRepositoryAdapter.findByEmail(USER_EMAIL);
		
		assertNull(foundUser);
	}
	
	@Test
	void shouldReturnUserWhenFindByIdExists() {
		RoleData roleData = validRoleOwnerData();
		UserData testUserData = validUserData();
		testUserData.setRole(roleData);
		
		when(userJpaRepository.findById(1L)).thenReturn(Optional.of(testUserData));
		
		User foundUser = userRepositoryAdapter.findById(1L);
		
		assertNotNull(foundUser);
		assertEquals(USER_NAME, foundUser.getName());
		assertEquals(USER_LASTNAME, foundUser.getLastname());
		assertEquals(USER_DOCUMENT_NUMBER, foundUser.getDocumentNumber());
		assertEquals(USER_PHONE_NUMBER, foundUser.getPhoneNumber());
		assertEquals(USER_BIRTHDATE, foundUser.getBirthdate());
		assertEquals(USER_EMAIL, foundUser.getEmail());
		assertEquals(USER_PASSWORD, foundUser.getPassword());
		assertEquals(USER_ROLE, foundUser.getRole());
	}
	
	@Test
	void shouldReturnNullWhenFindByIdNotExists() {
		when(userJpaRepository.findById(1L)).thenReturn(Optional.empty());
		
		User foundUser = userRepositoryAdapter.findById(1L);
		
		assertNull(foundUser);
	}
	
	private User validUser() {
		return User.builder()
			.name(USER_NAME)
			.lastname(USER_LASTNAME)
			.documentNumber(USER_DOCUMENT_NUMBER)
			.phoneNumber(USER_PHONE_NUMBER)
			.birthdate(USER_BIRTHDATE)
			.email(USER_EMAIL)
			.password(USER_PASSWORD)
			.build();
	}
	
	private UserData validUserData() {
		return UserData.builder()
			.id(1L)
			.name(USER_NAME)
			.lastname(USER_LASTNAME)
			.documentNumber(USER_DOCUMENT_NUMBER)
			.phone(USER_PHONE_NUMBER)
			.birthdate(USER_BIRTHDATE)
			.email(USER_EMAIL)
			.password(USER_PASSWORD)
			.build();
	}
	
	private RoleData validRoleOwnerData() {
		return RoleData.builder()
			.id(ROLE_ID_OWNER)
			.name(USER_ROLE.name())
			.build();
	}
}
