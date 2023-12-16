package com.lending.application.controller;

import com.lending.application.exception.AccountNotFoundException;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.InsufficientFundsException;
import com.lending.application.facade.AccountServiceFacade;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class Balance {
        @Test
        void get_account_balance_should_return_http_status_not_found_404_if_client_not_found() throws Exception {
            // given
            Long clientId = 999L;

            when(accountServiceFacade.getBalance(any())).thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/balance")
                            .param("clientId", clientId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void get_account_balance_should_return_http_status_not_found_404_if_account_not_found() throws Exception {
            // given
            Long clientId = 1L;

            when(accountServiceFacade.getBalance(clientId)).thenThrow(AccountNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/balance")
                            .param("clientId", clientId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void get_account_balance_should_return_http_status_bad_request_400_if_param_clientId_is_below_min()
                throws Exception {

            // given
            Long clientId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/balance")
                            .param("clientId", clientId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void get_account_balance_should_return_http_status_ok_200_if_get_balance_succeed() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal balance = BigDecimal.valueOf(22.50);
            BigDecimal expectedBalance = BigDecimal.valueOf(22.50);

            when(accountServiceFacade.getBalance(any())).thenReturn(balance);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(
                                    PATH + "/balance")
                            .param("clientId", clientId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").value(expectedBalance));
        }
    }

    @Nested
    class Deposit {
        @Test
        void deposit_should_return_http_status_not_found_404_if_client_not_found() throws Exception {
            // given
            Long clientId = 999L;
            BigDecimal depositAmount = BigDecimal.valueOf(200.00);

            when(accountServiceFacade.deposit(clientId,depositAmount)).thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(
                                    PATH + "/deposit")
                            .param("clientId", clientId.toString())
                            .param("deposit", depositAmount.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void deposit_should_return_http_status_bad_request_400_if_param_client_id_is_below_min() throws Exception {

            // given
            Long clientId = 0L;
            BigDecimal depositAmount = BigDecimal.ONE;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/deposit")
                            .param("clientId", clientId.toString())
                            .param("deposit", depositAmount.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void deposit_should_return_http_status_bad_request_400_if_param_deposit_is_below_min() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal deposit = BigDecimal.ZERO;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/deposit")
                            .param("clientId", clientId.toString())
                            .param("deposit", deposit.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void deposit_should_return_http_status_bad_request_400_if_param_deposit_is_above_max() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal deposit = new BigDecimal("10001");

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/deposit")
                            .param("clientId", clientId.toString())
                            .param("deposit", deposit.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void deposit_should_return_http_status_not_found_404_if_account_is_not_found() throws Exception {
            // given
            Long clientId = 999L;
            BigDecimal deposit = BigDecimal.valueOf(200.00);

            when(accountServiceFacade.deposit(clientId,deposit)).thenThrow(AccountNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(
                                    PATH + "/deposit")
                            .param("clientId", clientId.toString())
                            .param("deposit", deposit.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void deposit_should_return_http_status_ok_200_if_deposit_succeed() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal balance = BigDecimal.valueOf(250.00);
            BigDecimal deposit = BigDecimal.valueOf(20.00);
            BigDecimal expectedBalance = BigDecimal.valueOf(250.00);

            when(accountServiceFacade.deposit(clientId,deposit)).thenReturn(balance);

            // when
            mockMvc.perform(MockMvcRequestBuilders.put(
                                    PATH + "/deposit")
                            .param("clientId", clientId.toString())
                            .param("deposit", deposit.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").value(expectedBalance));

            verify(accountServiceFacade,times(1)).deposit(any(),any(BigDecimal.class));
        }
    }

    @Nested
    class Withdraw {
        @Test
        void withdraw_should_return_http_status_not_found_404_if_client_is_not_found() throws Exception {
            // given
            Long clientId = 999L;
            BigDecimal withdraw = BigDecimal.valueOf(20.00);

            when(accountServiceFacade.withdraw(clientId,withdraw)).thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(
                                    PATH + "/withdraw")
                            .param("clientId", clientId.toString())
                            .param("withdraw", withdraw.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void withdraw_should_return_http_status_bad_request_400_if_param_clientId_is_below_min() throws Exception {
            // given
            Long clientId = 0L;
            BigDecimal withdraw = BigDecimal.ONE;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/withdraw")
                            .param("clientId", clientId.toString())
                            .param("withdraw", withdraw.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void withdraw_should_return_http_status_bad_request_400_if_param_withdraw_is_below_min() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal withdraw = BigDecimal.ZERO;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/withdraw")
                            .param("clientId", clientId.toString())
                            .param("withdraw", withdraw.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void withdraw_should_return_http_status_bad_request_400_if_param_withdraw_is_above_max() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal withdraw = BigDecimal.valueOf(10001);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/withdraw")
                            .param("clientId", clientId.toString())
                            .param("withdraw", withdraw.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void withdraw_should_return_http_status_payment_required_402_if_account_balance_is_below_withdraw()
                throws Exception {

            // given
            Long clientId = 1L;
            BigDecimal withdraw = BigDecimal.valueOf(20.00);
            when(accountServiceFacade.withdraw(clientId,withdraw)).thenThrow(InsufficientFundsException.class);

            // when
            mockMvc.perform(MockMvcRequestBuilders.put(PATH + "/withdraw")
                            .param("clientId", clientId.toString())
                            .param("withdraw", withdraw.toString()))
                    .andExpect(MockMvcResultMatchers.status().isPaymentRequired());
        }

        @Test
        void withdraw_should_return_http_status_ok_200_if_withdraw_succeed() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal withdrawAmount = BigDecimal.valueOf(20.00);
            BigDecimal balance = BigDecimal.valueOf(200.00);
            BigDecimal expectedBalance = BigDecimal.valueOf(200.00);

            when(accountServiceFacade.withdraw(any(),any(BigDecimal.class))).thenReturn(balance);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.put(
                                    PATH + "/withdraw")
                            .param("clientId", clientId.toString())
                            .param("withdraw", withdrawAmount.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$").value(expectedBalance));
        }
    }
}