package com.lending.application.service.credit.rating;

import com.lending.application.domain.Client;
import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.mapper.CreditRatingMapper;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.credit.rating.calculate.rating.CreditRatingEvaluator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreditRatingFacade {
    private final CreditRatingService creditRatingService;
    private final CreditRatingMapper creditRatingMapper;
    private final CreditRatingEvaluator creditRatingEvaluator;
    private final ClientService clientService;

    public CreditRatingDto createNewCreditRating(
            final Long clientId,
            final BigDecimal customerMonthlyIncome,
            final BigDecimal customerMonthlyExpenses) throws ClientNotFoundException {

        CreditRatingEnum creditRatingEnum = creditRatingEvaluator
                .getCreditRatingEnum(clientId,customerMonthlyExpenses,customerMonthlyIncome);

        CreditRating creditRating = new CreditRating();
        creditRating.setDateOfRating(LocalDate.now());
        creditRating.setCreditRating(creditRatingEnum);

        Client client = clientService.getClientById(clientId);
        client.setCreditRating(creditRating);
        clientService.saveClient(client);

        return creditRatingMapper.mapToCreditRatingDto(client.getCreditRating());
    }

    public CreditRatingDto getCreditRating(final Long clientId) throws ClientNotFoundException {
        Client client = clientService.getClientById(clientId);

        return creditRatingMapper.mapToCreditRatingDto(client.getCreditRating());
    }

    public CreditRatingDto updateCreditRating(final CreditRatingDto creditRatingDto) throws CreditRatingNotFoundException {
        CreditRating creditRating = creditRatingMapper.mapToCreditRating(creditRatingDto);

        CreditRatingDto updatedCreditRatingDto = creditRatingMapper
                .mapToCreditRatingDto(creditRatingService.updateCreditRatingById(creditRating));

        return updatedCreditRatingDto;
    }

    public void deleteCreditRating(final Long ratingId) throws CreditRatingNotFoundException {
        creditRatingService.deleteCreditRatingById(ratingId);
    }
}