package com.phananh.e_commerce.order.application.service;

import com.phananh.e_commerce.order.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.order.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.order.application.dto.response.cart.CartItemResponse;

import java.util.List;

public interface CartItemService {
    List<CartItemResponse> getMyCart();

    void addProductToCart(CartAddItemRequest cartAddItemRequest);

    void removeProductFromCart(List<Long> cartItemIds);

    String updateCartItem(CartUpdateItemRequest cartUpdateItemRequest);
}


