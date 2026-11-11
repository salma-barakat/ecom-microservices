package com.ecommerce.product.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException e) {
        log.info("Inside handleValidationErrors: getting msg {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        // Field errors (form inputs)
        e.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        // Global errors (cross-field logic)
        e.getBindingResult().getGlobalErrors()
                .forEach(err -> errors.put(err.getObjectName(), err.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ProductNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ApiError> handleProductNotFoundException(Exception e) {
        log.info("Inside handleNotFoundException: getting msg {}", e.getMessage());
        return new ResponseEntity<>(new ApiError(404, e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ImageProcessingException.class)
    public ResponseEntity<ApiError> handleImageProcessingException(ImageProcessingException e) {
        log.info("Inside handleImageProcessingException: getting msg {}", e.getMessage());
        return new ResponseEntity<>(new ApiError(500, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException e) {
//        log.info("Inside handleAuthenticationException: getting msg {}", e.getMessage());
//        return new ResponseEntity<>(new ApiError(401, "Invalid email or password."),
//                HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(value = InsufficientQuantityException.class)
    public ResponseEntity<ApiError> handleInsufficientQuantityException(InsufficientQuantityException e) {
        log.info("Inside handleInsufficientQuantityException: getting msg {}", e.getMessage());
        return new ResponseEntity<>(new ApiError(409, e.getMessage()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception e) {
        log.info("Inside handleGeneralException: getting msg {}", e.getMessage());
        return  new ResponseEntity<>(new ApiError(500, "Unexpected server error."),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
