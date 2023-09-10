package com.lending.application.controller;

import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientAccountService;
import com.lending.application.service.client.ClientService;
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
    private final ClientService clientService;
    private final ClientAccountService clientAccountService;

    @GetMapping("{clientId}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long clientId) {
        try {
            ClientDto retrievedClientDto = clientService.getClientById(clientId);
            log.info("Operation return succeeded client: " + clientId);
            return ResponseEntity.ok(retrievedClientDto);
        } catch (ClientNotFoundException e) {
            log.error("Operation return failed client: " + clientId + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> retrievedClientDtoList = clientService.getAllClients();
        log.info("Operation return all clients succeeded.");
        return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDtoList);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto clientDto) {
        ClientDto retrievedClientDto = clientAccountService.createClient(clientDto);
        log.info("Operation create a new client succeeded.");
        return ResponseEntity.status(HttpStatus.CREATED).body(retrievedClientDto);
    }

    @DeleteMapping("{clientId}")
    public ResponseEntity<Void> deleteClientById(@PathVariable ("clientId") Long clientId) {
        try {
            clientService.deleteClientById(clientId);
            log.info("Operation delete succeeded client id: " + clientId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (ClientNotFoundException e) {
            log.info("Operation delete failed client id: " + clientId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClientDto> updateClient(@RequestBody ClientDto clientDto) {
        try {
            ClientDto retrievedClientDto = clientService.updateClient(clientDto);
            log.info("Operation update succeeded client id: " + clientDto.getClientId());
            return ResponseEntity.status(HttpStatus.OK).body(retrievedClientDto);
        } catch (ClientNotFoundException e) {
            log.error("Operation update failed client id: " + clientDto.getClientId() + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DataIntegrityViolationException e) {
            log.error("Operation update failed not-null are null.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}