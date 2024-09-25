package com.houssem.book.auth;

import com.houssem.book.email.EmailService;
import com.houssem.book.email.EmailTemplateName;
import com.houssem.book.role.RoleRepository;
import com.houssem.book.user.Token;
import com.houssem.book.user.TokenRepository;
import com.houssem.book.user.User;
import com.houssem.book.user.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;

    @Value("${application.mailing.frontend.activation-url}")
    String activationUrl;

    public void register(RegistrationRequest registrationRequest) throws MessagingException {
        var userRole=roleRepository.findByName("USER").orElseThrow(()-> new IllegalArgumentException("ROLE USER WAS NOT INITIALIZED"));
        var user= User.builder()
                .firstname(registrationRequest.getFirstname())
                .lastname(registrationRequest.getLastname())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);

    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken=generateAndSaveActivationToken(user);
        //send email

        emailService.sendMail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Account Activation"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        //generate a token
        String generatedToken=generateActivationCode(6);
        var token= Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String caracters="0123456789";
        StringBuilder codeBuilder=new StringBuilder();
        SecureRandom secureRandom=new SecureRandom();
        for(int i=0;i<length;i++){
            int randomIndex=secureRandom.nextInt(caracters.length());
            codeBuilder.append(caracters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }
}
