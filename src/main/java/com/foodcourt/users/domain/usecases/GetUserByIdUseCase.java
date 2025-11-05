package com.foodcourt.users.domain.usecases;

import com.foodcourt.users.domain.exception.UserNotFoundException;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.ports.GetUserByIdPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.foodcourt.users.domain.constants.ErrorMessage.USER_NOT_FOUND;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase implements GetUserByIdPort {
	
	private final UserRepositoryGateway userRepositoryGateway;
	
	@Override
	public User execute(Long id) {
		User user = userRepositoryGateway.findById(id);
		if (isNull(user)) throw new UserNotFoundException(USER_NOT_FOUND);
		return user;
	}
	
}
