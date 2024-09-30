package com.houssem.book.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @Email(message = "Email is not formatted")
    @NotEmpty(message = "Email is mandatory")
    @NotBlank(message = "Email Name is mandatory")
    private  String email;
    @NotEmpty(message = "Password Name is mandatory")
    @NotBlank(message = "Password Name is mandatory")
    @Size(min = 8,message = "Password must have at least 8 caracters")
    private String password;

}
