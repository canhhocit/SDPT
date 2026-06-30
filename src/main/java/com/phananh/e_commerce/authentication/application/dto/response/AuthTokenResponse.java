package com.phananh.e_commerce.authentication.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthTokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private Long expiresIn;
    private Long refreshExpiresIn;
}



