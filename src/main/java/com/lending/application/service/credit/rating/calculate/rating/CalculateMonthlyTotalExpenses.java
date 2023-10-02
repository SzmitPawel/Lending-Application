package com.lending.application.service.credit.rating.calculate.rating;

import com.lending.application.domain.Loan;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
class CalculateMonthlyTotalExpenses {

    private final ClientService clientService;

    public BigDecimal getTotalMonthlyExpenses(
            final Long clientId,
            final BigDecimal customerMonthlyExpenses) throws ClientNotFoundException {

        List<Loan> clientLoansList = getLoanListByClientId(clientId);

        BigDecimal totalMonthlyPayments = clientLoansList.stream()
                .map(Loan::getMonthlyPayment)
                .reduce(BigDecimal.ZERO,((bigDecimalA, bigDecimalB) -> bigDecimalA.add(bigDecimalB)));

        return totalMonthlyPayments.add(customerMonthlyExpenses);
    }

    private List<Loan> getLoanListByClientId(final Long clientId) throws ClientNotFoundException {
        return clientService.getClientById(clientId).getLoanList();
    }
}