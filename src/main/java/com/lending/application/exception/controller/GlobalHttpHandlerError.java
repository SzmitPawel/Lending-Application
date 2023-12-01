package com.lending.application.exception.controller;

import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalHttpHandlerError extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidationException(ConstraintViolationException e,
                                                            HttpServletRequest request) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiError> handleClientNotFoundException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Client not found.",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CreditRatingNotFoundException.class)
    public ResponseEntity<ApiError> handleRatingNotFoundException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Credit rating not found.",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }
}