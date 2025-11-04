package com.foodcourt.users.infrastructure.adapters.persistence;

import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import com.foodcourt.users.infrastructure.adapters.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.foodcourt.users.domain.constants.ErrorMessage.ROLE_NOT_FOUND;
import static java.util.Objects.isNull;

@Repository
@RequiredArgsConstructor
public class UserAdapter implements UserRepositoryGateway {
	
	private final UserJpaRepository userRepository;
	private final RoleJpaRepository roleRepository;
	
	@Override
	public User save(User user) {
		UserData userData = mapUserToData(user);
		UserData savedUserData = userRepository.save(userData);
		return mapDataToUser(savedUserData);
	}
	
	@Override
	public User findByDocumentNumber(String documentNumber) {
		return mapDataToUser(userRepository.findByDocumentNumber(documentNumber));
	}
	
	@Override
	public User findByEmail(String email) {
		return mapDataToUser(userRepository.findByEmail(email));
	}
	
	private UserData mapUserToData(User user) {
		UserData userData = UserMapper.INSTANCE.toData(user);
		RoleData roleData = roleRepository.findByName(user.getRole().name());
		
		if (isNull(roleData)) throw new TechnicalException(ROLE_NOT_FOUND);
		
		userData.setRole(roleData);
		return userData;
	}
	
	private User mapDataToUser(UserData userData) {
		if (isNull(userData)) return null;
		User user = UserMapper.INSTANCE.toDomain(userData);
		user.setRole(UserRole.getRoleof(userData.getRole().getName()));
		return user;
	}
}
