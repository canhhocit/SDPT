package com.phananh.e_commerce.usermanagement.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserStatusUpdateRequest {
    @NotNull(message = "User id is required")
    private Long userId;

    @NotNull(message = "User status is required")
    private Boolean status;
}
