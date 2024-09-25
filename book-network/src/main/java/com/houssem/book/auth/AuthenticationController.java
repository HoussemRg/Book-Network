package com.houssem.book.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentcication")
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    private ResponseEntity<?> register(
            @RequestBody @Valid RegistrationRequest registrationRequest
    ) throws MessagingException {
        authService.register(registrationRequest);
        return ResponseEntity.accepted().build();
    }

}
