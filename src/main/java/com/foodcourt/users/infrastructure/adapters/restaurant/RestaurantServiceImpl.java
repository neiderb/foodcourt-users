package com.foodcourt.users.infrastructure.adapters.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.users.domain.exception.BusinessException;
import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.users.domain.gateways.RestaurantServiceGateway;
import com.foodcourt.users.infrastructure.adapters.restaurant.dto.ErrorExternalResponse;
import com.foodcourt.users.infrastructure.adapters.restaurant.dto.RestaurantExternalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.function.Consumer;

import static com.foodcourt.users.domain.constants.RestaurantErrorMessage.RESTAURANT_NOT_FOUND;
import static com.foodcourt.users.infrastructure.adapters.restaurant.constants.ErrorMessage.EXTERNAL_SERVICE_ERROR;
import static com.foodcourt.users.infrastructure.adapters.restaurant.constants.ErrorMessage.UNMAPPING_RESPONSE;
import static java.util.Objects.isNull;

@Slf4j
@Service
public class RestaurantServiceImpl implements RestaurantServiceGateway {
	
	private final RestClient restClient;
	private final ObjectMapper objectMapper;
	
	public RestaurantServiceImpl(
		@Qualifier("restaurantServiceClient")
		RestClient restClient,
		ObjectMapper objectMapper
	) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}
	
	@Override
	public boolean isRestaurantOwner(Long idRestaurant, Long idUser) {
		log.trace("Checking if user with ID {} is owner of restaurant with ID {}", idUser, idRestaurant);
		RestaurantExternalResponse restaurant = getRestaurantById(idRestaurant);
		if (isNull(restaurant)) throw new RestaurantNotFoundException(RESTAURANT_NOT_FOUND);
		return restaurant.ownerId().equals(idUser);
	}
	
	private RestaurantExternalResponse getRestaurantById(Long idRestaurant) {
		return restClient.get()
			.uri("/api/v1/restaurant/{id}", idRestaurant)
			.headers(buildHeaders())
			.retrieve()
			.onStatus(
				HttpStatusCode::is4xxClientError, (req, res) -> {
					log.warn("Received 4xx error when fetching restaurant with ID {}: {}", idRestaurant, res.getStatusCode());
					ErrorExternalResponse error = mapErrorResponse(res);
					log.warn("Mapped error response: {}", error);
					throw new BusinessException(error.message());
				})
			.onStatus(HttpStatusCode::isError, (req, res) -> {
				log.error("Received error response when fetching restaurant with ID {}: {}", idRestaurant, res.getStatusCode());
				throw new TechnicalException(EXTERNAL_SERVICE_ERROR);
			})
			.body(RestaurantExternalResponse.class);
	}
	
	private Consumer<HttpHeaders> buildHeaders() {
		return headers -> {
			String token = getToken();
			if (token != null) {
				headers.set(HttpHeaders.AUTHORIZATION, token);
			}
		};
	}
	
	private String getToken() {
		String token = null;
		Object credentials = SecurityContextHolder.getContext().getAuthentication().getCredentials();
		if (credentials instanceof String strToken) {
			token = "Bearer ".concat(strToken);
		}
		
		return token;
	}
	
	private ErrorExternalResponse mapErrorResponse(ClientHttpResponse response) {
		try {
			return objectMapper.readValue(
				response.getBody(),
				ErrorExternalResponse.class
			);
		} catch (Exception e) {
			log.error("Error mapping error response", e);
			throw new TechnicalException(UNMAPPING_RESPONSE);
		}
	}
	
}
