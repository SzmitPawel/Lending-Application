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
    private final static int MIN_AMOUNT = 1;
    private final static int MIN_MONTH = 1;
    private final static int MIN_LOAN_ID = 1;
    private final static int MIN_CLIENT_ID = 1;
    private final static int MAX_AMOUNT = 100000;
    private final static int MAX_MONTH = 120;

    @GetMapping()
    public ResponseEntity<LoanDto> getLoanById(
            @RequestParam @Min(MIN_LOAN_ID) final Long loanId)
            throws LoanNotFoundException {

        LoanDto retrievedLoanDto = loanServiceFacade.getLoanById(loanId);
        log.info("Successfully retrieved loan: " + loanId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanDto);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<LoanDto>> getAllClientLoans(
            @RequestParam @Min(MIN_CLIENT_ID) final Long clientId)
            throws ClientNotFoundException {

        List<LoanDto> retrievedLoanDtoList = loanServiceFacade.getAllClientLoans(clientId);
        log.info("Successfully retrieved client: " + clientId + " loans list.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanDtoList);
    }

    @GetMapping( "/calculate")
    public ResponseEntity<LoanCalculationDto> calculateLoan(
            @RequestParam("amount") @Min(MIN_AMOUNT) @Max(MAX_AMOUNT) final BigDecimal amount,
            @RequestParam("months") @Min(MIN_MONTH) @Max(MAX_MONTH) final int months)
            throws InvalidLoanAmountOfCreditException,
                   IOException,
                   InvalidLoanMonthsException {

        LoanCalculationDto retrievedLoanCalculationDto = loanServiceFacade.calculateLoan(amount,months);
        log.info("Successfully calculated loan.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanCalculationDto);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<LoanDto> createLoan(
            @RequestParam("clientId") @Min(MIN_CLIENT_ID) final Long clientId,
            @RequestParam("amount") @Min(MIN_AMOUNT) @Max(MAX_AMOUNT) final BigDecimal amount,
            @RequestParam("months") @Min(MIN_MONTH) @Max(MAX_MONTH) final int months)
            throws LowCreditRatingException,
                   ClientNotFoundException,
                   InvalidLoanAmountOfCreditException,
                   IOException,
                   InvalidLoanMonthsException {

        LoanDto retrievedLoanDto = loanServiceFacade.createNewLoan(clientId, amount, months);
        log.info("Successfully create loan: " + retrievedLoanDto.getLoanId());
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanDto);
    }
}