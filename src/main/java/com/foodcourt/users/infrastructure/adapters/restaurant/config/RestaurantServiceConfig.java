package com.foodcourt.users.infrastructure.adapters.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class RestaurantServiceConfig {
	
	@Value("${external-api.restaurant-service.url}")
	private String restaurantServiceUrl;
	
	@Bean
	public RestClient restaurantServiceClient(RestClient.Builder builder) {
		return builder
			.baseUrl(restaurantServiceUrl)
			.defaultHeader(
				HttpHeaders.CONTENT_TYPE,
				MediaType.APPLICATION_JSON_VALUE
			)
			.build();
	}
	
}
