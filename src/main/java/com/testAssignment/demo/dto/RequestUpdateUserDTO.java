package com.testAssignment.demo.dto;

import com.testAssignment.demo.model.validator.Address;
import com.testAssignment.demo.model.validator.Email;
import com.testAssignment.demo.model.validator.PhoneNumber;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder
@Getter
@Setter
public class RequestUpdateUserDTO {

    private String firstName;
    private String lastName;
    private Email email;
    private LocalDate birthDate;
    private Address address;
    private PhoneNumber phoneNumber;

    @Override
    public String toString() {
        return "RequestUpdateUserDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email +
                ", birthDate=" + birthDate +
                ", address=" + address +
                ", phoneNumber=" + phoneNumber +
                '}';
    }
}