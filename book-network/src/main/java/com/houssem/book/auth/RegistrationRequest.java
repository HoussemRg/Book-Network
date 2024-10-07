package com.houssem.book.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RegistrationRequest {
    @NotEmpty(message = "First Name is mandatory")
    @NotBlank(message = "First Name is mandatory")
    private String firstname;
    @NotEmpty(message = "Last Name is mandatory")
    @NotBlank(message = "Last Name is mandatory")
    private String lastname;
    @Email(message = "Email is not formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email is mandatory")
    private  String email;
    @NotEmpty(message = "Password is mandatory")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8,message = "Password must have at least 8 caracters")
    private String password;
}
