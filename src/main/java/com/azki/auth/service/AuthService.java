package com.azki.auth.service;

import com.azki.auth.model.dto.LoginRequest;
import com.azki.auth.model.dto.LoginResponse;
import com.azki.common.util.JwtUtil;
import com.azki.user.entity.UserEntity;
import com.azki.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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

}
