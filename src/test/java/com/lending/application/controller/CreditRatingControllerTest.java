package com.lending.application.controller;

import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.domain.credit.rating.CreditRatingResponseDTO;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.facade.CreditRatingFacade;
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
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(CreditRatingController.class)
class CreditRatingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CreditRatingFacade creditRatingFacade;

    private final String PATH = "/lending/credit";

    private CreditRatingResponseDTO prepareCreditRatingDto() {
        return new CreditRatingResponseDTO(1L, CreditRatingEnum.TWO, LocalDate.now());
    }

    private LinkedMultiValueMap<String, String> prepareParamsMap(
            final BigDecimal income,
            final BigDecimal expenses
    ) {
        LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
        linkedMultiValueMap.add("income", income.toString());
        linkedMultiValueMap.add("expenses", expenses.toString());

        return linkedMultiValueMap;
    }

    @Nested
    class CreateCreditRating {
        @Test
        void create_credit_rating_should_return_http_status_not_found_404_if_client_not_found()
                throws Exception {
            // given
            Long clientId = 999L;
            BigDecimal income = BigDecimal.TEN;
            BigDecimal expenses = BigDecimal.TEN;

            LinkedMultiValueMap<String, String> linkedMultiValueMap = prepareParamsMap(income, expenses);

            when(creditRatingFacade
                    .createNewCreditRating(any(), any(BigDecimal.class), any(BigDecimal.class)))
                    .thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(PATH + "/{clientId}", clientId)
                            .params(linkedMultiValueMap))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void create_credit_rating_should_return_http_status_bad_request_400_if_client_id_is_below_min()
                throws Exception {
            Long clientId = 0L;
            BigDecimal income = BigDecimal.TEN;
            BigDecimal expenses = BigDecimal.TEN;

            LinkedMultiValueMap<String, String> linkedMultiValueMap = prepareParamsMap(income, expenses);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(PATH + "/clientId", clientId)
                            .params(linkedMultiValueMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_credit_rating_should_return_http_status_bad_request_400_if_income_is_below_min()
                throws Exception {
            Long clientId = 1L;
            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expenses = BigDecimal.TEN;

            LinkedMultiValueMap<String, String> linkedMultiValueMap = prepareParamsMap(income, expenses);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(PATH + "/{clientId}", clientId)
                            .params(linkedMultiValueMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_credit_rating_should_return_http_status_bad_request_400_if_expenses_are_below_min()
                throws Exception {
            Long clientId = 1L;
            BigDecimal income = BigDecimal.TEN;
            BigDecimal expenses = BigDecimal.ZERO;

            LinkedMultiValueMap<String, String> linkedMultiValueMap = prepareParamsMap(income, expenses);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(PATH + "/{clientId}", clientId)
                            .params(linkedMultiValueMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_credit_rating_should_return_http_status_ok_200_and_credit_rating_dto_if_succeed()
                throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal income = BigDecimal.valueOf(100.00);
            BigDecimal expenses = BigDecimal.valueOf(20.00);

            LinkedMultiValueMap<String, String> linkedMultiValueMap = prepareParamsMap(income, expenses);

            CreditRatingResponseDTO creditRatingResponseDto = prepareCreditRatingDto();

            when(creditRatingFacade
                    .createNewCreditRating(clientId, income, expenses))
                    .thenReturn(creditRatingResponseDto);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(PATH + "/{clientId}", clientId)
                            .params(linkedMultiValueMap))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.ratingId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.creditRating", Matchers.is("TWO")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfRating", Matchers.is(LocalDate.now().toString())));
        }
    }

    @Nested
    class GetCreditRating {
        @Test
        void get_credit_rating_should_return_http_status_not_found_404_if_client_not_found()
                throws Exception {
            // given
            Long clientId = 1L;

            when(creditRatingFacade.getCreditRating(clientId)).thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .get(PATH + "/{clientId}", clientId))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void get_credit_rating_should_return_http_status_bad_request_400_if_client_id_is_below_min()
                throws Exception {
            // given
            Long clientId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .get(PATH + "/{clientId}", clientId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void get_credit_rating_should_return_http_status_ok_200_if_succeed()
                throws Exception {
            // given
            Long clientId = 1L;
            CreditRatingResponseDTO creditRatingResponseDto = prepareCreditRatingDto();

            when(creditRatingFacade.getCreditRating(clientId)).thenReturn(creditRatingResponseDto);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .get(PATH + "/{clientId}", clientId))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.ratingId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.creditRating", Matchers.is("TWO")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfRating", Matchers.is(LocalDate.now().toString())));
        }

    }

    @Nested
    class DeleteCreditRating {
        @Test
        void delete_credit_rating_should_return_http_status_not_found_404_if_credit_rating_is_not_found()
                throws Exception {
            // given
            Long creditRatingId = 1L;

            willThrow(CreditRatingNotFoundException.class).given(creditRatingFacade).deleteCreditRating(any());

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete(PATH + "/{creditRatingId}", creditRatingId))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void delete_credit_rating_should_return_http_status_bad_request_400_if_credit_rating_id_is_below_min()
                throws Exception {
            // given
            Long creditRatingId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete(PATH + "/{creditRatingId}", creditRatingId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void delete_credit_rating_should_return_http_status_ok_200_if_succeed()
                throws Exception {
            // given
            Long creditRatingId = 1L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete(PATH + "/{creditRatingId}", creditRatingId))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }
}