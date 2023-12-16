package com.lending.application.controller;

import com.lending.application.domain.TransactionMethodEnum;
import com.lending.application.domain.dto.TransactionDto;
import com.lending.application.facade.TransactionServiceFacade;
import org.hamcrest.Matchers;
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
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionServiceFacade transactionServiceFacade;

    private final String PATH = "/lending/transaction";

    private List<TransactionDto> prepareTransactionsList() {
        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setTransactionID(1L);
        transactionDto1.setTransactionAmount(BigDecimal.valueOf(100.00));
        transactionDto1.setTransactionDate(LocalDate.now());
        transactionDto1.setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);

        TransactionDto transactionDto2 = new TransactionDto();
        transactionDto2.setTransactionID(2L);
        transactionDto2.setTransactionAmount(BigDecimal.valueOf(200.00));
        transactionDto2.setTransactionDate(LocalDate.now());
        transactionDto2.setTransactionMethodEnum(TransactionMethodEnum.DEPOSIT);

        TransactionDto transactionDto3 = new TransactionDto();
        transactionDto3.setTransactionID(3L);
        transactionDto3.setTransactionAmount(BigDecimal.valueOf(300.00));
        transactionDto3.setTransactionDate(LocalDate.now());
        transactionDto3.setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);

        TransactionDto transactionDto4 = new TransactionDto();
        transactionDto4.setTransactionID(4L);
        transactionDto4.setTransactionAmount(BigDecimal.valueOf(400.00));
        transactionDto4.setTransactionDate(LocalDate.now());
        transactionDto4.setTransactionMethodEnum(TransactionMethodEnum.DEPOSIT);

        return List.of(transactionDto1,transactionDto2,transactionDto3,transactionDto4);
    }

    private List<TransactionDto> prepareTransactionsListOneMethod() {
        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setTransactionID(1L);
        transactionDto1.setTransactionAmount(BigDecimal.valueOf(100.00));
        transactionDto1.setTransactionDate(LocalDate.now());
        transactionDto1.setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);

        TransactionDto transactionDto2 = new TransactionDto();
        transactionDto2.setTransactionID(2L);
        transactionDto2.setTransactionAmount(BigDecimal.valueOf(200.00));
        transactionDto2.setTransactionDate(LocalDate.now());
        transactionDto2.setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);

        return List.of(transactionDto1,transactionDto2);
    }

    @Nested
    class All {
        @Test
        void get_all_account_transactions_should_return_http_status_bad_request_400_if_param_accountId_is_below_min()
                throws Exception {

            // given
            Long accountId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/all")
                            .param("accountId", accountId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void get_all_account_transactions_should_return_http_status_ok_200_and_transaction_dto_list_if_succeed()
                throws Exception {

            // given
            List<TransactionDto> transactionDtoList = prepareTransactionsList();
            Long accountId = 1L;

            when(transactionServiceFacade.getAllAccountTransactions(accountId)).thenReturn(transactionDtoList);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/all")
                            .param("accountId", accountId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(4)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionID",Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionAmount",Matchers.is(100.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionDate",Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionMethodEnum",Matchers.is("WITHDRAWAL")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionID",Matchers.is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionAmount",Matchers.is(200.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionDate",Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionMethodEnum",Matchers.is("DEPOSIT")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].transactionID",Matchers.is(3)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].transactionAmount",Matchers.is(300.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].transactionDate",Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[2].transactionMethodEnum",Matchers.is("WITHDRAWAL")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].transactionID",Matchers.is(4)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].transactionAmount",Matchers.is(400.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].transactionDate",Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[3].transactionMethodEnum",Matchers.is("DEPOSIT")));;
        }
    }

    @Nested
    class Find {
        @Test
        void get_all_transactions_by_method_should_return_http_status_bad_request_400_if_param_accountId_is_below_min()
                throws Exception {

            // given
            Long accountId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/find")
                            .param("accountId", accountId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void get_all_transactions_by_method_should_return_http_status_ok_200_and_transaction_dto_list_if_succeed()
                throws Exception {

            // given
            List<TransactionDto> transactionDtoList = prepareTransactionsListOneMethod();
            Long accountId = 1L;
            TransactionMethodEnum method = TransactionMethodEnum.WITHDRAWAL;

            when(transactionServiceFacade
                    .getAllTransactionsByMethod(method,accountId))
                    .thenReturn(transactionDtoList);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/find")
                            .param("method", method.toString())
                            .param("accountId", accountId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$",Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionID",Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionAmount",Matchers.is(100.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionDate",Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionMethodEnum",Matchers.is("WITHDRAWAL")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionID",Matchers.is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionAmount",Matchers.is(200.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionDate",Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].transactionMethodEnum",Matchers.is("WITHDRAWAL")));
        }
    }
}