package com.testAssignment.demo.model;

import com.testAssignment.demo.model.validator.Address;
import com.testAssignment.demo.model.validator.Email;
import com.testAssignment.demo.model.validator.PhoneNumber;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "Users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Embedded
    @Column(name = "email", nullable = false)
    private Email email;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Past
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Embedded
    @Column(name = "address")
    private Address address;

    @Embedded
    @Column(name = "phone_number")
    private PhoneNumber phoneNumber;
}