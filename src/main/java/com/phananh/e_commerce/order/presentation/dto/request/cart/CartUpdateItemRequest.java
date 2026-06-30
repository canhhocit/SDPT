package com.phananh.e_commerce.order.presentation.dto.request.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartUpdateItemRequest {

    @NotNull(message = "Cart item id is required")
    private Long cartItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be >= 1")
    private Integer quantity;

    @NotNull(message = "Variant id is required")
    private Long variantId;
}



