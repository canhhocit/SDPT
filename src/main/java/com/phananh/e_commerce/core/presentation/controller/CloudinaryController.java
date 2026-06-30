package com.phananh.e_commerce.core.presentation.controller;

import com.phananh.e_commerce.core.infrastructure.service.CloudinaryService;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cloudinary")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Cloudinary", description = "API for generating upload signatures đi kèm với tải file trực tiếp")
public class CloudinaryController {

    CloudinaryService cloudinaryService;

    @Operation(summary = "Lấy chữ ký tải file lên", description = "Tạo chữ ký để tải file trực tiếp lên Cloudinary từ phía client")
    @GetMapping("/signature")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUploadSignature(@RequestParam String folder) {
        Map<String, Object> signature = cloudinaryService.generateUploadSignature(folder);
        ApiResponse<Map<String, Object>> response = ApiResponse.<Map<String, Object>>builder()
                .message("Cloudinary upload signature generated")
                .result(signature)
                .build();
        return ResponseEntity.ok(response);
    }
}

