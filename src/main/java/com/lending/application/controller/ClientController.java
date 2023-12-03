package com.lending.application.controller;

import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.facade.ClientServiceFacade;
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
public class ClientController {
    private final ClientServiceFacade clientServiceFacade;
    private final static int MIN_CLIENT_VALID = 1;

    @GetMapping
    public ResponseEntity<ClientDto> getClientById(
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        ClientDto retrievedClientDto = clientServiceFacade.getClientById(clientId);
        log.info("Successfully retrieved client: " + clientId);
        return ResponseEntity.ok(retrievedClientDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClientDto>> getAllClients() {

        List<ClientDto> retrievedClientDtoList = clientServiceFacade.getAllClients();
        log.info("Successfully retrieved clients list.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDtoList);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> createClient(
            @RequestBody final ClientDto clientDto) {

        ClientDto retrievedClientDto = clientServiceFacade.createClient(clientDto);
        log.info("Successfully created a new client.");
        return ResponseEntity.status(HttpStatus.CREATED).body(retrievedClientDto);
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteClientById(
            @RequestParam("clientId") @Min(MIN_CLIENT_VALID) final Long clientId)
            throws ClientNotFoundException {

        clientServiceFacade.deleteClientById(clientId);
        log.info("Successfully deleted client: " + clientId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> updateClient(
            @RequestBody final ClientDto clientDto) {

        ClientDto retrievedClientDto = clientServiceFacade.updateClient(clientDto);
        log.info("Successfully updated client " + clientDto.getClientId());
        return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDto);
    }
}