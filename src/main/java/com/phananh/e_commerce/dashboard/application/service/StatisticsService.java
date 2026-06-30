package com.phananh.e_commerce.dashboard.application.service;

import com.phananh.e_commerce.dashboard.presentation.dto.request.DashboardOrderStatisticRequest;
import com.phananh.e_commerce.dashboard.presentation.dto.request.DashboardRevenueReportRequest;
import com.phananh.e_commerce.dashboard.application.dto.response.DashboardResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.SalesStatsResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.StatisticsResponse;

public interface StatisticsService {
	StatisticsResponse getStatistics();

	DashboardResponse getOrderStatistics(DashboardOrderStatisticRequest request);

	SalesStatsResponse getRevenueReport(DashboardRevenueReportRequest request);
}


