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
public class Address {

    @NotEmpty
    @Pattern(regexp = "^St\\.\\s[A-Z][a-zA-Z0-9\\s]*$",
             message = "Street address must include 'St. <address>'")
    @Size(min = 6)
    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address otherAddress = (Address) o;
        return Objects.equals(address, otherAddress.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }
}
