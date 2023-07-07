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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        // when & then
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
        client = clientRepository.findAll().get(0);
        creditRating = client.getCreditRating();
        client.setCreditRating(null);
        creditRatingRepository.delete(creditRating);
        clientRepository.saveAndFlush(client);

        // then
        assertEquals(1, clientRepository.count());
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

        // then
        assertEquals(LocalDate.now(), clientRepository
                .findAll()
                .get(0)
                .getCreditRating()
                .getDateOfRating());
        assertEquals(CreditRatingEnum.TWO, clientRepository
                .findAll()
                .get(0)
                .getCreditRating()
                .getCreditRating());
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

        // when & then
        assertEquals(LocalDate.of(2023,01,01), clientRepository
                .findAll()
                .get(0)
                .getCreditRating()
                .getDateOfRating());
        assertEquals(CreditRatingEnum.ONE, clientRepository
                .findAll()
                .get(0)
                .getCreditRating()
                .getCreditRating());
    }
}