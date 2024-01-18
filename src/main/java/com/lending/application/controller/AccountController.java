package com.lending.application.controller;

import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.exception.controller.ApiError;
import com.lending.application.service.account.BalanceCommand;
import com.lending.application.service.account.DepositCommand;
import com.lending.application.service.account.WithdrawCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequestMapping("/lending/account")
@RequiredArgsConstructor
@Validated
@Tag(name = "Account",description = "Controller for account operations")
public class AccountController {
    private final DepositCommand depositCommand;
    private final WithdrawCommand withdrawCommand;
    private final BalanceCommand balanceCommand;
    private final static int MIN_DEPOSIT_VALID = 1;
    private final static int MIN_WITHDRAW_VALID = 1;
    private final static int MIN_CLIENT_VALID = 1;
    private final static int MAX_DEPOSIT_VALID = 10000;
    private final static int MAX_WITHDRAW_VALID = 10000;

    @Operation(summary = "Get account balance")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received account balance.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = BigDecimal.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When client not found in the database.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1")
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        BigDecimal balance = balanceCommand.getBalance(clientId);
        log.info("Successfully retrieved account balance for client: " + clientId);
        return ResponseEntity.status(HttpStatus.OK).body(balance);
    }

    @Operation(summary = "Making a deposit.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed make a deposit.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When client not found in database.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/deposit")
    public ResponseEntity<BigDecimal> deposit(
            @Parameter(
                    description = "Client id, minimum = 1",
                    example = "1")
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId,
            @Parameter(
                    description = "Deposit amount, minimum: / maximum: 10000",
                    example = "123.11")
            @RequestParam("deposit") @Min(MIN_DEPOSIT_VALID) @Max(MAX_DEPOSIT_VALID) final BigDecimal deposit)
            throws ClientNotFoundException {

        BigDecimal accountBalance = depositCommand.doDeposit(clientId,deposit);
        log.info("Successfully deposit " + deposit + " to the account for client: " + clientId);
        return ResponseEntity.status(HttpStatus.OK).body(accountBalance);
    }

    @Operation(summary = "Making a withdraw.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed make a withdraw.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BigDecimal.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found in database",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "402",
                    description = "Insufficient founds in an account",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class)))
    })
    @PutMapping("/withdraw")
    public ResponseEntity<BigDecimal> withdraw(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1")
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId,
            @Parameter(
                    description = "Withdraw amount, minimum: 1 / maximum: 10000",
                    example = "155.36")
            @RequestParam("withdraw") @Min(MIN_WITHDRAW_VALID) @Max(MAX_WITHDRAW_VALID) final BigDecimal withdraw)
            throws ClientNotFoundException, InsufficientFundsException {

        BigDecimal accountBalance = withdrawCommand.doWithdraw(clientId,withdraw);
        log.info("Successfully withdraw " + withdraw + " from the account for client " + clientId);
        return ResponseEntity.status(HttpStatus.OK).body(accountBalance);
    }
}