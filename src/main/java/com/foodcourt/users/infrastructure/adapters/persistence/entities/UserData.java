package com.foodcourt.users.infrastructure.adapters.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "foodcourt_user")
public class UserData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "lastname")
    private String lastname;
    
    @Column(name = "document_number")
    private String documentNumber;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "birthdate")
    private LocalDate birthdate;
    
    @Column(name = "email")
    private String email;
    
    @Column(name = "password")
    private String password;
    
	@ManyToOne
    @JoinColumn(name = "id_role")
    private RoleData role;
    
}
