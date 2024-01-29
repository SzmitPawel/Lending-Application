package com.lending.application.controller;

import com.lending.application.domain.client.ClientRequestDTO;
import com.lending.application.domain.client.ClientResponseDTO;
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
                            schema = @Schema(implementation = ClientResponseDTO.class))),
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
    @GetMapping(
            value = "/{clientId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ClientResponseDTO> getClientById(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        ClientResponseDTO clientResponseDTO = clientServiceFacade.getClientById(clientId);
        log.info("Successfully retrieved client: " + clientId);
        return ResponseEntity.ok(clientResponseDTO);
    }

    @Operation(summary = "Get list of all clients.")
    @ApiResponse(
            responseCode = "200",
            description = "Succeed received all clients.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = ClientResponseDTO.class)))
    )
    @GetMapping(
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<ClientResponseDTO>> getAllClients() {

        List<ClientResponseDTO> clientResponseDTOList = clientServiceFacade.getAllClients();
        log.info("Successfully retrieved clients list.");
        return ResponseEntity.status(HttpStatus.OK).body(clientResponseDTOList);
    }

    @Operation(summary = "Create client.")
    @ApiResponse(
            responseCode = "200",
            description = "Succeed created client.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ClientResponseDTO.class))
    )
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ClientResponseDTO> createClient(
            @Parameter(
                    description = "Client to add.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientRequestDTO.class)),
                    required = true)
            @RequestBody final ClientRequestDTO clientRequestDTO) {

        ClientResponseDTO clientResponseDTO = clientServiceFacade.createClient(clientRequestDTO);
        log.info("Successfully created a new client.");
        return ResponseEntity.status(HttpStatus.CREATED).body(clientResponseDTO);
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
    @DeleteMapping(value = "/{clientId}")
    public ResponseEntity<Void> deleteClientById(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true)
            @PathVariable("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        clientServiceFacade.deleteClientById(clientId);
        log.info("Successfully deleted client: " + clientId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Update client.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Succeed update.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "When client not found in the database.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input, validation error.",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiError.class))
            )
    })
    @PutMapping(
            value = "/{clientId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ClientResponseDTO> updateClient(
            @Parameter(
                    description = "Client id, minimum: 1",
                    example = "1",
                    required = true
            )
            @PathVariable("clientId") @Min(MIN_CLIENT_VALID) final Long clientId,
            @Parameter(
                    description = "Client to update",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientRequestDTO.class)),
                    required = true
            )
            @RequestBody final ClientRequestDTO clientRequestDTO)
            throws ClientNotFoundException {
        ClientResponseDTO clientResponseDTO = clientServiceFacade
                .updateClient(clientId, clientRequestDTO);
        log.info("Successfully updated client " + clientResponseDTO.getClientId());
        return ResponseEntity.status(HttpStatus.OK).body(clientResponseDTO);
    }
}