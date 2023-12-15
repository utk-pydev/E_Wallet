package org.example.wallet.wallet;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

public class UserRequest {

    @NotBlank
    @Size(max=20)
    private String name;
    @Size(min = 10, max=10)
    private String phoneNumber;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "New password is mandatory")
    private String password;


    private String Country;
    private String dob;

    @NotBlank
    private String authorities;
    private String identifierValue;

    public User to(){
        return User.builder()
                .name(name)
                .email(email)
                .password(password)
                .dob(dob)
                .Country(Country)
                .phoneNumber(phoneNumber)
                .identifierValue(identifierValue)
                .authorities(authorities)
                .build();
    }

}
