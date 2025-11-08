package com.foodcourt.users.domain.model;

import com.foodcourt.users.domain.exception.InvalidUserException;
import lombok.*;

import java.time.LocalDate;

import static com.foodcourt.users.domain.constants.ValidationMessage.RESTAURANT_ID_REQUIRED_FOR_EMPLOYEE;
import static java.util.Objects.isNull;

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
	private Long idRestaurant;
	
	public boolean isEmployee() {
		return this.role == UserRole.EMPLOYEE;
	}
	
	public void validateRestaurantAssociation() {
		if (this.isEmployee() && isNull(this.idRestaurant)) {
			throw new InvalidUserException(RESTAURANT_ID_REQUIRED_FOR_EMPLOYEE);
		}
	}
	
}
