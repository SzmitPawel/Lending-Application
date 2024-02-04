package com.lending.application.controller;

import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.exception.controller.ApiError;
import com.lending.application.facade.CreditRatingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Credit Rating", description = "Controller for Credit Rating operations.")
public class CreditRatingController {
    private final CreditRatingFacade creditRatingFacade;
    private static final int MIN_CLIENT_VALID = 1;
    private static final int MIN_INCOME_VALID = 1;
    private static final int MIN_EXPENSES_VALID = 1;
    private static final int MIN_CREDIT_ID_VALID = 1;

    @Operation(summary = "Create Credit Rating for client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed created Credit Rating.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When client not found in the database.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping(
            value = "/{clientId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreditRatingDto> createCreditRating(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable("clientId") @Min(MIN_CLIENT_VALID) final Long clientId,
            @Parameter(
                    description = "Client monthly income, minimum: 1",
                    example = "5555.55",
                    required = true)
            @RequestParam("income") @Min(MIN_INCOME_VALID) final BigDecimal income,
            @Parameter(
                    description = "Client monthly expenses, minimum 1",
                    example = "1555.55",
                    required = true)
            @RequestParam("expenses") @Min(MIN_EXPENSES_VALID) final BigDecimal expenses)
            throws ClientNotFoundException {

        CreditRatingDto createdCreditRatingDto = creditRatingFacade
                .createNewCreditRating(clientId, income, expenses);

        log.info("Successfully created credit rating for client: " + clientId);
        return ResponseEntity.status(HttpStatus.OK).body(createdCreditRatingDto);
    }

    @Operation(summary = "Get Credit Rating for client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received Credit Rating for clint.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreditRatingDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping(
            value = "/{clientId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreditRatingDto> getCreditRating(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        CreditRatingDto retrievedCreditRatingDto = creditRatingFacade.getCreditRating(clientId);

        log.info("Successfully retrieved credit rating: " + retrievedCreditRatingDto.getRatingId());
        return ResponseEntity.status(HttpStatus.OK).body(retrievedCreditRatingDto);
    }

    @Operation(summary = "Update Credit Rating")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed updated Credit Rating.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreditRatingDto.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When Credit Rating not found.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CreditRatingDto> updateCreditRating(
            @Parameter(
                    description = "Credit Rating to update.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreditRatingDto.class)),
                    required = true)
            @RequestBody final CreditRatingDto creditRatingDto)
            throws CreditRatingNotFoundException {

        CreditRatingDto updatedCreditRatingDto = creditRatingFacade.updateCreditRating(creditRatingDto);
        log.info("Successfully updated credit rating: " + updatedCreditRatingDto.getRatingId());
        return ResponseEntity.status(HttpStatus.OK).body(updatedCreditRatingDto);
    }

    @Operation(summary = "Delete Credit Rating.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed delete Credit Rating."),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When Credit Rating not found.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @DeleteMapping(value = "/{creditRatingId}")
    public ResponseEntity<Void> deleteCreditRating(
            @Parameter(
                    description = "Credit Rating id, minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable("creditRatingId") @Min(MIN_CREDIT_ID_VALID) final Long creditRatingId)
            throws CreditRatingNotFoundException {

        creditRatingFacade.deleteCreditRating(creditRatingId);
        log.info("Successfully deleted credit rating: " + creditRatingId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}