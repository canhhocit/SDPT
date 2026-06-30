package com.phananh.e_commerce.order.application.service.impl;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.ListUtils;
import com.phananh.e_commerce.core.util.SecurityUtils;
import com.phananh.e_commerce.order.application.mapper.CartItemMapper;
import com.phananh.e_commerce.order.application.service.CartItemService;
import com.phananh.e_commerce.order.domain.model.CartItem;
import com.phananh.e_commerce.order.domain.repository.CartItemRepository;
import com.phananh.e_commerce.order.presentation.dto.request.cart.CartAddItemRequest;
import com.phananh.e_commerce.order.presentation.dto.request.cart.CartUpdateItemRequest;
import com.phananh.e_commerce.order.application.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import com.phananh.e_commerce.product.application.service.ProductInternalService;
import com.phananh.e_commerce.usermanagement.application.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemServiceImpl implements CartItemService {

    CartItemRepository cartItemRepository;

    CartItemMapper cartItemMapper;
    ProductInternalService productService;
    UserService userService;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemResponse> getMyCart() {
        Long userId = userService.getIdByUserName(SecurityUtils.getCurrentUserName());
        List<CartItem> cartItems = cartItemRepository.getByUserId(userId);

        if (ListUtils.isNullOrEmpty(cartItems)) return null;
        else {
            return cartItems.stream()
                    .map(cartItem -> {
                        ProductInfoResponse productInfo = productService.getProductInfoByVariantId(cartItem.getVariantId());
                        return cartItemMapper.toResponse(cartItem, productInfo);
                    })
                    .toList();
        }
    }

    @Override
    @Transactional
    public void addProductToCart(CartAddItemRequest cartAddItemRequest) {
        Long userId = userService.getIdByUserName(SecurityUtils.getCurrentUserName());
        Optional<CartItem> cartItem = cartItemRepository.getByUserIdAndVariantId(userId, cartAddItemRequest.getVariantId());
        if(cartItem.isEmpty()){
            if(cartAddItemRequest.getQuantity() > productService.getStockQuantityByVariantId(cartAddItemRequest.getVariantId())){
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            CartItem newCartItem = CartItem.addItem(userId, cartAddItemRequest.getVariantId(), cartAddItemRequest.getQuantity());
            cartItemRepository.save(newCartItem);
        }
        else {
            CartItem existingCartItem = cartItem.get();
            Integer newQuantity = existingCartItem.getQuantity() + cartAddItemRequest.getQuantity();
            if(newQuantity > productService.getStockQuantityByVariantId(cartAddItemRequest.getVariantId())){
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            existingCartItem.updateQuantity(newQuantity);
            cartItemRepository.save(existingCartItem);
        }
    }

    @Override
    @Transactional
    public void removeProductFromCart(List<Long> cartItemIds) {
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Long userId = userService.getIdByUserName(SecurityUtils.getCurrentUserName());
        List<CartItem> cartItems = cartItemRepository.getByListId(cartItemIds);
        if (ListUtils.isNullOrEmpty(cartItems)) {
            throw new AppException(ErrorCode.CART_ITEM_NOT_FOUND);
        }
        if(cartItems.stream().anyMatch(item -> !item.getUserId().equals(userId))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    @Transactional
    public String updateCartItem(CartUpdateItemRequest cartUpdateItemRequest){
        if (cartUpdateItemRequest == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Long userId = userService.getIdByUserName(SecurityUtils.getCurrentUserName());
        CartItem cartItems = cartItemRepository.getById(cartUpdateItemRequest.getCartItemId())
                .orElseThrow(() -> new AppException(ErrorCode.CART_ITEM_NOT_FOUND));
        if(!cartItems.getUserId().equals(userId)) throw new AppException(ErrorCode.UNAUTHORIZED);

        Optional<CartItem> existingCartItem = cartItemRepository.getByUserIdAndVariantId(userId, cartUpdateItemRequest.getVariantId());
        if(existingCartItem.isEmpty()){
            cartItems.updateVariant(cartUpdateItemRequest.getVariantId());

            if(cartUpdateItemRequest.getQuantity() > productService.getStockQuantityByVariantId(cartUpdateItemRequest.getVariantId())){
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }
            cartItems.updateQuantity(cartUpdateItemRequest.getQuantity());

            cartItemRepository.save(cartItems);

            return "Update successful";
        }
        else {
            cartItemRepository.delete(cartItems);
            CartItem cartItemUpdate = existingCartItem.get();
            Integer newQuantity = cartItemUpdate.getQuantity() + cartUpdateItemRequest.getQuantity();
            Integer stockQuantity = productService.getStockQuantityByVariantId(cartUpdateItemRequest.getVariantId());
            if(newQuantity > stockQuantity){
                newQuantity = stockQuantity;
            }
            cartItemUpdate.updateQuantity(newQuantity);
            cartItemRepository.save(cartItemUpdate);

            return "Update successful, variant existing in cart and quantity has been adjusted to " + newQuantity;
        }
    }
}


