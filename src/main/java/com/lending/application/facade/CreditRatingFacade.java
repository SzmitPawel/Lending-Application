package com.lending.application.facade;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.domain.credit.rating.CreditRatingResponseDTO;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.mapper.credit.rating.CreditRatingResponseMapper;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.credit.rating.CreditRatingService;
import com.lending.application.service.credit.rating.calculate.rating.CreditRatingEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreditRatingFacade {
    private final CreditRatingService creditRatingService;
    private final CreditRatingResponseMapper responseMapper;
    private final CreditRatingEvaluator creditRatingEvaluator;
    private final ClientService clientService;

    public CreditRatingResponseDTO createNewCreditRating(
            final Long clientId,
            final BigDecimal customerMonthlyIncome,
            final BigDecimal customerMonthlyExpenses) throws ClientNotFoundException {

        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(clientId, customerMonthlyExpenses, customerMonthlyIncome);

        CreditRating creditRating = new CreditRating();
        creditRating.setDateOfRating(LocalDate.now());
        creditRating.setCreditRating(creditRatingEnum);

        Client client = clientService.getClientById(clientId);
        client.setCreditRating(creditRating);
        clientService.saveClient(client);

        return responseMapper.mapToCreditRatingDTO(client.getCreditRating());
    }

    public CreditRatingResponseDTO getCreditRating(final Long clientId) throws ClientNotFoundException {
        Client client = clientService.getClientById(clientId);

        return responseMapper.mapToCreditRatingDTO(client.getCreditRating());
    }

    public void deleteCreditRating(final Long ratingId) throws CreditRatingNotFoundException {
        creditRatingService.deleteCreditRatingById(ratingId);
    }
}
