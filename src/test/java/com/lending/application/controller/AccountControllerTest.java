package com.lending.application.controller;

import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.facade.AccountServiceFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(AccountController.class)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountServiceFacade accountServiceFacade;

    private final String PATH = "/lending/account";

    @Test
    void testGetAccountBalance_ClientNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;

        when(accountServiceFacade.getBalance(any())).thenThrow(ClientNotFoundException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{clientId}",clientId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(accountServiceFacade,times(1)).getBalance(any());
    }

    @Test
    void testGetAccountBalance_AccountNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;

        when(accountServiceFacade.getBalance(any())).thenThrow(AccountNotFoundException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{clientId}",clientId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(accountServiceFacade,times(1)).getBalance(any());
    }

    @Test
    void testGetAccountBalance_SucceedHttp200() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal balance = BigDecimal.valueOf(22.50);
        BigDecimal expectedBalance = BigDecimal.valueOf(22.50);

        when(accountServiceFacade.getBalance(any())).thenReturn(balance);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(
                PATH + "/{clientId}",clientId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(expectedBalance));

        verify(accountServiceFacade,times(1)).getBalance(any());
    }

    @Test
    void testDeposit_ClientNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal depositAmount = BigDecimal.valueOf(200.00);

        when(accountServiceFacade.deposit(any(),any(BigDecimal.class))).thenThrow(ClientNotFoundException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(
                PATH + "/deposit/{clientId}/{deposit}",clientId,depositAmount))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(accountServiceFacade,times(1)).deposit(any(),any(BigDecimal.class));
    }

    @Test
    void testDeposit_AccountNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal depositAmount = BigDecimal.valueOf(200.00);

        when(accountServiceFacade.deposit(any(),any(BigDecimal.class))).thenThrow(AccountNotFoundException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(
                PATH + "/deposit/{clientId}/{deposit}",clientId,depositAmount))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(accountServiceFacade,times(1)).deposit(any(),any(BigDecimal.class));
    }

    @Test
    void testDeposit_SucceedHttp200() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal balance = BigDecimal.valueOf(250.00);
        BigDecimal depositAmount = BigDecimal.valueOf(20.00);
        BigDecimal expectedBalance = BigDecimal.valueOf(250.00);

        when(accountServiceFacade.deposit(any(),any(BigDecimal.class))).thenReturn(balance);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put(
                PATH + "/deposit/{clientId}/{deposit}",clientId, depositAmount))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(expectedBalance));

        verify(accountServiceFacade,times(1)).deposit(any(),any(BigDecimal.class));
    }

    @Test
    void testWithdraw_ClientNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(20.00);
        when(accountServiceFacade.withdraw(any(),any(BigDecimal.class))).thenThrow(ClientNotFoundException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(
                PATH + "/withdraw/{clientId}/{withdraw}",clientId,withdrawAmount))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(accountServiceFacade, times(1)).withdraw(any(),any(BigDecimal.class));
    }

    @Test
    void testWithdraw_InsufficientFundsExceptionHttp402() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(20.00);
        when(accountServiceFacade.withdraw(any(),any(BigDecimal.class))).thenThrow(InsufficientFundsException.class);

        // when
        mockMvc.perform(MockMvcRequestBuilders.put(
                PATH + "/withdraw/{clientId}/{withdraw}",clientId,withdrawAmount))
                .andExpect(MockMvcResultMatchers.status().isPaymentRequired());
    }

    @Test
    void testWithdraw_SucceedHttp200() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal withdrawAmount = BigDecimal.valueOf(20.00);
        BigDecimal balance = BigDecimal.valueOf(200.00);
        BigDecimal expectedBalance = BigDecimal.valueOf(200.00);

        when(accountServiceFacade.withdraw(any(),any(BigDecimal.class))).thenReturn(balance);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(
                PATH + "/withdraw/{clientId}/{withdraw}",clientId,withdrawAmount))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(expectedBalance));

        verify(accountServiceFacade,times(1)).withdraw(any(),any(BigDecimal.class));
    }
}