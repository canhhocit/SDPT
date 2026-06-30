package com.phananh.e_commerce.authentication.application.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IntrospectResponse {
    private boolean active;
    private String username;
    private String tokenType;
    private Long expiresAt;
}



