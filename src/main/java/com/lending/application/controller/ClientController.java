package com.lending.application.controller;

import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.controller.ApiError;
import com.lending.application.facade.ClientServiceFacade;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lending/client")
@Validated
@Tag(name = "Client", description = "Controller for client operations.")
public class ClientController {
    private final ClientServiceFacade clientServiceFacade;
    private final static int MIN_CLIENT_VALID = 1;


    @Operation(summary = "Get client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed received client.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientDto.class))),
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
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> getClientById(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        ClientDto retrievedClientDto = clientServiceFacade.getClientById(clientId);
        log.info("Successfully retrieved client: " + clientId);
        return ResponseEntity.ok(retrievedClientDto);
    }

    @Operation(summary = "Get list of all clients.")
    @ApiResponse(
            responseCode = "200",
            description = "Succeed received all clients.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            array = @ArraySchema(schema = @Schema(implementation = ClientDto.class)))
    )
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClientDto>> getAllClients() {

        List<ClientDto> retrievedClientDtoList = clientServiceFacade.getAllClients();
        log.info("Successfully retrieved clients list.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDtoList);
    }

    @Operation(summary = "Create client.")
    @ApiResponse(
            responseCode = "200",
            description = "Succeed created client.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ClientDto.class))
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> createClient(
            @Parameter(
                    description = "Client to add.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientDto.class)),
                    required = true)
            @RequestBody final ClientDto clientDto) {

        ClientDto retrievedClientDto = clientServiceFacade.createClient(clientDto);
        log.info("Successfully created a new client.");
        return ResponseEntity.status(HttpStatus.CREATED).body(retrievedClientDto);
    }

    @Operation(summary = "Delete client.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed delete client."),
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
    @DeleteMapping()
    public ResponseEntity<Void> deleteClientById(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        clientServiceFacade.deleteClientById(clientId);
        log.info("Successfully deleted client: " + clientId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Update client.")
    @ApiResponse(
            responseCode = "200",
            description = "Succeed update."
    )
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> updateClient(
            @Parameter(
                    description = "Client to update",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientDto.class)),
                    required = true
            )
            @RequestBody final ClientDto clientDto) {

        ClientDto retrievedClientDto = clientServiceFacade.updateClient(clientDto);
        log.info("Successfully updated client " + clientDto.getClientId());
        return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDto);
    }
}