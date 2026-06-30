package com.phananh.e_commerce.order.application.mapper;

import com.phananh.e_commerce.order.domain.model.CartItem;
import com.phananh.e_commerce.order.application.dto.response.cart.CartItemResponse;
import com.phananh.e_commerce.product.application.dto.response.internal.ProductInfoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(target = "cartItemId", source = "cartItem.id")
    @Mapping(target = "productId", source = "productInfoResponse.productId")
    @Mapping(target = "productName", source = "productInfoResponse.productName")
    @Mapping(target = "productStatus", source = "productInfoResponse.productStatus")
    @Mapping(target = "currentVariantId", source = "cartItem.variantId")
    @Mapping(target = "variantSkuCode", source = "productInfoResponse.variantSkuCode")
    @Mapping(target = "variantImageUrl", source = "productInfoResponse.variantImageUrl")
    @Mapping(target = "variantPrice", source = "productInfoResponse.variantPrice")
    @Mapping(target = "stockQuantity", source = "productInfoResponse.stockQuantity")
    @Mapping(target = "cartItemQuantity", source = "cartItem.quantity")
    CartItemResponse toResponse(CartItem cartItem, ProductInfoResponse productInfoResponse);
}
