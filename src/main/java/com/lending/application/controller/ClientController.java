package com.lending.application.controller;

import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/lending/client")
public class ClientController {
    private final ClientServiceFacade clientServiceFacade;

    @GetMapping("{clientId}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long clientId) {
        try {
            ClientDto retrievedClientDto = clientServiceFacade.getClientById(clientId);
            log.info("Successfully retrieved client: " + clientId);
            return ResponseEntity.ok(retrievedClientDto);
        } catch (ClientNotFoundException e) {
            log.error("Failed to retrieve client: " + clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> retrievedClientDtoList = clientServiceFacade.getAllClients();
        log.info("Successfully retrieved clients list.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDtoList);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        ClientDto retrievedClientDto = clientServiceFacade.createClient(clientDto);
        log.info("Successfully created a new client.");
        return ResponseEntity.status(HttpStatus.CREATED).body(retrievedClientDto);
    }

    @DeleteMapping("{clientId}")
    public ResponseEntity<Void> deleteClientById(@PathVariable ("clientId") Long clientId) {
        try {
            clientServiceFacade.deleteClientById(clientId);
            log.info("Successfully deleted client: " + clientId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ClientNotFoundException e) {
            log.info("Failed to delete client: " + clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        try {
            ClientDto retrievedClientDto = clientServiceFacade.updateClient(clientDto);
            log.info("Successfully updated client " + clientDto.getClientId());
            return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDto);
        } catch (ClientNotFoundException e) {
            log.error("Failed to update client: " + clientDto.getClientId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Failed to update client due to data integrity violation.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}