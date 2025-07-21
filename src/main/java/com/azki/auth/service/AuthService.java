package com.azki.auth.service;

import com.azki.auth.model.dto.LoginRequest;
import com.azki.auth.model.dto.LoginResponse;
import com.azki.auth.model.dto.RegisterRequest;
import com.azki.common.exception.model.ApiException;
import com.azki.common.util.JwtUtil;
import com.azki.user.entity.UserEntity;
import com.azki.user.model.crud.CreateUserRequest;
import com.azki.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse login(LoginRequest loginRequest) {
        UserEntity user = userService.findByUsername(loginRequest.getUsername())
                .filter(it -> passwordEncoder.matches(loginRequest.getPassword(), it.getPassword()))
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        final String jwtToken = jwtUtil.generateToken(user.getId());
        return LoginResponse.builder()
                .token(jwtToken)
                .expiresAt(jwtUtil.getExpiration(jwtToken))
                .build();
    }

    @Transactional
    public void register(RegisterRequest registerRequest) {
        if (userService.existsByEmail(registerRequest.getEmail()))
            throw ApiException.builder()
                    .message("Email already in use!")
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();

        if (userService.existsByUsername(registerRequest.getUsername()))
            throw ApiException.builder()
                    .message("Username already in use!")
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();

        userService.createUser(
                CreateUserRequest.builder()
                        .email(registerRequest.getEmail())
                        .username(registerRequest.getUsername())
                        .password(registerRequest.getPassword())
                        .build()
        );
    }

}
