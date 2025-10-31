package com.foodcourt.users.infrastructure.adapters;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleJpaRepository extends JpaRepository<RoleData, Long> {
	
	RoleData findByName(String name);
	
}
