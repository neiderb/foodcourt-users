package com.foodcourt.users.domain.gateways;

public interface EncryptServiceGateway {
	
	String encrypt(String value);
	
	Boolean verify(String value, String encryptedValue);
	
}
