package com.lending.application.relations;

import com.lending.application.domain.Account;
import com.lending.application.domain.Client;
import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.repository.AccountRepository;
import com.lending.application.repository.ClientRepository;
import com.lending.application.repository.CreditRatingRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ClientRelationsTest {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CreditRatingRepository creditRatingRepository;

    @Nested
    public class ClientToAccount {
        @Test
        void createClientWithAccount_shouldReturn1ClientAnd1Account() {
            // given
            Client client = new Client();
            client.setName("Client");
            client.setLastName("Last name");

            Account account = new Account();

            client.setAccount(account);

            clientRepository.save(client);

            // when & then
            assertEquals(1,clientRepository.count());
            assertEquals(1,accountRepository.count());
        }
        @Test
        void deleteClientWithAccount_shouldReturn0ClientAnd0Accounts() {
            // given
            Client client = new Client();
            client.setName("Client");
            client.setLastName("Last name");

            Account account = new Account();
            account.setBalance(new BigDecimal(1));

            client.setAccount(account);
            clientRepository.save(client);

            // when
            clientRepository.delete(client);

            // then
            assertEquals(0, clientRepository.count());
            assertEquals(0, accountRepository.count());
        }
        @Test
        void updateAccount_shouldReturnUpdatedData() {
            // given
            Client client = new Client();
            client.setName("Client");
            client.setLastName("Last name");

            Account account = new Account();
            account.setBalance(new BigDecimal(1));

            client.setAccount(account);
            clientRepository.save(client);

            // when
            account.setBalance(new BigDecimal(200));
            accountRepository.save(account);

            // then
            assertEquals(new BigDecimal(200), clientRepository
                    .findAll()
                    .get(0)
                    .getAccount()
                    .getBalance());
        }
        @Test
        void readAccount_shouldReturnAllData() {
            // given
            Client client = new Client();
            client.setName("Client");
            client.setLastName("Last name");

            Account account = new Account();
            account.setBalance(new BigDecimal(1));

            client.setAccount(account);
            clientRepository.save(client);

            // when & then
            assertEquals(new BigDecimal(1), clientRepository
                    .findAll()
                    .get(0)
                    .getAccount()
                    .getBalance());
        }

    }
    @Nested
    public class ClientToRating {
        @Test
        void createClientWithCreditRating_shouldReturn1ClientAnd1Rating() {
            // given
            Client client = new Client();
            client.setName("Client");
            client.setLastName("Last name");

            CreditRating creditRating = new CreditRating();

            client.setCreditRating(creditRating);
            clientRepository.save(client);

            // when & then
            assertEquals(1, clientRepository.count());
            assertEquals(1, creditRatingRepository.count());
        }
        @Test
        void deleteClientWithCreditRating_shouldReturn0ClientAnd0Ratings() {
            // given
            Client client = new Client();
            client.setName("Client");
            client.setLastName("Last name");

            CreditRating creditRating = new CreditRating();

            client.setCreditRating(creditRating);
            clientRepository.save(client);

            // when
            clientRepository.delete(client);

            // then
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
            clientRepository.save(client);

            // when
            creditRating.setDateOfRating(LocalDate.now());
            creditRating.setCreditRating(CreditRatingEnum.TWO);
            creditRatingRepository.save(creditRating);

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
            clientRepository.save(client);

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
}
