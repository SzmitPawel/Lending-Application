package com.lending.application.controller;

import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/lending/client")
public class ClientController {
    ClientService clientService;

    @GetMapping("{clientId}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable ("clientId") Long clientId) {

        try {
            ClientDto retrievedClientDto = clientService.getClientById(clientId);
            log.info("Client id: " + clientId + " found.");
            return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDto);
        } catch (ClientNotFoundException e) {
            log.error("Client id: " + clientId + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> retrievedClientDtoList = clientService.getAllClients();
        log.info("Return all clients.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDtoList);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addNewClient(@RequestBody ClientDto clientDto) {

        try {
            clientService.createClient(clientDto);
            log.info("A new client created.");
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Fields not-null are null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("{clientId}")
    public ResponseEntity<Void> deleteClientById(@PathVariable ("clientId") Long clientId) {
        try {
            clientService.deleteClientById(clientId);
            log.info("Client id: " + clientId + " deleted.");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ClientNotFoundException e) {
            log.info("Client id: " + clientId + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        try {
            ClientDto retrievedClientDto = clientService.updateClient(clientDto);
            log.info("Client id: " + clientDto.getClientId() + " updated.");
            return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDto);
        } catch (ClientNotFoundException e) {
            log.error("Client id: " + clientDto.getClientId() + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Fields not-null are null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}