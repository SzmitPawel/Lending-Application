package com.lending.application.exception.controller;

import com.lending.application.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
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

    @ExceptionHandler(LowCreditRatingException.class)
    public ResponseEntity<ApiError> handleLowCreditRatingException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Too low credit rating.",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Data integrity violation.",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LoanNotFoundException.class)
    public ResponseEntity<ApiError> handleLoanNotFoundException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Loan not found.",
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidLoanAmountOfCreditException.class)
    public ResponseEntity<ApiError> handleInvalidLoanAmountOfCreditException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Failed to calculate loan, invalid amount of credit.",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidLoanMonthsException.class)
    public ResponseEntity<ApiError> handleInvalidLoanMonthsException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "Failed to calculate loan, invalid months.",
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiError> handleIOException(HttpServletRequest request) {

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                "failed or interrupted I/O operations.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        log.error(apiError.message());
        return new ResponseEntity<>(apiError,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}