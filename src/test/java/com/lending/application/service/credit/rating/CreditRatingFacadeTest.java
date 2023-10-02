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
import org.apache.el.stream.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void getCreditRating_shouldReturnCreditRating() throws ClientNotFoundException {
        // given
        CreditRating creditRating = prepareCreditRating();
        Client client = prepareClient();
        client.setCreditRating(creditRating);

        when(clientService.getClientById(any())).thenReturn(client);
        when(creditRatingMapper.mapToCreditRatingDto(any(CreditRating.class))).thenCallRealMethod();

        // when
        CreditRatingDto retrievedRatingDto = creditRatingFacade.getCreditRating(1L);

        // then
        assertEquals(CreditRatingEnum.ONE, retrievedRatingDto.getCreditRating());
        assertEquals(LocalDate.now(), retrievedRatingDto.getDateOfRating());
    }

    @Test
    void updateCreditRating_shouldReturnUpdatedCreditRating() throws CreditRatingNotFoundException {
        // given
        CreditRatingDto creditRatingDto = new CreditRatingDto();
        creditRatingDto.setRatingId(1L);
        creditRatingDto.setCreditRating(CreditRatingEnum.FIVE);
        creditRatingDto.setDateOfRating(LocalDate.of(2023,1,1));


        CreditRating creditRating = new CreditRating();
        creditRating.setRatingId(1L);
        creditRating.setCreditRating(CreditRatingEnum.FIVE);
        creditRating.setDateOfRating(LocalDate.of(2023,1,1));

        when(creditRatingMapper.mapToCreditRating(any(CreditRatingDto.class))).thenCallRealMethod();
        when(creditRatingService.updateCreditRatingById(any(CreditRating.class))).thenReturn(creditRating);
        when(creditRatingMapper.mapToCreditRatingDto(any(CreditRating.class))).thenCallRealMethod();

        // when
        CreditRatingDto updatedCreditRatingDto = creditRatingFacade.updateCreditRating(creditRatingDto);

        // then
        assertEquals(creditRatingDto.getRatingId(), updatedCreditRatingDto.getRatingId());
        assertEquals(creditRatingDto.getCreditRating(), updatedCreditRatingDto.getCreditRating());
        assertEquals(creditRatingDto.getDateOfRating(), updatedCreditRatingDto.getDateOfRating());
    }

    @Test
    void deleteCreditRating() throws CreditRatingNotFoundException {
        // given
        Long ratingId = 1L;

        // when
        creditRatingFacade.deleteCreditRating(ratingId);

        // then
        verify(creditRatingService,times(1)).deleteCreditRatingById(any());
    }
}