package com.phananh.e_commerce.usermanagement.presentation.dto.request;

import com.phananh.e_commerce.usermanagement.domain.model.enums.RoleName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
public class UserRoleUpdateRequest {

    @NotNull(message = "User id is required")
    private Long userId;

    @NotEmpty(message = "Roles must not be empty")
    private Set<RoleName> roleNames;
}



