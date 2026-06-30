package com.phananh.e_commerce.dashboard.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long totalOrders;
    private Long paidOrders;
    private Long pendingOrders;
    private Long confirmedOrders;
    private Long shippingOrders;
    private Long deliveredOrders;
    private Long cancelledOrders;
    private Long returnedOrders;
    private BigDecimal totalRevenue;
    private List<StatusStatistic> statusStatistics;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatusStatistic {
        private String status;
        private Long count;
    }
}

