package com.foodcourt.users.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserData, Long> {
	
	UserData findByDocumentNumber(String documentNumber);
	
	UserData findByEmail(String email);
	
}
