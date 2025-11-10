package com.foodcourt.users.infrastructure.adapters.persistence;

import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.model.User;
import com.foodcourt.users.domain.model.UserRole;
import com.foodcourt.users.infrastructure.adapters.persistence.entities.RoleData;
import com.foodcourt.users.infrastructure.adapters.persistence.entities.UserData;
import com.foodcourt.users.infrastructure.adapters.persistence.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.foodcourt.users.domain.constants.ErrorMessage.ROLE_NOT_FOUND;
import static java.util.Objects.isNull;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryGateway {
	
	private final UserJpaRepository userRepository;
	private final RoleJpaRepository roleRepository;
	
	@Override
	public User save(User user) {
		log.trace("Saving user with email: {}", user.getEmail());
		UserData userData = mapUserToData(user);
		UserData savedUserData = userRepository.save(userData);
		log.debug("Saved user with ID: {}", savedUserData.getId());
		return mapDataToUser(savedUserData);
	}
	
	@Override
	public User findByDocumentNumber(String documentNumber) {
		log.trace("Finding user by document number: {}", documentNumber);
		return mapDataToUser(userRepository.findByDocumentNumber(documentNumber));
	}
	
	@Override
	public User findByEmail(String email) {
		log.trace("Finding user by email: {}", email);
		return mapDataToUser(userRepository.findByEmail(email));
	}
	
	@Override
	public User findById(Long id) {
		log.trace("Finding user by ID: {}", id);
		return userRepository.findById(id).map(this::mapDataToUser).orElse(null);
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
