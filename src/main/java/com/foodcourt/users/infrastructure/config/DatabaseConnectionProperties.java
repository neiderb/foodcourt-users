package com.foodcourt.users.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.db")
public record DatabaseConnectionProperties(
	String host,
	Integer port,
	String username,
	String password,
	String database
) {
}
