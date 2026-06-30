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
public class SalesStatsResponse {
    private String groupBy;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Long totalOrders;
    private Long paidOrders;
    private BigDecimal totalRevenue;
    private List<Item> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String period;
        private Long orders;
        private Long paidOrders;
        private BigDecimal revenue;
    }
}

