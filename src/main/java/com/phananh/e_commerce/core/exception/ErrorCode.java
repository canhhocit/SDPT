package com.phananh.e_commerce.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    // Success code
    SUCCESS(1000, "Success", HttpStatus.OK),

    // Client errors (4xx)
    INVALID_REQUEST(400, "Invalid request", HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(400, "Validation error", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_USERNAME_OR_PASSWORD(401, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(401, "Token has expired", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(401, "Invalid token", HttpStatus.UNAUTHORIZED),
    TOKEN_INVALIDATED(401, "Token has been invalidated", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED(401, "Refresh token has expired", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(403, "Forbidden", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(403, "Account is disabled", HttpStatus.FORBIDDEN),
    NOT_FOUND(404, "Not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND(404, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_VARIANT_NOT_FOUND(404, "Product variant not found", HttpStatus.NOT_FOUND),
    ATTRIBUTE_NOT_FOUND(404, "Attribute not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(404, "Category not found", HttpStatus.NOT_FOUND),
    BRAND_NOT_FOUND(404, "Brand not found", HttpStatus.NOT_FOUND),
    ORDER_NOT_FOUND(404, "Order not found", HttpStatus.NOT_FOUND),
    ORDER_ITEM_NOT_FOUND(404, "Order item not found", HttpStatus.NOT_FOUND),
    CART_ITEM_NOT_FOUND(404, "Cart item not found", HttpStatus.NOT_FOUND),
    CONFLICT(409, "Conflict", HttpStatus.CONFLICT),
    CONCURRENT_UPDATE_ERROR(409, "Concurrent update error", HttpStatus.CONFLICT),
    USER_ALREADY_EXISTS(409, "User already exists", HttpStatus.CONFLICT),
    USERNAME_ALREADY_EXISTS(409, "Username already exists", HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS(409, "Email already exists", HttpStatus.CONFLICT),
    OLD_PASSWORD_INCORRECT(400, "Old password is incorrect", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY(400, "Invalid quantity", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK(400, "Insufficient stock", HttpStatus.BAD_REQUEST),

    // Server errors (5xx)
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    DATABASE_ERROR(500, "Database error", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_UPLOAD_ERROR(500, "File upload error", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_DELETE_ERROR(500, "File delete error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}


