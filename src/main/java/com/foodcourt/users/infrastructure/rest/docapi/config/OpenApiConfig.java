package com.foodcourt.users.infrastructure.rest.docapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	
	private static final String TITLE = "Food Court - Users Service API";
	private static final String DESCRIPTION = "This API manages users within the Food Court application.";
	private static final String VERSION = "1.0.0";
	
	@Bean
	public OpenAPI openAPIConfig() {
		return new OpenAPI()
			.components(new Components())
			.info(new Info()
				.title(TITLE)
				.description(DESCRIPTION)
				.version(VERSION)
			);
	}
	
}
