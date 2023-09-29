package com.testAssignment.demo.model.validator;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Embeddable
public class Email {

    @NotEmpty(message = "Email must not be empty")
    @jakarta.validation.constraints.Email(message = "Email should be valid")
    @Pattern(regexp = "[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+@gmail\\.com")
    @Size(min = 5)
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email otherEmail = (Email) o;
        return Objects.equals(email, otherEmail.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
