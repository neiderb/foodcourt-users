package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.InvalidRoleException;
import com.foodcourt.users.domain.exception.InvalidUserException;
import com.foodcourt.users.domain.gateways.EncryptServiceGateway;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import com.foodcourt.users.domain.ports.CreateUserPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.foodcourt.users.domain.constants.ErrorMessage.*;
import static com.foodcourt.users.domain.constants.UserRules.LEGAL_AGE;
import static com.foodcourt.users.domain.constants.ValidationMessage.USER_MUST_BE_OF_LEGAL_AGE;
import static com.foodcourt.users.domain.model.UserRole.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserPort {
	
	private final UserRepositoryGateway userRepositoryGateway;
	private final EncryptServiceGateway encryptServiceGateway;
	
	@Override
	public User execute(User userToCreate, UserRole roleCreator) {
		validateAuthorization(userToCreate, roleCreator);
		validateAge(userToCreate.getBirthdate());
		validateUniqueDocumentNumber(userToCreate.getDocumentNumber());
		validateUniqueEmail(userToCreate.getEmail());
		
		userToCreate.setPassword(encryptPassword(userToCreate.getPassword()));
		
		return userRepositoryGateway.save(userToCreate);
	}
	
	private void validateAuthorization(User userToCreate, UserRole roleCreator) {
		UserRole newUserRole = userToCreate.getRole();
		
		boolean isForbiddenAction =
			// Clients can only create themselves
			(isNull(roleCreator) && !CLIENT.equals(newUserRole))
			|| (CLIENT.equals(roleCreator))
			|| (CLIENT.equals(newUserRole))
			
			// Admin can only create Owners
			|| (ADMIN.equals(roleCreator) && !OWNER.equals(newUserRole))
			
			// Owner can only create Employees
			|| (OWNER.equals(roleCreator) && !EMPLOYEE.equals(newUserRole))
			
			// Employees cannot create users
			|| (EMPLOYEE.equals(roleCreator))
			
			// No one can create Admins
			|| (ADMIN.equals(newUserRole));
		
		if (isForbiddenAction) {
			throw new InvalidRoleException(UNAUTHORIZED_ACTION);
		}
	}
	
	private void validateUniqueDocumentNumber(String documentNumber) {
		User existingUser = userRepositoryGateway.findByDocumentNumber(documentNumber);
		if (nonNull(existingUser)) throw new InvalidUserException(USER_WITH_THIS_DOCUMENT_NUMBER_ALREADY_EXISTS);
	}
	
	private void validateUniqueEmail(String email) {
		User existingUser = userRepositoryGateway.findByEmail(email);
		if (nonNull(existingUser)) throw new InvalidUserException(USER_WITH_THIS_EMAIL_ALREADY_EXISTS);
	}
	
	private void validateAge(LocalDate birthDate) {
		int age = Math.toIntExact(birthDate.until(
			LocalDate.now(),
			ChronoUnit.YEARS
		));
		if (age < LEGAL_AGE) throw new InvalidUserException(USER_MUST_BE_OF_LEGAL_AGE);
	}
	
	private String encryptPassword(String password) {
		return encryptServiceGateway.encrypt(password);
	}
	
}
