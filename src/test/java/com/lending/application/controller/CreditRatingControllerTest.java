package com.lending.application.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lending.application.controller.json.LocalDateSerializer;
import com.lending.application.domain.CreditRatingEnum;
import com.lending.application.domain.dto.CreditRatingDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.exception.CreditRatingNotFoundException;
import com.lending.application.service.credit.rating.CreditRatingFacade;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.*;

@SpringJUnitWebConfig
@WebMvcTest(CreditRatingController.class)
class CreditRatingControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CreditRatingFacade creditRatingFacade;

    private final String PATH = "/lending/credit";

    private CreditRatingDto prepareCreditRatingDto() {
        return new CreditRatingDto(1L, CreditRatingEnum.TWO, LocalDate.now());
    }

    @Test
    void testCreateCreditRating_shouldThrowClientNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal income = BigDecimal.valueOf(100.00);
        BigDecimal expenses = BigDecimal.valueOf(50.00);

        when(creditRatingFacade
                .createNewCreditRating(any(), any(BigDecimal.class), any(BigDecimal.class)))
                .thenThrow(ClientNotFoundException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                .post(PATH + "/client/{clientId}/income/{income}/expenses/{expenses}",clientId,income,expenses))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testCreateCreditRating_shouldReturnCreditRatingSucceedHttp200() throws Exception {
        // given
        Long clientId = 1L;
        BigDecimal income = BigDecimal.valueOf(100.00);
        BigDecimal expenses = BigDecimal.valueOf(20.00);

        CreditRatingDto creditRatingDto = prepareCreditRatingDto();

        when(creditRatingFacade
                .createNewCreditRating(clientId,income,expenses))
                .thenReturn(creditRatingDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                .post(PATH + "/client/{clientId}/income/{income}/expenses/{expenses}",clientId,income,expenses))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ratingId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creditRating", Matchers.is("TWO")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfRating", Matchers.is(LocalDate.now().toString())));
    }

    @Test
    void testGetCreditRating_shouldThrowClientNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;

        when(creditRatingFacade.getCreditRating(clientId)).thenThrow(ClientNotFoundException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{clientId}", clientId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetCreditRating_SucceedHttp200() throws Exception {
        // given
        Long clientId = 1L;
        CreditRatingDto creditRatingDto = prepareCreditRatingDto();

        when(creditRatingFacade.getCreditRating(clientId)).thenReturn(creditRatingDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(PATH + "/{clientId}", clientId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ratingId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creditRating", Matchers.is("TWO")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfRating", Matchers.is(LocalDate.now().toString())));
    }

    @Test
    void updateCreditRating_shouldThrowCreditRatingNotFoundException404() throws Exception {
        // given
        CreditRatingDto creditRatingDto = prepareCreditRatingDto();

        willThrow(CreditRatingNotFoundException.class).given(creditRatingFacade).updateCreditRating(any());

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String jsonContent = gson.toJson(creditRatingDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void updateCreditRating_SucceedHttp200() throws Exception {
        // given
        CreditRatingDto creditRatingDto = new CreditRatingDto();
        creditRatingDto.setRatingId(1L);
        creditRatingDto.setDateOfRating(LocalDate.now());
        creditRatingDto.setCreditRating(CreditRatingEnum.FIVE);

        when(creditRatingFacade.updateCreditRating(any(CreditRatingDto.class))).thenReturn(creditRatingDto);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String jsonContent = gson.toJson(creditRatingDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put(PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.ratingId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dateOfRating", Matchers.is(LocalDate.now().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creditRating", Matchers.is("FIVE")));
    }

    @Test
    void testDeleteCreditRating_SucceedHttp200() throws Exception {
        // given
        Long creditRatingId = 1L;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(PATH+ "/{creditRatingId}", creditRatingId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteCreditRating_CreditRatingNotFoundException404() throws Exception {
        // given
        Long creditRatingId = 1L;

        willThrow(CreditRatingNotFoundException.class).given(creditRatingFacade).deleteCreditRating(any());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(PATH + "/{creditRatingId}", creditRatingId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}