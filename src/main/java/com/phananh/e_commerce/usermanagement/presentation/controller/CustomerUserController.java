package com.phananh.e_commerce.usermanagement.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserChangePasswordRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserInfoUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Người dùng", description = "Các API quản lý thông tin tài khoản người dùng")
public class CustomerUserController {

    UserService userService;

    @GetMapping("/my-info")
    @Operation(summary = "Lấy thông tin cá nhân", description = "Lấy thông tin tài khoản của người dùng hiện tại")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getMyInfo() {
        ApiResponse<UserInfoResponse> apiResponse = ApiResponse.<UserInfoResponse>builder()
                .result(userService.getMyInfo())
                .message("Get user info successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PatchMapping("/update-info")
    @Operation(summary = "Cập nhật thông tin cá nhân", description = "Cập nhật thông tin hồ sơ cá nhân của người dùng")
    public ResponseEntity<?> updateMyInfo(@RequestBody @Valid UserInfoUpdateRequest userInfoUpdateRequest) {
        userService.updateMyInfo(userInfoUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Đổi mật khẩu", description = "Thay đổi mật khẩu tài khoản của người dùng")
    public ResponseEntity<?> changePassword(@RequestBody @Valid UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(userChangePasswordRequest);
        return ResponseEntity.noContent().build();
    }

}
