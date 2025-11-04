package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.InvalidUserException;
import com.foodcourt.users.domain.gateways.EncryptService;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.ports.CreateUserPort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static com.foodcourt.users.domain.constants.ErrorMessage.USER_WITH_THIS_DOCUMENT_NUMBER_ALREADY_EXISTS;
import static com.foodcourt.users.domain.constants.ErrorMessage.USER_WITH_THIS_EMAIL_ALREADY_EXISTS;
import static com.foodcourt.users.domain.constants.UserConstants.LEGAL_AGE;
import static com.foodcourt.users.domain.constants.ValidationMessage.USER_MUST_BE_OF_LEGAL_AGE;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserPort {
	
	private final UserRepositoryGateway userRepositoryGateway;
	private final EncryptService encryptService;
	
	@Override
	public User execute(User user) {
		validateAge(user.getBirthdate());
		validateUniqueDocumentNumber(user.getDocumentNumber());
		validateUniqueEmail(user.getEmail());
		
		user.setPassword(encryptPassword(user.getPassword()));
		
		return userRepositoryGateway.save(user);
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
		return encryptService.encrypt(password);
	}
	
}
