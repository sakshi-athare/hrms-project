package com.hrms.common.handler;

import com.hrms.common.exception.BadRequestException;
import com.hrms.common.exception.InvalidLeaveStateException;
import com.hrms.common.exception.ResourceNotFoundException;
import com.hrms.common.exception.UnauthorizedActionException;
import com.hrms.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException; // ✅ ADD

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /* RESOURCE NOT FOUND */

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND, request);
    }

    /* STATIC RESOURCE NOT FOUND (🔥 IMPORTANT FIX) */

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<?>> handleNoResource(
            NoResourceFoundException ex,
            HttpServletRequest request) {

        return buildResponse(
                "Resource not found",
                HttpStatus.NOT_FOUND,
                request
        );
    }

    /* FORBIDDEN */

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ApiResponse<?>> handleForbidden(
            UnauthorizedActionException ex,
            HttpServletRequest request) {

        return buildResponse(ex.getMessage(), HttpStatus.FORBIDDEN, request);
    }

    /* BAD REQUEST GROUP */

    @ExceptionHandler({
            BadRequestException.class,
            InvalidLeaveStateException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ApiResponse<?>> handleBadRequest(
            RuntimeException ex,
            HttpServletRequest request) {

        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, request);
    }

    /* VALIDATION ERROR */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        return buildResponse(message, HttpStatus.BAD_REQUEST, request);
    }

    /* GENERIC ERROR */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleGlobal(
            Exception ex,
            HttpServletRequest request) {

        log.error("Unhandled exception occurred", ex);

        return buildResponse(
                "Internal server error",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    /* COMMON BUILDER */

    private ResponseEntity<ApiResponse<?>> buildResponse(
            String message,
            HttpStatus status,
            HttpServletRequest request) {

        ApiResponse<?> response =
                ApiResponse.error(message, status.value(), request.getRequestURI());

        return ResponseEntity
                .status(status)
                .body(response);
    }
}