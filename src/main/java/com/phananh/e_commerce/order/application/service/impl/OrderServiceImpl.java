package com.phananh.e_commerce.order.application.service.impl;

import com.phananh.e_commerce.core.util.ListUtils;
import com.phananh.e_commerce.order.application.dto.command.OrderItemCreateCommand;
import com.phananh.e_commerce.order.domain.model.Order;
import com.phananh.e_commerce.order.domain.model.OrderItem;
import com.phananh.e_commerce.order.domain.model.enums.OrderStatus;
import com.phananh.e_commerce.order.domain.model.enums.PaymentMethod;
import com.phananh.e_commerce.order.application.service.OrderService;
import com.phananh.e_commerce.order.application.dto.command.OrderCreateCommand;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenuePeriodProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderRevenueSummaryProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsOverviewProjection;
import com.phananh.e_commerce.order.application.dto.projection.order.OrderStatisticsRangeProjection;
import com.phananh.e_commerce.order.presentation.dto.request.order.CheckoutRequest;
import com.phananh.e_commerce.order.presentation.dto.request.order.OrderPreviewRequest;
import com.phananh.e_commerce.order.application.dto.response.order.OrderDetailResponse;
import com.phananh.e_commerce.order.application.dto.response.order.OrderPreviewDetailResponse;
import com.phananh.e_commerce.order.application.dto.response.order.OrderSummaryResponse;
import com.phananh.e_commerce.order.application.dto.response.order.StaffOrderResponse;
import com.phananh.e_commerce.order.domain.repository.OrderRepository;
import com.phananh.e_commerce.order.domain.repository.OrderItemRepository;
import com.phananh.e_commerce.product.application.service.ProductInternalService;
import com.phananh.e_commerce.product.application.service.StaffProductService;
import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.SecurityUtils;
import com.phananh.e_commerce.order.application.mapper.OrderMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;

    ProductInternalService productService;
    StaffProductService staffProductService;
    UserService userService;
    OrderMapper orderMapper;

    @Override
    @Transactional(readOnly = true)
    public OrderStatisticsOverviewProjection getStatisticsOverview() {
        return orderRepository.getStatisticsOverview();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatisticsRangeProjection getStatisticsByDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderRepository.getStatisticsByDateRange(fromDate, toDate);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderRevenueSummaryProjection getRevenueSummary(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderRepository.getRevenueSummary(fromDate, toDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderRevenuePeriodProjection> getRevenueReport(LocalDateTime fromDate, LocalDateTime toDate, String groupBy) {
        return orderRepository.getRevenueReport(fromDate, toDate, groupBy);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffOrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toStaffOrderResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPreviewDetailResponse previewOrder(List<OrderPreviewRequest> orderPreviewRequest) {
        if (ListUtils.isNullOrEmpty(orderPreviewRequest)) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        var userInfo = userService.getMyInfo();

        List<OrderPreviewDetailResponse.Item> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderPreviewRequest req : orderPreviewRequest) {
            ProductInfoResponse productInfo = productService.getProductInfoByVariantId(req.getVariantId());
            if (req.getQuantity() == null || req.getQuantity() <= 0) throw new AppException(ErrorCode.INVALID_REQUEST);
            if (req.getQuantity() > productInfo.getStockQuantity()) throw new AppException(ErrorCode.INSUFFICIENT_STOCK);

            BigDecimal line = productInfo.getVariantPrice().multiply(BigDecimal.valueOf(req.getQuantity()));
            total = total.add(line);

            items.add(orderMapper.toOrderPreviewItem(productInfo));
        }

        OrderPreviewDetailResponse response = orderMapper.toOrderPreviewDetailResponse(userInfo);
        response.setShippingFee(BigDecimal.ZERO);
        response.setTotalPrice(total);
        response.setItems(items);
        response.setPaymentMethods(Arrays.asList(PaymentMethod.values()));

        return response;
    }

    @Override
    @Transactional
    public void checkout(CheckoutRequest checkoutRequest) {
        if (checkoutRequest == null || checkoutRequest.getItems() == null || checkoutRequest.getItems().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.valueOf(checkoutRequest.getPaymentMethod().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Long userId = userService.getIdByUserName(SecurityUtils.getCurrentUserName());
        Boolean isPaid = paymentMethod != PaymentMethod.COD;
        OrderCreateCommand orderCreateCommand = OrderCreateCommand.builder()
                .userId(userId)
                .fullName(checkoutRequest.getFullName())
                .phoneNumber(checkoutRequest.getPhoneNumber())
                .shippingAddress(checkoutRequest.getShippingAddress())
                .paymentMethod(paymentMethod)
                .isPaid(isPaid)
                .build();

        Order order = Order.createFirst(orderCreateCommand);
        order = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> itemsToSave = new HashSet<>();

        for (CheckoutRequest.Item item : checkoutRequest.getItems()) {
            ProductInfoResponse productInfo = productService.getProductInfoByVariantId(item.getVariantId());
            if (item.getQuantity() == null || item.getQuantity() <= 0) throw new AppException(ErrorCode.INVALID_REQUEST);
            Integer currentStock = productInfo.getStockQuantity();
            if (item.getQuantity() > currentStock) throw new AppException(ErrorCode.INSUFFICIENT_STOCK);

            BigDecimal price = productInfo.getVariantPrice();
            BigDecimal line = price.multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(line);

            OrderItemCreateCommand orderItemCreateCommand = OrderItemCreateCommand.builder()
                    .order(order)
                    .variantId(item.getVariantId())
                    .quantity(item.getQuantity())
                    .price(price)
                    .build();
            OrderItem orderItem = OrderItem.create(orderItemCreateCommand);
            orderItemRepository.save(orderItem);

            int newStock = currentStock - item.getQuantity();
            staffProductService.updateVariantStock(item.getVariantId(), Math.max(newStock, 0));
            itemsToSave.add(orderItem);
        }
        order.updateTotalPrice(total);
        order.updateShippingFee(BigDecimal.ZERO);
        order.addOrderItems(itemsToSave);
        orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderSummaryResponse> getMyOrder() {
        Long userId = userService.getIdByUserName(SecurityUtils.getCurrentUserName());
        List<Order> orders = orderRepository.findByUserId(userId);

        return orders.stream().map(order -> {
            List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(order.getId());
            List<OrderSummaryResponse.Item> items = orderItems.stream()
                    .map(oi -> orderMapper.toOrderSummaryItem(
                            oi,
                            productService.getProductInfoByVariantId(oi.getVariantId())
                    ))
                    .collect(Collectors.toList());

            OrderSummaryResponse response = orderMapper.toOrderSummaryResponse(order);
            response.setItems(items);
            return response;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderItem> orderItems = orderItemRepository.findByOrder_Id(order.getId());

        List<OrderDetailResponse.Item> items = orderItems.stream().map(oi -> {
            ProductInfoResponse p = productService.getProductInfoByVariantId(oi.getVariantId());
            return orderMapper.toCustomerOrderDetailItem(oi, p);
        }).collect(Collectors.toList());

        OrderDetailResponse response = orderMapper.toCustomerOrderDetailResponse(order);
        response.setItems(items);
        return response;
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        try {
            order.updateStatus(OrderStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        orderRepository.save(order);
    }
}
