package com.lending.application.facade;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.domain.credit.rating.CreditRatingResponseDTO;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.mapper.credit.rating.CreditRatingResponseMapper;
import com.lending.application.service.client.ClientService;
import com.lending.application.service.credit.rating.calculate.rating.CreditRatingEvaluator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import rating.CreditRatingResponseMapperImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditRatingFacadeTest {
    @InjectMocks
    private CreditRatingFacade creditRatingFacade;
    @Mock
    private CreditRatingResponseMapper responseMapper = new CreditRatingResponseMapperImpl();
    @Mock
    private CreditRatingEvaluator creditRatingEvaluator;
    @Mock
    private ClientService clientService;

    private Client prepareClient() {
        Client client = new Client();
        client.setClientId(1L);
        client.setName("John");
        client.setLastName("Doe");

        return client;
    }

    @Test
    void create_new_credit_rating_should_create_new_credit_rating_for_client()
            throws ClientNotFoundException {
        // given
        BigDecimal customerMonthlyIncome = BigDecimal.valueOf(1500.00);
        BigDecimal customerMonthlyExpenses = BigDecimal.valueOf(150.33);

        CreditRatingEnum creditRatingEnum = CreditRatingEnum.THREE;
        when(creditRatingEvaluator.getCreditRatingEnum(anyLong(), any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(creditRatingEnum);

        Client client = prepareClient();
        when(clientService.getClientById(anyLong())).thenReturn(client);

        // when
        creditRatingFacade.createNewCreditRating(client.getClientId(), customerMonthlyIncome, customerMonthlyExpenses);

        // then
        verify(clientService, times(1)).saveClient(any(Client.class));
        verify(responseMapper, times(1)).mapToCreditRatingDTO(any(CreditRating.class));

        assertEquals(creditRatingEnum, client.getCreditRating().getCreditRating());
        assertEquals(LocalDate.now(), client.getCreditRating().getDateOfRating());
    }
}