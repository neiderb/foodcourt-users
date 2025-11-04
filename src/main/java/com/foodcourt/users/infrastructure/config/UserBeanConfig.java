package com.foodcourt.users.infrastructure.config;

import com.foodcourt.users.domain.gateways.EncryptService;
import com.foodcourt.users.domain.gateways.UserRepositoryGateway;
import com.foodcourt.users.domain.ports.CreateUserPort;
import com.foodcourt.users.domain.usecases.CreateUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserBeanConfig {
	
	@Bean
	public CreateUserPort createUserPort(UserRepositoryGateway userRepositoryGateway, EncryptService encryptService) {
		return new CreateUserUseCase(
			userRepositoryGateway,
			encryptService
		);
	}
	
}
