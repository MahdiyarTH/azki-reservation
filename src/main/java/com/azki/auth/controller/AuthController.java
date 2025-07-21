package com.azki.auth.controller;

import com.azki.auth.model.dto.LoginRequest;
import com.azki.auth.model.dto.LoginResponse;
import com.azki.auth.model.dto.RegisterRequest;
import com.azki.auth.service.AuthService;
import com.azki.common.model.HttpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication operation APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Login user by passed credentials"
    )
    public HttpResponse<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return HttpResponse.success(authService.login(loginRequest));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            description = "Register new user with specified information"
    )
    public HttpResponse<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return HttpResponse.success();
    }

}
