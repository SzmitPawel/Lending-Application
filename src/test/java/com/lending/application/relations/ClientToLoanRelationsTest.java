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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ClientToLoanRelationsTest {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    LoanRepository loanRepository;

    @Test
    void createClientWithLoan_shouldReturnClientWith1Loan() {
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

        // then
        assertEquals(1, clientRepository.findAll()
                .get(0)
                .getLoanList()
                .size());
    }
    @Test
    void deleteLoanFromClient_shouldReturn0Loans() {
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
        client = clientRepository.findAll().get(0);
        loan = client.getLoanList().get(0);
        client.getLoanList().remove(loan);
        loanRepository.delete(loan);
        clientRepository.saveAndFlush(client);

        // then
        assertEquals(0, clientRepository.findAll()
                .get(0)
                .getLoanList()
                .size());
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

        // then
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

        // then
        assertEquals(new BigDecimal(2000.00), clientRepository.findAll()
                .get(0)
                .getLoanList()
                .get(0)
                .getLoanAmount());
        assertEquals(10.0F, clientRepository.findAll()
                .get(0)
                .getLoanList()
                .get(0)
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

        // when & then
        assertEquals("Client", clientRepository.findAll()
                .get(0)
                .getName());
        assertEquals("Last name", clientRepository.findAll()
                .get(0)
                .getLastName());
        assertEquals(new BigDecimal(1000.00), clientRepository.findAll()
                .get(0)
                .getLoanList()
                .get(0)
                .getLoanAmount());
        assertEquals(5.0F, clientRepository.findAll()
                .get(0)
                .getLoanList()
                .get(0)
                .getInterest());
        assertEquals(LocalDate.now(), clientRepository.findAll()
                .get(0)
                .getLoanList()
                .get(0)
                .getLoanStartDate());
        assertEquals(22, clientRepository.findAll()
                .get(0)
                .getLoanList()
                .get(0)
                .getRepaymentPeriod());
    }
}