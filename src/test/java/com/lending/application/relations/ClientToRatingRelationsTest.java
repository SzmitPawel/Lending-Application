package com.lending.application.relations;

import com.lending.application.domain.Client;
import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.repository.ClientRepository;
import com.lending.application.repository.CreditRatingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ClientToRatingRelationsTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CreditRatingRepository creditRatingRepository;

    @Test
    void createClientWithCreditRating_shouldReturn1ClientAnd1Rating() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        CreditRating creditRating = new CreditRating();

        client.setCreditRating(creditRating);
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClient);
        assertNotNull(retrievedClient.getCreditRating());
        assertEquals(1, clientRepository.count());
        assertEquals(1, creditRatingRepository.count());
    }
    @Test
    void deleteClientWithCreditRating_shouldReturn1ClientAnd0Ratings() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        CreditRating creditRating = new CreditRating();

        client.setCreditRating(creditRating);
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        assertNotNull(retrievedClient);
        creditRatingRepository.delete(retrievedClient.getCreditRating());
        retrievedClient.setCreditRating(null);
        clientRepository.saveAndFlush(client);

        Client retrievedClientAfterDelete = clientRepository
                .findById(retrievedClient.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClientAfterDelete);
        assertNull(retrievedClientAfterDelete.getCreditRating());
        assertEquals(1, clientRepository.count());
        assertEquals(0, creditRatingRepository.count());
    }

    @Test
    void deleteClientAndCreditRating_shouldDeleteClientWithRating() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        CreditRating creditRating = new CreditRating();

        client.setCreditRating(creditRating);
        clientRepository.saveAndFlush(client);

        // when
        clientRepository.delete(client);

        Client retrievedClientAfterDelete = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNull(retrievedClientAfterDelete);
        assertEquals(0, clientRepository.count());
        assertEquals(0, creditRatingRepository.count());
    }

    @Test
    void updateCreditRating_shouldReturnUpdatedData() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        CreditRating creditRating = new CreditRating();
        creditRating.setDateOfRating(LocalDate.of(2023,01,01));
        creditRating.setCreditRating(CreditRatingEnum.ONE);

        client.setCreditRating(creditRating);
        clientRepository.saveAndFlush(client);

        // when
        creditRating.setDateOfRating(LocalDate.now());
        creditRating.setCreditRating(CreditRatingEnum.TWO);
        creditRatingRepository.saveAndFlush(creditRating);

        Client retrievedClientAfterUpdate = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClientAfterUpdate);
        assertEquals(LocalDate.now(), retrievedClientAfterUpdate.getCreditRating().getDateOfRating());
        assertEquals(CreditRatingEnum.TWO, retrievedClientAfterUpdate.getCreditRating().getCreditRating());
    }
    @Test
    void readCreditRating_shouldReturnAllData() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        CreditRating creditRating = new CreditRating();
        creditRating.setDateOfRating(LocalDate.of(2023,01,01));
        creditRating.setCreditRating(CreditRatingEnum.ONE);

        client.setCreditRating(creditRating);
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClient);
        assertEquals(LocalDate.of(2023,01,01), retrievedClient
                .getCreditRating()
                .getDateOfRating());
        assertEquals(CreditRatingEnum.ONE, retrievedClient
                .getCreditRating()
                .getCreditRating());
    }
}