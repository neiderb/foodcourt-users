package com.foodcourt.users.infrastructure.adapters.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleData, Long> {
	
	RoleData findByName(String name);
	
}
