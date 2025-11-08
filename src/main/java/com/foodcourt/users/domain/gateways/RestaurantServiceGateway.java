package com.foodcourt.users.domain.gateways;

public interface RestaurantServiceGateway {
	
	boolean isRestaurantOwner(Long idRestaurant, Long idUser);
	
}
