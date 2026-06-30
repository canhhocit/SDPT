package com.phananh.e_commerce.dashboard.application.mapper;

import com.phananh.e_commerce.dashboard.application.dto.response.DashboardResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.SalesStatsResponse;
import com.phananh.e_commerce.dashboard.application.dto.response.StatisticsResponse;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;

@Mapper(componentModel = "spring")
public interface DashboardMapper {

    @Mapping(target = "totalUsers", source = "totalUsers")
    @Mapping(target = "totalProducts", source = "totalProducts")
    StatisticsResponse toStatisticsResponse(long totalUsers, long totalProducts);

    default DashboardResponse.StatusStatistic toStatusStatistic(String status, long count) {
        DashboardResponse.StatusStatistic statusStatistic = new DashboardResponse.StatusStatistic();
        statusStatistic.setStatus(status);
        statusStatistic.setCount(count);
        return statusStatistic;
    }

    default List<DashboardResponse.StatusStatistic> toStatusStatistics(OrderStatisticsRangeProjection projection) {
        return List.of(
                toStatusStatistic("PENDING", zeroIfNull(projection.getPendingOrders())),
                toStatusStatistic("CONFIRMED", zeroIfNull(projection.getConfirmedOrders())),
                toStatusStatistic("SHIPPING", zeroIfNull(projection.getShippingOrders())),
                toStatusStatistic("DELIVERED", zeroIfNull(projection.getDeliveredOrders())),
                toStatusStatistic("CANCELLED", zeroIfNull(projection.getCancelledOrders())),
                toStatusStatistic("RETURNED", zeroIfNull(projection.getReturnedOrders()))
        );
    }

    @Mapping(target = "fromDate", ignore = true)
    @Mapping(target = "toDate", ignore = true)
    @Mapping(target = "totalOrders", source = "totalOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "paidOrders", source = "paidOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "pendingOrders", source = "pendingOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "confirmedOrders", source = "confirmedOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "shippingOrders", source = "shippingOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "deliveredOrders", source = "deliveredOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "cancelledOrders", source = "cancelledOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "returnedOrders", source = "returnedOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "totalRevenue", source = "totalRevenue")
    @Mapping(target = "statusStatistics", ignore = true)
    DashboardResponse toDashboardBaseResponse(OrderStatisticsRangeProjection orderStats);

    default DashboardResponse toDashboardResponse(LocalDate fromDate,
                                                  LocalDate toDate,
                                                  OrderStatisticsRangeProjection orderStats,
                                                  List<DashboardResponse.StatusStatistic> statusStatistics) {
        DashboardResponse response = toDashboardBaseResponse(orderStats);
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setStatusStatistics(statusStatistics);
        return response;
    }

    @Mapping(target = "period", source = "period")
    @Mapping(target = "orders", source = "orders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "paidOrders", source = "paidOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "revenue", source = "revenue")
    SalesStatsResponse.Item toSalesItem(OrderRevenuePeriodProjection projection);

    @Mapping(target = "groupBy", ignore = true)
    @Mapping(target = "fromDate", ignore = true)
    @Mapping(target = "toDate", ignore = true)
    @Mapping(target = "totalOrders", source = "totalOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "paidOrders", source = "paidOrders", qualifiedByName = "zeroIfNull")
    @Mapping(target = "totalRevenue", source = "totalRevenue")
    @Mapping(target = "items", ignore = true)
    SalesStatsResponse toSalesSummaryResponse(OrderRevenueSummaryProjection summary);

    default SalesStatsResponse toSalesStatsResponse(String groupBy,
                                                    LocalDate fromDate,
                                                    LocalDate toDate,
                                                    OrderRevenueSummaryProjection summary,
                                                    List<SalesStatsResponse.Item> items) {
        SalesStatsResponse response = toSalesSummaryResponse(summary);
        response.setGroupBy(groupBy);
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setItems(items);
        return response;
    }

    @Named("zeroIfNull")
    default Long zeroIfNull(Long value) {
        return value == null ? 0L : value;
    }
}

