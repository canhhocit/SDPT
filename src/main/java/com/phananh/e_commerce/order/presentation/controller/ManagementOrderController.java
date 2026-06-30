package com.phananh.e_commerce.order.presentation.controller;

import com.phananh.e_commerce.order.application.dto.response.order.*;
import com.phananh.e_commerce.order.application.service.OrderService;
import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.core.util.PageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/management")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Nhân viên - Đơn hàng", description = "API nhân viên quản lý đơn hàng")
public class ManagementOrderController {

    OrderService orderService;

    @Operation(summary = "Tìm kiếm đơn hàng", description = "Tìm kiếm và phân trang danh sách đơn hàng")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<StaffOrderResponse>>> getAllOrdersForStaff(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortType) {

        Pageable pageable = PageRequest.of(
                Math.max(PageUtils.getPageNumber(page), 1) - 1,
                PageUtils.getPageSize(size),
                Sort.by(Sort.Direction.fromString(PageUtils.getSortType(sortType)), PageUtils.getSortBy(sortBy)));

        Page<StaffOrderResponse> response = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ApiResponse.<Page<StaffOrderResponse>>builder()
                .result(response)
                .message("Get staff orders successfully")
                .build());
    }

    @Operation(summary = "Lấy chi tiết đơn hàng", description = "Lấy thông tin chi tiết của một đơn hàng theo ID")
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getOrderDetailForStaff(@PathVariable Long orderId) {
        OrderDetailResponse response = orderService.getOrderDetail(orderId);
        return ResponseEntity.ok(ApiResponse.<OrderDetailResponse>builder()
                .result(response)
                .message("Get staff order detail successfully")
                .build());
    }

    @Operation(summary = "Cập nhật trạng thái đơn hàng", description = "Cập nhật trạng thái xử lý của đơn hàng")
    @PatchMapping("{orderId}/{status}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @PathVariable String status) {
        orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }
}


