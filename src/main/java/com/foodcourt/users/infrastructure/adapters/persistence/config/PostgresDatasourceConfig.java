package com.foodcourt.users.infrastructure.adapters.persistence.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class PostgresDatasourceConfig {
	
	private final DatabaseConnectionProperties properties;
	
	private static final String URL_TEMPLATE = "jdbc:postgresql://%s:%d/%s";
	private static final String DRIVER_CLASS_NAME = "org.postgresql.Driver";
	private static final int POOL_SIZE = 10;
	
	@Bean
	public DataSource dataSource() {
		String url = String.format(
			URL_TEMPLATE,
			properties.host(),
			properties.port(),
			properties.database()
		);
		
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(properties.username());
		config.setPassword(properties.password());
		config.setDriverClassName(DRIVER_CLASS_NAME);
		config.setMaximumPoolSize(POOL_SIZE);
		
		return new HikariDataSource(config);
	}
	
}
