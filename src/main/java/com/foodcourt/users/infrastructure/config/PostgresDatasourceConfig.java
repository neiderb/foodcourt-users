package com.foodcourt.users.infrastructure.config;

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
	
	private static final String urlTemplate = "jdbc:postgresql://%s:%d/%s";
	private static final String driverClassName = "org.postgresql.Driver";
	private static final int poolSize = 10;
	
	@Bean
	public DataSource dataSource() {
		String url = String.format(
			urlTemplate,
			properties.host(),
			properties.port(),
			properties.database()
		);
		
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(url);
		config.setUsername(properties.username());
		config.setPassword(properties.password());
		config.setDriverClassName(driverClassName);
		config.setMaximumPoolSize(poolSize);
		
		return new HikariDataSource(config);
	}
	
}
