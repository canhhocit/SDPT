package com.phananh.e_commerce.core.exception;

import com.phananh.e_commerce.core.presentation.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<?>> handleAppException(AppException ex, WebRequest request) {
        log.error("AppException: ", ex);
        ErrorCode errorCode = ex.getErrorCode();
        return new ResponseEntity<>(
                ApiResponse.builder().code(errorCode.getCode()).message(ex.getMessage()).build(),
                errorCode.getStatusCode()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error ->
                errors.put(((FieldError) error).getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(
                ApiResponse.builder().code(ErrorCode.VALIDATION_ERROR.getCode()).message("Validation failed").result(errors).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<?>> handleResponseStatus(ResponseStatusException ex) {
        log.error("ResponseStatusException: ", ex);
        ErrorCode errorCode = mapHttpStatusToErrorCode(ex.getStatusCode());
        String message = ex.getReason() != null ? ex.getReason() : errorCode.getMessage();
        return new ResponseEntity<>(
                ApiResponse.builder().code(errorCode.getCode()).message(message).build(),
                ex.getStatusCode()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<?>> handleAccessDenied(AccessDeniedException ex) {
        return new ResponseEntity<>(
                ApiResponse.builder().code(ErrorCode.FORBIDDEN.getCode()).message(ErrorCode.FORBIDDEN.getMessage()).build(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobal(Exception ex) {
        log.error("Unexpected error: ", ex);
        return new ResponseEntity<>(
                ApiResponse.builder().code(ErrorCode.INTERNAL_SERVER_ERROR.getCode()).message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ErrorCode mapHttpStatusToErrorCode(Object statusCode) {
        if (statusCode instanceof HttpStatus status) {
            return switch (status) {
                case BAD_REQUEST -> ErrorCode.INVALID_REQUEST;
                case UNAUTHORIZED -> ErrorCode.UNAUTHORIZED;
                case FORBIDDEN -> ErrorCode.FORBIDDEN;
                case NOT_FOUND -> ErrorCode.NOT_FOUND;
                case CONFLICT -> ErrorCode.CONFLICT;
                default -> ErrorCode.INTERNAL_SERVER_ERROR;
            };
        }
        return ErrorCode.INTERNAL_SERVER_ERROR;
    }
}
