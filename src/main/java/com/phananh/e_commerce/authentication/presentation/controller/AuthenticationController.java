package com.phananh.e_commerce.authentication.presentation.controller;

import com.phananh.e_commerce.authentication.presentation.dto.request.AuthenticationRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.IntrospectRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.LogoutRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RefreshTokenRequest;
import com.phananh.e_commerce.authentication.presentation.dto.request.RegisterRequest;
import com.phananh.e_commerce.authentication.application.dto.response.AuthTokenResponse;
import com.phananh.e_commerce.authentication.application.dto.response.IntrospectResponse;
import com.phananh.e_commerce.authentication.application.dto.response.LogoutResponse;
import com.phananh.e_commerce.authentication.application.service.AuthenticationService;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Xác thực", description = "Đăng nhập, đăng ký và quản lý phiên làm việc")
public class AuthenticationController {

    AuthenticationService authenticationService;

    @Operation(summary = "Đăng nhập")
    @PostMapping("/login")
    public ApiResponse<AuthTokenResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ApiResponse.<AuthTokenResponse>builder()
                .message("Login successful")
                .result(authenticationService.login(request))
                .build();
    }

    @Operation(summary = "Đăng ký tài khoản mới")
    @PostMapping("/register")
    public ApiResponse<AuthTokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ApiResponse.<AuthTokenResponse>builder()
                .message("Register successful")
                .result(authenticationService.register(request))
                .build();
    }

    @Operation(summary = "Làm mới token")
    @PostMapping("/refresh")
    public ApiResponse<AuthTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ApiResponse.<AuthTokenResponse>builder()
                .message("Refresh token successful")
                .result(authenticationService.refreshToken(request))
                .build();
    }

    @Operation(summary = "Đăng xuất")
    @PostMapping("/logout")
    public ApiResponse<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {
        return ApiResponse.<LogoutResponse>builder()
                .message("Logout successful")
                .result(authenticationService.logout(request))
                .build();
    }

    @Operation(summary = "Kiểm tra token")
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@Valid @RequestBody IntrospectRequest request) {
        return ApiResponse.<IntrospectResponse>builder()
                .message("Introspect token successful")
                .result(authenticationService.introspect(request))
                .build();
    }
}
