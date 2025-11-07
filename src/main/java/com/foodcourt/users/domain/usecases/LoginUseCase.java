package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.InvalidLoginException;
import com.foodcourt.users.domain.gateways.EncryptServiceGateway;
import com.foodcourt.users.domain.gateways.TokenServiceGateway;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.ports.LoginPort;
import lombok.RequiredArgsConstructor;

import static com.foodcourt.users.domain.constants.ErrorMessage.INVALID_LOGIN_CREDENTIALS;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
public class LoginUseCase implements LoginPort {
	
	private final UserRepositoryGateway userRepositoryGateway;
	private final EncryptServiceGateway encryptServiceGateway;
	private final TokenServiceGateway tokenServiceGateway;
	
	@Override
	public String login(String email, String password) {
		User existingUser = sanitizeUser(email);
		if (!matchPassword(password, existingUser.getPassword()))
			throw new InvalidLoginException(INVALID_LOGIN_CREDENTIALS);
		return tokenServiceGateway.generateToken(existingUser);
	}
	
	private User sanitizeUser(String email) {
		User existingUser = userRepositoryGateway.findByEmail(email);
		if (isNull(existingUser)) throw new InvalidLoginException(INVALID_LOGIN_CREDENTIALS);
		return existingUser;
	}
	
	private boolean matchPassword(String password, String existingPassword) {
		return encryptServiceGateway.verify(password, existingPassword);
	}
	
}
