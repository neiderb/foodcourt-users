package com.foodcourt.users.infrastructure.config;

import com.foodcourt.users.domain.gateways.EncryptServiceGateway;
import com.foodcourt.users.domain.gateways.RestaurantServiceGateway;
import com.foodcourt.users.domain.gateways.TokenServiceGateway;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.ports.CreateUserPort;
import com.foodcourt.users.domain.ports.GetUserByIdPort;
import com.foodcourt.users.domain.ports.LoginPort;
import com.foodcourt.users.domain.usecases.CreateUserUseCase;
import com.foodcourt.users.domain.usecases.GetUserByIdUseCase;
import com.foodcourt.users.domain.usecases.LoginUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserBeanConfig {
	
	@Bean
	public CreateUserPort createUserPort(
		UserRepositoryGateway userRepositoryGateway,
		EncryptServiceGateway encryptServiceGateway,
		RestaurantServiceGateway restaurantServiceGateway
	) {
		return new CreateUserUseCase(
			userRepositoryGateway,
			encryptServiceGateway,
			restaurantServiceGateway
		);
	}
	
	@Bean
	public GetUserByIdPort getUserByIdPort(UserRepositoryGateway userRepositoryGateway) {
		return new GetUserByIdUseCase(userRepositoryGateway);
	}
	
	@Bean
	public LoginPort loginPort(
		UserRepositoryGateway userRepositoryGateway,
		EncryptServiceGateway encryptServiceGateway,
		TokenServiceGateway tokenServiceGateway
	) {
		return new LoginUseCase(
			userRepositoryGateway,
			encryptServiceGateway,
			tokenServiceGateway
		);
	}
	
}
