package com.foodcourt.users.domain.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	private Long id;
	private String name;
	private String lastname;
	private String documentNumber;
	private String phoneNumber;
	private LocalDate birthdate;
	private String email;
	private String password;
	private UserRole role;
	
}
