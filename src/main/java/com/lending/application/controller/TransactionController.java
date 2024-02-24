package com.lending.application.controller;

import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.domain.transaction.TransactionResponseDTO;
import com.lending.application.exception.controller.ApiError;
import com.lending.application.facade.TransactionServiceFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

import java.util.List;

@RestController
@RequestMapping("/lending/transaction")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Transactions Controller", description = "Controller for transactions operations.")
public class TransactionController {
    private final TransactionServiceFacade transactionServiceFacade;
    private final static int MIN_ACCOUNT_VALID = 1;

    @Operation(summary = "Get all operations for specific account.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received all transactions.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, bad validation.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @GetMapping("/all/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getAllAccountTransactions(
            @Parameter(
                    description = "account id. Validation: minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable("accountId") @Min(MIN_ACCOUNT_VALID) final Long accountId) {

        List<TransactionResponseDTO> retrievedTransactionResponseDTOList = transactionServiceFacade.getAllAccountTransactions(accountId);
        log.info("Successfully get all transactions for account: " + accountId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedTransactionResponseDTOList);
    }

    @Operation(summary = "Get all transactions by type for account.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received transactions.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, bad validation.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/find/{accountId}")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsByMethod(
            @Parameter(
                    description = "Transaction method.",
                    example = "WITHDRAWAL",
                    required = true)
            @RequestParam("method") final TransactionMethodEnum method,
            @Parameter(
                    description = "Account id. Validation minimum: 1",
                    example = "1",
                    required = true
            )
            @PathVariable("accountId") @Min(MIN_ACCOUNT_VALID) final Long accountId) {

        List<TransactionResponseDTO> retrievedTransactionResponseDTOList = transactionServiceFacade
                .getAllTransactionsByMethod(method, accountId);
        log.info("Successfully get all " + method + " transactions for account: " + accountId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedTransactionResponseDTOList);
    }
}