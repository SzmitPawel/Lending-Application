package com.lending.application.facade;

import com.lending.application.domain.Client;
import com.lending.application.domain.CreditRating;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.mapper.CreditRatingMapper;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.credit.rating.CreditRatingService;
import com.lending.application.service.credit.rating.calculate.rating.CreditRatingEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRatingFacadeTest {
    @InjectMocks
    private CreditRatingFacade creditRatingFacade;
    @Mock
    private CreditRatingService creditRatingService;
    @Mock
    private CreditRatingMapper creditRatingMapper;
    @Mock
    private CreditRatingEvaluator creditRatingEvaluator;
    @Mock
    private ClientService clientService;

    private CreditRating prepareCreditRating() {
        CreditRating creditRating = new CreditRating();
        creditRating.setRatingId(1L);
        creditRating.setCreditRating(CreditRatingEnum.ONE);
        creditRating.setDateOfRating(LocalDate.now());

        return creditRating;
    }

    private Client prepareClient() {
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        return client;
    }

    @Test
    void testCreateNewCreditRating_shouldCreateNewCreditRating() throws ClientNotFoundException {
        // given
        Client client = prepareClient();
        CreditRatingEnum creditRatingEnum = CreditRatingEnum.ONE;

        BigDecimal customerMonthlyIncome = BigDecimal.valueOf(1500.00);
        BigDecimal customerMonthlyExpenses = BigDecimal.valueOf(150.33);

        when(creditRatingEvaluator
                .getCreditRatingEnum(any(),any(BigDecimal.class),any(BigDecimal.class)))
                .thenReturn(creditRatingEnum);
        when(clientService.getClientById(any())).thenReturn(client);
        when(creditRatingMapper.mapToCreditRatingDto(any(CreditRating.class))).thenCallRealMethod();

        // when
        CreditRatingDto creditRatingDto = creditRatingFacade
                .createNewCreditRating(client.getClientId(),customerMonthlyIncome,customerMonthlyExpenses);

        // then
        verify(clientService, times(1)).saveClient(any(Client.class));

        assertEquals(CreditRatingEnum.ONE, creditRatingDto.getCreditRating());
        assertEquals(LocalDate.now(), creditRatingDto.getDateOfRating());
    }
}