package com.azki.auth.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LoginResponse {

    private String token;

    private Date expiresAt;

}
