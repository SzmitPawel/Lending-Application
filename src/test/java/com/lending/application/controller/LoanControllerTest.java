package com.lending.application.controller;

import com.lending.application.domain.dto.LoanDto;
import com.lending.application.exception.*;
import com.lending.application.facade.LoanServiceFacade;
import com.lending.application.service.loan.dto.LoanCalculationDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringJUnitWebConfig
@WebMvcTest(LoanController.class)
class LoanControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LoanServiceFacade loanServiceFacade;

    private final String PATH = "/lending/loan";

    private LinkedMultiValueMap<String,String> prepareParamsMap(
            final Long clientId,
            final BigDecimal loanAmount,
            final int months) {

        LinkedMultiValueMap<String,String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("clientId",clientId.toString());
        paramsMap.add("amount",loanAmount.toString());
        paramsMap.add("months",Integer.toString(months));

        return paramsMap;
    }

    private LinkedMultiValueMap<String,String> prepareParamsMap(
            final BigDecimal loanAmount,
            final int months) {

        LinkedMultiValueMap<String,String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("amount",loanAmount.toString());
        paramsMap.add("months",Integer.toString(months));

        return paramsMap;
    }

    @Nested
    class GetByIdEndpoint {
        @Test
        void get_loan_by_id_should_return_http_status_not_found_404_if_loan_not_found() throws Exception {
            // given
            Long loanId = 999L;

            when(loanServiceFacade.getLoanById(anyLong())).thenThrow(LoanNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                            .param("loanId", loanId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void get_loan_by_id_should_return_http_status_bad_request_400_if_param_is_below_min_valid() throws Exception {
            // given
            Long loanId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                            .param("loanId", loanId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void get_loan_by_id_should_return_http_status_ok_200() throws Exception {
            // given
            LoanDto loanDto = new LoanDto();
            loanDto.setLoanId(1L);
            loanDto.setLoanAmount(BigDecimal.valueOf(1000.00));
            loanDto.setLoanStartDate(LocalDate.now());
            loanDto.setInterest(5.75F);
            loanDto.setMonthlyPayment(BigDecimal.valueOf(150.00));
            loanDto.setRepaymentPeriod(12);

            when(loanServiceFacade.getLoanById(anyLong())).thenReturn(loanDto);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH)
                            .param("loanId", loanDto.getLoanId().toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.loanAmount", Matchers.is(1000.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyPayment", Matchers.is(150.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.interest", Matchers.is(5.75)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.loanStartDate", Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.repaymentPeriod", Matchers.is(12)));
        }
    }

    @Nested
    class GetAllClientLoansEndpoint {

        @Test
        void get_all_client_loans_should_return_http_status_not_found_404_if_client_not_found() throws Exception {
            // given
            Long clientId = 999L;

            when(loanServiceFacade.getAllClientLoans(clientId)).thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/all")
                            .param("clientId", clientId.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void get_all_client_loans_should_return_http_status_ok_200() throws Exception {
            // given
            Long clientId = 1L;

            LoanDto loanDto1 = new LoanDto();
            loanDto1.setLoanId(1L);
            loanDto1.setLoanAmount(BigDecimal.valueOf(1000.00));
            loanDto1.setInterest(3.75F);
            loanDto1.setLoanStartDate(LocalDate.now());
            loanDto1.setRepaymentPeriod(11);
            loanDto1.setMonthlyPayment(BigDecimal.valueOf(150.00));

            LoanDto loanDto2 = new LoanDto();
            loanDto2.setLoanId(2L);
            loanDto2.setLoanAmount(BigDecimal.valueOf(2000.00));
            loanDto2.setInterest(4.75F);
            loanDto2.setLoanStartDate(LocalDate.now());
            loanDto2.setRepaymentPeriod(12);
            loanDto2.setMonthlyPayment(BigDecimal.valueOf(250.00));

            List<LoanDto> loanDtoList = List.of(loanDto1,loanDto2);

            when(loanServiceFacade.getAllClientLoans(clientId)).thenReturn(loanDtoList);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/all")
                            .param("clientId",clientId.toString())
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].loanAmount", Matchers.is(1000.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].monthlyPayment", Matchers.is(150.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].interest", Matchers.is(3.75)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].loanStartDate", Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].repaymentPeriod", Matchers.is(11)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].loanAmount", Matchers.is(2000.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].monthlyPayment", Matchers.is(250.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].interest", Matchers.is(4.75)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].loanStartDate", Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].repaymentPeriod", Matchers.is(12)));
        }
    }

    @Nested
    class CalculateLoanEndpoint {
        @Test
        void calculate_loan_should_return_http_status_bad_request_400_if_amount_is_zero() throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.ZERO;
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            when(loanServiceFacade.calculateLoan(loanAmount,months)).thenThrow(InvalidLoanAmountOfCreditException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void calculate_loan_should_return_http_status_bad_request_400_if_months_are_zero() throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 0;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            when(loanServiceFacade.calculateLoan(loanAmount,months)).thenThrow(InvalidLoanMonthsException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void calculate_loan_should_return_http_status_internal_server_error_500_if_can_not_connect_to_web()
                throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            when(loanServiceFacade.calculateLoan(loanAmount,months)).thenThrow(IOException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError());
        }

        @Test
        void calculate_loan_should_return_http_status_bad_request_400_if_amount_is_below_min_valid() throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.ZERO;
            int months = 10;

            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void calculate_loan_should_return_http_status_bad_request_400_if_amount_is_above_max_valid() throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.valueOf(100001);
            int months = 10;

            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void calculate_loan_should_return_http_status_bad_request_400_if_months_are_below_min_valid() throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 0;

            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void calculate_loan_should_return_http_status_bad_request_400_if_months_are_above_max_valid() throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.valueOf(100001);
            int months = 121;

            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void calculate_loan_should_return_http_status_ok_200() throws Exception {
            // given
            BigDecimal loanAmount = BigDecimal.valueOf(1000.00);
            BigDecimal monthlyPayment = BigDecimal.valueOf(120.00);
            BigDecimal interestRate = BigDecimal.valueOf(3.75);
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(loanAmount,months);

            LoanCalculationDto loanCalculationDto = new LoanCalculationDto();
            loanCalculationDto.setMonthlyPayment(monthlyPayment);
            loanCalculationDto.setInterestRate(interestRate);
            loanCalculationDto.setAmountOfCredit(loanAmount);
            loanCalculationDto.setNumberOfMonths(months);

            when(loanServiceFacade.calculateLoan(loanAmount,months)).thenReturn(loanCalculationDto);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/calculate")
                            .params(paramsMap)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.amountOfCredit",Matchers.is(1000.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.interestRate",Matchers.is(3.75)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.numberOfMonths",Matchers.is(10)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyPayment",Matchers.is(120.00)));
        }
    }

    @Nested
    class CreateLoanEndpoint {
        @Test
        void create_loan_should_return_http_status_not_found_404_if_client_not_found() throws Exception {
            // given
            Long clientId = 999L;
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(clientId,loanAmount,months);

            when(loanServiceFacade.createNewLoan(clientId,loanAmount,months)).thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_amount_is_zero() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal loanAmount = BigDecimal.ZERO;
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(clientId,loanAmount,months);

            when(loanServiceFacade.createNewLoan(clientId,loanAmount,months))
                    .thenThrow(InvalidLoanAmountOfCreditException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_months_are_zero() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 0;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(clientId,loanAmount,months);

            when(loanServiceFacade.createNewLoan(clientId,loanAmount,months)).thenThrow(InvalidLoanMonthsException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_rating_is_too_low() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(clientId,loanAmount,months);

            when(loanServiceFacade.createNewLoan(clientId,loanAmount,months)).thenThrow(LowCreditRatingException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_loan_should_return_http_status_internal_server_error_500_if_can_not_connect_to_web()
                throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(clientId,loanAmount,months);

            when(loanServiceFacade.createNewLoan(clientId,loanAmount,months)).thenThrow(IOException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isInternalServerError());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_clientId_is_below_min_valid() throws Exception {
            // given
            Long clientId = 0L;
            BigDecimal amount = BigDecimal.TEN;
            int months = 10;

            LinkedMultiValueMap <String,String> paramsMap = prepareParamsMap(clientId,amount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_amount_is_below_min_valid() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal amount = BigDecimal.ZERO;
            int months = 10;

            LinkedMultiValueMap <String,String> paramsMap = prepareParamsMap(clientId,amount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_amount_is_above_max_valid() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal amount = BigDecimal.valueOf(100001);
            int months = 10;

            LinkedMultiValueMap <String,String> paramsMap = prepareParamsMap(clientId,amount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_months_are_below_min_valid() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal amount = BigDecimal.TEN;
            int months = 0;

            LinkedMultiValueMap <String,String> paramsMap = prepareParamsMap(clientId,amount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_loan_should_return_http_status_bad_request_400_if_months_are_above_max_valid() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal amount = BigDecimal.TEN;
            int months = 121;

            LinkedMultiValueMap <String,String> paramsMap = prepareParamsMap(clientId,amount,months);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_Loan_should_return_http_status_ok_200() throws Exception {
            // given
            Long clientId = 1L;
            BigDecimal loanAmount = BigDecimal.TEN;
            int months = 10;
            LinkedMultiValueMap<String,String> paramsMap = prepareParamsMap(clientId,loanAmount,months);

            LoanDto loanDto = new LoanDto();
            loanDto.setLoanId(1L);
            loanDto.setLoanAmount(BigDecimal.valueOf(1000.00));
            loanDto.setInterest(3.75F);
            loanDto.setLoanStartDate(LocalDate.now());
            loanDto.setRepaymentPeriod(11);
            loanDto.setMonthlyPayment(BigDecimal.valueOf(150.00));

            when(loanServiceFacade.createNewLoan(clientId,loanAmount,months)).thenReturn(loanDto);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders.post(PATH + "/create")
                            .params(paramsMap)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.loanAmount",Matchers.is(1000.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.monthlyPayment",Matchers.is(150.00)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.interest",Matchers.is(3.75)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.loanStartDate",Matchers.is(LocalDate.now().toString())))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.repaymentPeriod",Matchers.is(11)));
        }
    }
}