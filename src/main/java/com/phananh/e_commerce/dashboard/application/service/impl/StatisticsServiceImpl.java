package com.phananh.e_commerce.dashboard.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.dashboard.presentation.dto.request.DashboardOrderStatisticRequest;
import com.phananh.e_commerce.dashboard.presentation.dto.request.DashboardRevenueReportRequest;
import com.phananh.e_commerce.dashboard.presentation.dto.request.RevenueGroupBy;
import com.phananh.e_commerce.dashboard.application.dto.response.DashboardResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.SalesStatsResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.StatisticsResponse;
import com.phananh.e_commerce.dashboard.application.mapper.DashboardMapper;
import com.phananh.e_commerce.dashboard.application.service.StatisticsService;
import com.phananh.e_commerce.order.application.service.OrderService;
import com.phananh.e_commerce.product.application.service.ProductInternalService;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatisticsServiceImpl implements StatisticsService {

	OrderService orderService;
	ProductInternalService productInternalService;
	UserService userService;
	DashboardMapper dashboardMapper;

	@Override
	public StatisticsResponse getStatistics() {
		return dashboardMapper.toStatisticsResponse(
				userService.countUsers(),
				productInternalService.countProducts()
		);
	}

	@Override
	public DashboardResponse getOrderStatistics(DashboardOrderStatisticRequest request) {
		LocalDate fromDate = validateAndGetFromDate(request.getFromDate(), request.getToDate());
		LocalDate toDate = request.getToDate();
		var orderStats = orderService.getStatisticsByDateRange(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
		List<DashboardResponse.StatusStatistic> statusStatistics = dashboardMapper.toStatusStatistics(orderStats);

		return dashboardMapper.toDashboardResponse(
				fromDate,
				toDate,
				orderStats,
				statusStatistics
		);
	}

	@Override
	public SalesStatsResponse getRevenueReport(DashboardRevenueReportRequest request) {
		LocalDate fromDate = validateAndGetFromDate(request.getFromDate(), request.getToDate());
		LocalDate toDate = request.getToDate();
		String groupBy = request.getGroupBy() == null ? RevenueGroupBy.DAY.name() : request.getGroupBy().name();

		var summary = orderService.getRevenueSummary(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX));
		List<SalesStatsResponse.Item> items = orderService.getRevenueReport(fromDate.atStartOfDay(), toDate.atTime(LocalTime.MAX), groupBy)
				.stream()
				.map(dashboardMapper::toSalesItem)
				.collect(Collectors.toList());

		return dashboardMapper.toSalesStatsResponse(
				groupBy,
				fromDate,
				toDate,
				summary,
				items
		);
	}

	private LocalDate validateAndGetFromDate(LocalDate fromDate, LocalDate toDate) {
		if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
			throw new AppException(ErrorCode.INVALID_REQUEST);
		}
		return fromDate;
	}
}



