package com.foodcourt.users.infrastructure.adapters;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserData, Long> {
}
