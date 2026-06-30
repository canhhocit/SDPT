package com.phananh.e_commerce.usermanagement.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.usermanagement.application.dto.response.UserInfoResponse;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("staff/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Nhân viên - Người dùng", description = "API dành cho nhân viên quản lý thông tin khách hàng")
public class StaffUserController {

    UserService userService;

    @Operation(summary = "Lấy thông tin khách hàng", description = "Lấy thông tin chi tiết của một khách hàng theo ID")
    @GetMapping("/customer/info/{id}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getCustomerInfo(@PathVariable Long id) {
        ApiResponse<UserInfoResponse> apiResponse = ApiResponse.<UserInfoResponse>builder()
                .result(userService.getCustomerInfo(id))
                .message("Get customer info successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}

