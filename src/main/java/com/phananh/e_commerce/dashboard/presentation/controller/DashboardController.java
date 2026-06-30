package com.phananh.e_commerce.dashboard.presentation.controller;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import com.phananh.e_commerce.dashboard.presentation.dto.request.DashboardOrderStatisticRequest;
import com.phananh.e_commerce.dashboard.presentation.dto.request.DashboardRevenueReportRequest;
import com.phananh.e_commerce.dashboard.application.dto.response.DashboardResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.SalesStatsResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.StatisticsResponse;
import com.phananh.e_commerce.dashboard.application.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("admin/statistics")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Thống kê", description = "Quản lý dữ liệu tổng hợp và báo cáo")
public class DashboardController {

	StatisticsService statisticsService;

	@Operation(summary = "Lấy tổng quan thống kê", description = "Lấy tồng hợp các chỉ số thống kê chính của hệ thống")
	@GetMapping("/overview")
	public ResponseEntity<ApiResponse<StatisticsResponse>> getStatistics() {
		StatisticsResponse response = statisticsService.getStatistics();
		return ResponseEntity.ok(ApiResponse.<StatisticsResponse>builder()
				.message("Get dashboard overview successfully")
				.result(response)
				.build());
	}

	@Operation(summary = "Lấy thống kê đơn hàng", description = "Lấy thống kê số lượng đơn hàng theo các tiêu chí lọc")
	@PostMapping("/orders")
	public ResponseEntity<ApiResponse<DashboardResponse>> getOrderStatistics(@RequestBody @Valid DashboardOrderStatisticRequest request) {
		DashboardResponse response = statisticsService.getOrderStatistics(request);
		return ResponseEntity.ok(ApiResponse.<DashboardResponse>builder()
				.message("Get order statistics successfully")
				.result(response)
				.build());
	}

	@Operation(summary = "Lấy báo cáo doanh thu", description = "Lấy báo cáo doanh thu theo khoảng thời gian được chỉ định")
	@PostMapping("/revenue")
	public ResponseEntity<ApiResponse<SalesStatsResponse>> getRevenueReport(@RequestBody @Valid DashboardRevenueReportRequest request) {
		SalesStatsResponse response = statisticsService.getRevenueReport(request);
		return ResponseEntity.ok(ApiResponse.<SalesStatsResponse>builder()
				.message("Get revenue report successfully")
				.result(response)
				.build());
	}
}
