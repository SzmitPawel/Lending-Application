package com.lending.application.controller;

import com.lending.application.domain.dto.LoanDto;
import com.lending.application.exception.*;
import com.lending.application.facade.LoanServiceFacade;
import com.lending.application.service.loan.dto.LoanCalculationDto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;


@RestController
@RequestMapping("/lending/loan")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Validated
public class LoanController {
    private final LoanServiceFacade loanServiceFacade;

    @GetMapping()
    public ResponseEntity<LoanDto> getLoanById(
            @RequestParam @Min(1) final Long loanId) {
        try {
            LoanDto retrievedLoanDto = loanServiceFacade.getLoanById(loanId);
            log.info("Successfully retrieved loan: " + loanId);
            return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanDto);
        } catch (LoanNotFoundException e) {
            log.error("Failed to retrieve loan: " + loanId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<LoanDto>> getAllClientLoans(
            @RequestParam @Min(1) final Long clientId) {
        try {
            List<LoanDto> retrievedLoanDtoList = loanServiceFacade.getAllClientLoans(clientId);
            log.info("Successfully retrieved client: " + clientId + " loans list.");
            return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanDtoList);
        } catch (ClientNotFoundException e) {
            log.error("Failed to retrieve client: " + clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping( "/calculate")
    public ResponseEntity<LoanCalculationDto> calculateLoan(
            @RequestParam("amount") @Min(1) @Max(100000) final BigDecimal amount,
            @RequestParam("months") @Min(1) @Max(120) final int months) {
        try {
            LoanCalculationDto retrievedLoanCalculationDto = loanServiceFacade.calculateLoan(amount,months);
            log.info("Successfully calculated loan.");
            return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanCalculationDto);
        } catch (InvalidLoanAmountOfCreditException e) {
            log.error("Failed to calculate loan, invalid amount of credit.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InvalidLoanMonthsException e) {
            log.error("Failed to calculate loan, invalid months.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IOException e) {
            log.error("Failed to web scraping.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/create")
    public ResponseEntity<LoanDto> createLoan(
            @RequestParam("clientId") @Min(1) final Long clientId,
            @RequestParam("amount") @Min(1) @Max(100000) final BigDecimal amount,
            @RequestParam("months") @Min(1) @Max(120) final int months) {

        Long loanId = 0L;

        try {
            LoanDto retrievedLoanDto = loanServiceFacade.createNewLoan(clientId, amount, months);
            loanId = retrievedLoanDto.getLoanId();
            log.info("Successfully create loan: " + loanId);
            return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanDto);
        } catch (ClientNotFoundException e) {
            log.error("Failed to retrieve client: " + clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (InvalidLoanAmountOfCreditException e) {
            log.error("Failed to create loan: " + loanId + ", invalid amount of credit.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (LowCreditRatingException e) {
            log.error("Failed to create loan: " + loanId + ", too low credit rating.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (InvalidLoanMonthsException e) {
            log.error("Failed to create loan: " + loanId + ", invalid months.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IOException e) {
            log.error("Failed to web scraping.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}