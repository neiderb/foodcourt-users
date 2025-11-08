package com.foodcourt.users.infrastructure.adapters.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodcourt.users.domain.exception.BusinessException;
import com.foodcourt.users.domain.exception.TechnicalException;
import com.foodcourt.users.domain.exception.restaurant.RestaurantNotFoundException;
import com.foodcourt.users.infrastructure.adapters.restaurant.dto.ErrorExternalResponse;
import com.foodcourt.users.infrastructure.adapters.restaurant.dto.RestaurantExternalResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings(
	{
		"rawtypes",
		"unchecked",
		"resource"
	}
)
@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {
	
	@InjectMocks
	private RestaurantServiceImpl restaurantServiceImpl;
	
	@Mock
	private RestClient restClient;
	
	@Mock
	private ObjectMapper objectMapper;
	
	@Mock
	private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
	
	@Mock
	private RestClient.RequestHeadersSpec requestHeadersSpec;
	
	@Mock
	private RestClient.ResponseSpec responseSpec;
	
	@BeforeEach
	void setUpSecurityContext() {
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
			"test-user",
			"dummy-token",
			Collections.emptyList()
		);
		SecurityContextHolder.getContext().setAuthentication(auth);
		
		when(requestHeadersSpec.headers(any())).thenReturn(requestHeadersSpec);
	}
	
	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}
	
	@Test
	void shouldFindRestaurantOwnerSuccessfully() {
		Long idOwner = 1L;
		Long idRestaurant = 99L;
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/restaurant/{id}", idRestaurant)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(RestaurantExternalResponse.class))
			.thenReturn(validRestaurantResponse(idOwner));
		
		boolean isOwner = restaurantServiceImpl.isRestaurantOwner(idRestaurant, idOwner);
		
		assertTrue(isOwner);
	}
	
	@Test
	void shouldReturnFalseWhenUserIsNotRestaurantOwner() {
		Long idOwner = 1L;
		Long idRestaurant = 99L;
		Long idUser = 2L;
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/restaurant/{id}", idRestaurant)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(RestaurantExternalResponse.class))
			.thenReturn(validRestaurantResponse(idOwner));
		
		boolean isOwner = restaurantServiceImpl.isRestaurantOwner(idRestaurant, idUser);
		
		assertFalse(isOwner);
	}
	
	@Test
	void shouldThrowExceptionWhenRestaurantNotFound() {
		Long idRestaurant = 99L;
		Long idUser = 1L;
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/restaurant/{id}", idRestaurant)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(RestaurantExternalResponse.class))
			.thenReturn(null);
		
		assertThrows(RestaurantNotFoundException.class, () -> restaurantServiceImpl.isRestaurantOwner(idRestaurant, idUser));
	}
	
	@Test
	void shouldThrowExceptionOnMappingErrorResponse() throws Exception {
		String errorBodyJson = """
				{
				"prop": "someThing",
				"otherProp": "any"
				}
			""";
		Long idRestaurant = 1L;
		Long idOwner = 10L;
		ClientHttpResponse clientHttpResponse = mock(ClientHttpResponse.class);
		
		when(clientHttpResponse.getBody()).thenReturn(new ByteArrayInputStream(errorBodyJson.getBytes()));
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/restaurant/{id}", idRestaurant)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		
		when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
			RestClient.ResponseSpec.ErrorHandler handler = invocation.getArgument(1);
			handler.handle(mock(ClientHttpRequest.class), clientHttpResponse);
			return responseSpec;
		});
		
		when(objectMapper.readValue(any(InputStream.class), eq(ErrorExternalResponse.class)))
			.thenThrow(new RuntimeException("Mapping error"));
		
		assertThrows(TechnicalException.class, () -> restaurantServiceImpl.isRestaurantOwner(idRestaurant, idOwner));
	}
	
	@Test
	void shouldAddAuthorizationHeaderWhenTokenPresent() {
		final Long idRestaurant = 1L;
		final Long idUser = 10L;
		RestaurantExternalResponse externalResponse = validRestaurantResponse(idUser);
		
		AtomicReference<Consumer<HttpHeaders>> headersConsumer = new AtomicReference<>();
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/restaurant/{id}", idRestaurant)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.headers(any())).thenAnswer(invocation -> {
			headersConsumer.set(invocation.getArgument(0));
			return requestHeadersSpec;
		});
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
		when(responseSpec.body(RestaurantExternalResponse.class)).thenReturn(externalResponse);
		
		restaurantServiceImpl.isRestaurantOwner(idRestaurant, idUser);
		
		HttpHeaders headers = new HttpHeaders();
		Consumer<HttpHeaders> consumer = headersConsumer.get();
		assertNotNull(consumer);
		consumer.accept(headers);
		assertEquals("Bearer dummy-token", headers.getFirst(HttpHeaders.AUTHORIZATION));
	}
	
	@Test
	void shouldThrowExceptionOn4xxError() throws Exception {
		final Long idUser = 10L;
		final Long idRestaurant = 1L;
		final int code = 404;
		final String errorMessage = "Restaurant not found";
		String errorBodyJson = String.format("""
				{
					"code": %d,
					"message": "%s"
				}
			""", code, errorMessage);
		ClientHttpResponse clientHttpResponse = mock(ClientHttpResponse.class);
		ErrorExternalResponse errorResponse = new ErrorExternalResponse(code, errorMessage);
		
		when(clientHttpResponse.getBody()).thenReturn(new ByteArrayInputStream(errorBodyJson.getBytes()));
		when(objectMapper.readValue(any(InputStream.class), eq(ErrorExternalResponse.class))).thenReturn(errorResponse);
		
		when(restClient.get()).thenReturn(requestHeadersUriSpec);
		when(requestHeadersUriSpec.uri("/api/v1/restaurant/{id}", idRestaurant)).thenReturn(requestHeadersSpec);
		when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
		
		when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
			RestClient.ResponseSpec.ErrorHandler handler = invocation.getArgument(1);
			handler.handle(mock(ClientHttpRequest.class), clientHttpResponse);
			return responseSpec;
		});
		
		assertThrows(BusinessException.class, () -> restaurantServiceImpl.isRestaurantOwner(idRestaurant, idUser));
	}
	
	private RestaurantExternalResponse validRestaurantResponse(Long idOwner) {
		return new RestaurantExternalResponse(
			99L,
			"Test Restaurant",
			"5555666666",
			"123 Test St",
			"555-1234",
			"http://testrestaurant.com",
			idOwner
		);
	}
	
}
