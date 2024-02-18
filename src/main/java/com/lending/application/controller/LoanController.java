package com.lending.application.controller;

import com.lending.application.domain.loan.LoanResponseDTO;
import com.lending.application.exception.*;
import com.lending.application.exception.controller.ApiError;
import com.lending.application.facade.LoanServiceFacade;
import com.lending.application.service.loan.dto.LoanCalculationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/lending/loan")
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Loan Controller", description = "Loan controller to create and get loans of the client.")
public class LoanController {
    private final LoanServiceFacade loanServiceFacade;
    private final static int MIN_AMOUNT = 1;
    private final static int MIN_MONTH = 1;
    private final static int MIN_LOAN_ID = 1;
    private final static int MIN_CLIENT_ID = 1;
    private final static int MAX_AMOUNT = 100000;
    private final static int MAX_MONTH = 120;

    @Operation(summary = "Get loan.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received Loan by id.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoanResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400", description = "Invalid input, bad validation.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When loan not found in the database.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @GetMapping(value = "/{loanId}")
    public ResponseEntity<LoanResponseDTO> getLoanById(
            @Parameter(
                    description = "Loan id / minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable @Min(MIN_LOAN_ID) final Long loanId)
            throws LoanNotFoundException {

        LoanResponseDTO retrievedLoanResponseDTO = loanServiceFacade.getLoanById(loanId);
        log.info("Successfully retrieved loan: " + loanId);
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanResponseDTO);
    }

    @Operation(summary = "Return all loans for client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received all loans of client.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = LoanResponseDTO.class)))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, bad validation.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When client not found in the database.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @GetMapping(value = "/all/{clientId}")
    public ResponseEntity<List<LoanResponseDTO>> getAllClientLoans(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable @Min(MIN_CLIENT_ID) final Long clientId)
            throws ClientNotFoundException {

        List<LoanResponseDTO> retrievedLoanResponseDTOList = loanServiceFacade.getAllClientLoans(clientId);
        log.info("Successfully retrieved client: " + clientId + " loans list.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanResponseDTOList);
    }

    @Operation(summary = "Calculate loan.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received loan calculations.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoanResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, bad validation.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Interrupted I/O operations. Failed WebScraping.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @GetMapping("/calculate")
    public ResponseEntity<LoanCalculationDto> calculateLoan(
            @Parameter(
                    description = "Amount of Loan, minimum:1 / maximum: 100000",
                    example = "1500.00",
                    required = true)
            @RequestParam("amount") @Min(MIN_AMOUNT) @Max(MAX_AMOUNT) final BigDecimal amount,
            @Parameter(
                    description = "Loan duration, minimum: 1 / maximum: 120",
                    example = "12",
                    required = true)
            @RequestParam("months") @Min(MIN_MONTH) @Max(MAX_MONTH) final int months)
            throws InvalidLoanAmountOfCreditException,
            IOException,
            InvalidLoanMonthsException {

        LoanCalculationDto retrievedLoanCalculationDto = loanServiceFacade.calculateLoan(amount, months);
        log.info("Successfully calculated loan.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanCalculationDto);
    }

    @Operation(summary = "Create loan.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed created loan.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LoanResponseDTO.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, bad validation.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "When client not found in the database.",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @PostMapping(value = "/create/{clientId}")
    public ResponseEntity<LoanResponseDTO> createLoan(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable("clientId") @Min(MIN_CLIENT_ID) final Long clientId,
            @Parameter(
                    description = "Amount of loan, minimum: 1 / maximum: 100000",
                    example = "1500.00",
                    required = true)
            @RequestParam("amount") @Min(MIN_AMOUNT) @Max(MAX_AMOUNT) final BigDecimal amount,
            @Parameter(
                    description = "Loan duration, minimum: 1 / maximum: 120",
                    example = "20",
                    required = true)
            @RequestParam("months") @Min(MIN_MONTH) @Max(MAX_MONTH) final int months)
            throws LowCreditRatingException,
            ClientNotFoundException,
            InvalidLoanAmountOfCreditException,
            IOException,
            InvalidLoanMonthsException {

        LoanResponseDTO retrievedLoanResponseDTO = loanServiceFacade.createNewLoan(clientId, amount, months);
        log.info("Successfully create loan: " + retrievedLoanResponseDTO.getLoanId());
        return ResponseEntity.status(HttpStatus.OK).body(retrievedLoanResponseDTO);
    }
}