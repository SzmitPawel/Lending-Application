package com.lending.application.service.credit.rating.calculate.rating;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.Loan;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateMonthlyTotalExpensesTest {
    @InjectMocks
    private CalculateMonthlyTotalExpenses calculateMonthlyTotalExpenses;
    @Mock
    private ClientService clientService;

    @Test
    void testGetTotalMonthlyExpenses() throws ClientNotFoundException {
        // given
        BigDecimal clientMonthlyExpenses = BigDecimal.valueOf(500);

        Loan loan1 = new Loan();
        loan1.setMonthlyPayment(BigDecimal.valueOf(100));
        Loan loan2 = new Loan();
        loan2.setMonthlyPayment(BigDecimal.valueOf(300));

        List<Loan> loanList = List.of(loan1,loan2);

        Client client = new Client();
        client.setName("John");
        client.setLastName("Doe");
        client.setClientId(1L);
        client.setLoanList(loanList);

        when(clientService.getClientById(any())).thenReturn(client);

        // when
        BigDecimal clientTotalExpenses = calculateMonthlyTotalExpenses
                .getTotalMonthlyExpenses(client.getClientId(),clientMonthlyExpenses);

        // then
        assertEquals(BigDecimal.valueOf(900), clientTotalExpenses);
    }
}