package com.lending.application.relations;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.loan.Loan;
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

    private Client prepareClient() {
        Client client = new Client();
        client.setName("John");
        client.setLastName("Doe");

        return client;
    }

    private Loan prepareLoan() {
        return new Loan(
                BigDecimal.valueOf(1000.00),
                BigDecimal.valueOf(250),
                5.0F,
                LocalDate.now(),
                22
        );
    }

    @Test
    void createClientWithLoan_shouldReturn1ClientAnd1Loan() {
        // given
        Client client = prepareClient();
        Loan loan = prepareLoan();

        clientRepository.saveAndFlush(client);
        loanRepository.saveAndFlush(loan);

        // when
        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClient);
        assertEquals(1, retrievedClient.getLoanList().size());
    }
    @Test
    void deleteLoanFromClient_shouldReturn1ClientAnd0Loans() {
        // given
        Client client = prepareClient();
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        assertNotNull(retrievedClient);

        retrievedClient.getLoanList().remove(loan);
        loanRepository.delete(loan);
        clientRepository.saveAndFlush(client);

        Client retrievedClientAfterDelete = clientRepository
                .findById(retrievedClient.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClientAfterDelete);
        assertEquals(0, retrievedClientAfterDelete.getLoanList().size());
    }

    @Test
    void deleteClientWithLoan_shouldDeleteClientWithLoan() {
        // given
        Client client = prepareClient();
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        clientRepository.delete(client);

        Client retrievedClientAfterDelete = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNull(retrievedClientAfterDelete);
        assertEquals(0, clientRepository.count());
        assertEquals(0, loanRepository.count());
    }

    @Test
    void updateClientLoan_shouldReturnUpdatedData() {
        // given
        Client client = prepareClient();
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        loan.setLoanAmount(BigDecimal.valueOf(2000.00));
        loan.setInterest(10.0F);
        loanRepository.saveAndFlush(loan);

        Client retrievedClientAfterUpdate = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClientAfterUpdate);
        assertEquals(BigDecimal.valueOf(2000.00), retrievedClientAfterUpdate
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
        Client client = prepareClient();
        Loan loan = prepareLoan();

        client.getLoanList().add(loan);
        clientRepository.saveAndFlush(client);

        // when
        Client retrievedClient = clientRepository
                .findById(client.getClientId())
                .orElse(null);

        // then
        assertNotNull(retrievedClient);
        assertEquals(BigDecimal.valueOf(1000.00), retrievedClient
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