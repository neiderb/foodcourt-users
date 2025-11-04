package com.foodcourt.users.infrastructure.adapters.passwordencoder;

import com.foodcourt.users.domain.gateways.EncryptService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordEncoderServiceImpl implements EncryptService {
	
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public String encrypt(String value) {
		return passwordEncoder.encode(value);
	}
	
	@Override
	public Boolean verify(String value, String encryptedValue) {
		return passwordEncoder.matches(value, encryptedValue);
	}
}
