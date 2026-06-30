package com.phananh.e_commerce.order.infrastructure.persistence.repository.springdata;

import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsOverviewProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

    @Query(value = """
            select
                count(*) as totalOrders,
                sum(case when o.is_paid = true then 1 else 0 end) as paidOrders,
                sum(case when o.status = 'PENDING' then 1 else 0 end) as pendingOrders,
                coalesce(sum(case when o.is_paid = true then o.total_price else 0 end), 0) as totalRevenue
            from orders o
            """, nativeQuery = true)
    OrderStatisticsOverviewProjection getStatisticsOverview();

    @Query(value = """
            select
                count(*) as totalOrders,
                sum(case when o.is_paid = true then 1 else 0 end) as paidOrders,
                sum(case when o.status = 'PENDING' then 1 else 0 end) as pendingOrders,
                sum(case when o.status = 'CONFIRMED' then 1 else 0 end) as confirmedOrders,
                sum(case when o.status = 'SHIPPING' then 1 else 0 end) as shippingOrders,
                sum(case when o.status = 'DELIVERED' then 1 else 0 end) as deliveredOrders,
                sum(case when o.status = 'CANCELLED' then 1 else 0 end) as cancelledOrders,
                sum(case when o.status = 'RETURNED' then 1 else 0 end) as returnedOrders,
                coalesce(sum(case when o.is_paid = true then o.total_price else 0 end), 0) as totalRevenue
            from orders o
            where o.created_at between :fromDate and :toDate
            """, nativeQuery = true)
    OrderStatisticsRangeProjection getStatisticsByDateRange(@Param("fromDate") LocalDateTime fromDate,
                                                           @Param("toDate") LocalDateTime toDate);

    @Query(value = """
            select
                count(*) as totalOrders,
                sum(case when o.is_paid = true then 1 else 0 end) as paidOrders,
                coalesce(sum(case when o.is_paid = true then o.total_price else 0 end), 0) as totalRevenue
            from orders o
            where o.created_at between :fromDate and :toDate
            """, nativeQuery = true)
    OrderRevenueSummaryProjection getRevenueSummary(@Param("fromDate") LocalDateTime fromDate,
                                                     @Param("toDate") LocalDateTime toDate);

    @Query(value = """
            select
                case upper(:groupBy)
                    when 'DAY' then date_format(o.created_at, '%Y-%m-%d')
                    when 'MONTH' then date_format(o.created_at, '%Y-%m')
                    when 'YEAR' then date_format(o.created_at, '%Y')
                    when 'QUARTER' then concat(year(o.created_at), '-Q', quarter(o.created_at))
                end as period,
                count(*) as orders,
                sum(case when o.is_paid = true then 1 else 0 end) as paidOrders,
                coalesce(sum(case when o.is_paid = true then o.total_price else 0 end), 0) as revenue
            from orders o
            where o.created_at between :fromDate and :toDate
            group by period
            order by min(o.created_at)
            """, nativeQuery = true)
    List<OrderRevenuePeriodProjection> getRevenueReport(@Param("fromDate") LocalDateTime fromDate,
                                                        @Param("toDate") LocalDateTime toDate,
                                                        @Param("groupBy") String groupBy);
}


