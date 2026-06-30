package com.phananh.e_commerce.authentication.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntrospectRequest {

    @NotBlank(message = "Token is required")
    private String token;
}



