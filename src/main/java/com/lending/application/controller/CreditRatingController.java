package com.lending.application.controller;

import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.facade.CreditRatingFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/lending/credit")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class CreditRatingController {
    private final CreditRatingFacade creditRatingFacade;

    @PostMapping(value = "/client/{clientId}/income/{income}/expenses/{expenses}")
    public ResponseEntity<CreditRatingDto> createCreditRating(
            @PathVariable final Long clientId,
            @PathVariable final BigDecimal income,
            @PathVariable final BigDecimal expenses)
    {
        try {
            CreditRatingDto createdCreditRatingDto = creditRatingFacade
                    .createNewCreditRating(clientId, income, expenses);
            log.info("Successfully calculated credit rating for client: " +clientId);
            return ResponseEntity.status(HttpStatus.OK).body(createdCreditRatingDto);
        } catch (ClientNotFoundException e) {
            log.error("Failed to retrieve client: " + clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping(value = "{clientId}")
    public ResponseEntity<CreditRatingDto> getCreditRating(@PathVariable final Long clientId) {
        try {
            CreditRatingDto retrievedCreditRatingDto = creditRatingFacade.getCreditRating(clientId);
            log.info("Successfully retrieved credit rating: " + retrievedCreditRatingDto.getRatingId());
            return ResponseEntity.status(HttpStatus.OK).body(retrievedCreditRatingDto);
        } catch (ClientNotFoundException e) {
            log.error("Failed to retrieved credit rating for client: " + clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreditRatingDto> updateCreditRating(@RequestBody final CreditRatingDto creditRatingDto) {
        try {
            CreditRatingDto updatedCreditRatingDto = creditRatingFacade.updateCreditRating(creditRatingDto);
            log.info("Successfully updated credit rating: " + updatedCreditRatingDto.getRatingId());
            return ResponseEntity.status(HttpStatus.OK).body(updatedCreditRatingDto);
        } catch (CreditRatingNotFoundException e) {
            log.error("Failed to update client rating id: " + creditRatingDto.getRatingId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("{creditRatingId}")
    public ResponseEntity<Void> deleteCreditRating(@PathVariable final Long creditRatingId) {
        try {
            creditRatingFacade.deleteCreditRating(creditRatingId);
            log.info("Successfully deleted credit rating: " + creditRatingId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (CreditRatingNotFoundException e) {
            log.error("Failed to delete credit rating: " + creditRatingId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}