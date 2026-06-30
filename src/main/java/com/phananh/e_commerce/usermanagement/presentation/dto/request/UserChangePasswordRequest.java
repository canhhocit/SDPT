package com.phananh.e_commerce.usermanagement.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserChangePasswordRequest {

    @NotBlank(message = "Old Password is required")
    private String oldPassword;

    @NotBlank(message = "New Password is required")
    private String newPassword;
}


