package com.lending.application.relations;

import com.lending.application.domain.Client;
import com.lending.application.domain.Loan;
import com.lending.application.repository.ClientRepository;
import com.lending.application.repository.LoanRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ClientToLoanRelationsTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;

    @Test
    void createClientWithLoan_shouldReturn1ClientAnd1Loan() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.now(),
                22
        );

        clientRepository.saveAndFlush(client);
        loanRepository.saveAndFlush(loan);

        // when
        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        Client retrievedClient = clientRepository
                .findById(client.getClientID())
                .orElse(null);

        // then
        assertNotNull(retrievedClient);
        assertEquals(1, retrievedClient.getLoanList().size());
    }
    @Test
    void deleteLoanFromClient_shouldReturn1ClientAnd0Loans() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.now(),
                22
        );

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientID())
                .orElse(null);

        assertNotNull(retrievedClient);

        retrievedClient.getLoanList().remove(loan);
        loanRepository.delete(loan);
        clientRepository.saveAndFlush(client);

        Client retrievedClientAfterDelete = clientRepository
                .findById(retrievedClient.getClientID())
                .orElse(null);

        // then
        assertNotNull(retrievedClientAfterDelete);
        assertEquals(0, retrievedClientAfterDelete.getLoanList().size());
    }

    @Test
    void deleteClientWithLoan_shouldDeleteClientWithLoan() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.now(),
                22
        );

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        clientRepository.delete(client);

        Client retrievedClientAfterDelete = clientRepository
                .findById(client.getClientID())
                .orElse(null);

        // then
        assertNull(retrievedClientAfterDelete);
        assertEquals(0, clientRepository.count());
        assertEquals(0, loanRepository.count());
    }

    @Test
    void updateClientLoan_shouldReturnUpdatedData() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.now(),
                22
        );

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        loan.setLoanAmount(new BigDecimal(2000.00));
        loan.setInterest(10.0F);
        loanRepository.saveAndFlush(loan);

        Client retrievedClientAfterUpdate = clientRepository
                .findById(client.getClientID())
                .orElse(null);

        // then
        assertNotNull(retrievedClientAfterUpdate);
        assertEquals(new BigDecimal(2000.00), retrievedClientAfterUpdate
                .getLoanList()
                .get(0)
                .getLoanAmount());
        assertEquals(10.0F, retrievedClientAfterUpdate
                .getLoanList().get(0)
                .getInterest());
    }
    @Test
    void readLoanFromClient_shouldReturnAllData() {
        // given
        Client client = new Client();
        client.setName("Client");
        client.setLastName("Last name");

        Loan loan = new Loan(
                new BigDecimal(1000.00),
                5.0F,
                LocalDate.now(),
                22
        );

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientID())
                .orElse(null);

        // then
        assertNotNull(retrievedClient);
        assertEquals(new BigDecimal(1000.00), retrievedClient
                .getLoanList()
                .get(0)
                .getLoanAmount());
        assertEquals(5.0F, retrievedClient
                .getLoanList()
                .get(0)
                .getInterest());
        assertEquals(LocalDate.now(), retrievedClient
                .getLoanList().get(0)
                .getLoanStartDate());
        assertEquals(22, retrievedClient
                .getLoanList()
                .get(0)
                .getRepaymentPeriod());
    }
}