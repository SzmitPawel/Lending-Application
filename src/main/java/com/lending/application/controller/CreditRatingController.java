package com.lending.application.controller;

import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.facade.CreditRatingFacade;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/lending/credit")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Validated
public class CreditRatingController {
    private final CreditRatingFacade creditRatingFacade;
    private static final int MIN_CLIENT_VALID = 1;
    private static final int MIN_INCOME_VALID = 1;
    private static final int MIN_EXPENSES_VALID = 1;
    private static final int MIN_CREDIT_ID_VALID = 1;

    @PostMapping()
    public ResponseEntity<CreditRatingDto> createCreditRating (
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId,
            @RequestParam("income") @Min(MIN_INCOME_VALID) final BigDecimal income,
            @RequestParam("expenses") @Min(MIN_EXPENSES_VALID) final BigDecimal expenses)
            throws ClientNotFoundException {

        CreditRatingDto createdCreditRatingDto = creditRatingFacade
                .createNewCreditRating(clientId,income,expenses);

        log.info("Successfully created credit rating for client: " + clientId);
        return ResponseEntity.status(HttpStatus.OK).body(createdCreditRatingDto);
    }

    @GetMapping()
    public ResponseEntity<CreditRatingDto> getCreditRating(
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        CreditRatingDto retrievedCreditRatingDto = creditRatingFacade.getCreditRating(clientId);

        log.info("Successfully retrieved credit rating: " + retrievedCreditRatingDto.getRatingId());
        return ResponseEntity.status(HttpStatus.OK).body(retrievedCreditRatingDto);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditRatingDto> updateCreditRating(
            @RequestBody final CreditRatingDto creditRatingDto)
            throws CreditRatingNotFoundException {

        CreditRatingDto updatedCreditRatingDto = creditRatingFacade.updateCreditRating(creditRatingDto);
        log.info("Successfully updated credit rating: " + updatedCreditRatingDto.getRatingId());
        return ResponseEntity.status(HttpStatus.OK).body(updatedCreditRatingDto);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteCreditRating(
            @RequestParam("creditRatingId") @Min(MIN_CREDIT_ID_VALID) final Long creditRatingId)
            throws CreditRatingNotFoundException {

            creditRatingFacade.deleteCreditRating(creditRatingId);
            log.info("Successfully deleted credit rating: " + creditRatingId);
            return ResponseEntity.status(HttpStatus.OK).build();
    }
}