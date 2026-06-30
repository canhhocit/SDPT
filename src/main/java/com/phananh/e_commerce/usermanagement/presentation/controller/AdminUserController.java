package com.phananh.e_commerce.usermanagement.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserResponse;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.AdminUserQueryRequest;
import com.phananh.e_commerce.usermanagement.presentation.dto.request.UserRoleUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Quản trị - Người dùng", description = "API quản trị dành cho quản lý người dùng")
public class AdminUserController {

    UserService userService;

    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "Tìm kiếm và phân trang danh sách người dùng")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(@ModelAttribute @Valid AdminUserQueryRequest adminUserQueryRequest) {
        Page<UserResponse> users = userService.getAllUsers(adminUserQueryRequest);
        ApiResponse<Page<UserResponse>> apiResponse = ApiResponse.<Page<UserResponse>>builder()
                .result(users)
                .message("Get all users successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Lấy thông tin người dùng", description = "Lấy thông tin chi tiết của một người dùng theo ID")
    @GetMapping("/info/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@PathVariable Long id) {
        ApiResponse<UserInfoResponse> apiResponse = ApiResponse.<UserInfoResponse>builder()
                .result(userService.getUserInfo(id))
                .message("Get user info successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @Operation(summary = "Cập nhật vai trò người dùng", description = "Cập nhật thay đổi vai trò của người dùng")
    @PatchMapping("/update-role")
    public ResponseEntity<?> updateUserRole(@RequestBody @Valid UserRoleUpdateRequest userRoleUpdateRequest) {
        userService.updateUserRole(userRoleUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật trạng thái người dùng", description = "Kích hoạt hoặc vô hiệu hóa tài khoản người dùng")
    @PatchMapping("/{id}/{status}")
    public ResponseEntity<?> updateUserStatus(@PathVariable("id") Long userId,
                                              @PathVariable String status) {
        userService.updateUserStatus(userId, status);
        return ResponseEntity.noContent().build();
    }

}

