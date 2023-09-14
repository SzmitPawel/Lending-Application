package com.lending.application.controller;

import com.google.gson.Gson;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.service.client.ClientServiceFacade;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@SpringJUnitWebConfig
@WebMvcTest(ClientController.class)
class ClientControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClientServiceFacade clientServiceFacade;

    private final String PATH = "/lending/client";

    private List<ClientDto> generateClientsList() {
        ClientDto clientDto1 = new ClientDto(
                1L,
                "John 1",
                "Doe 1",
                null,
                null,
                null
        );
        ClientDto clientDto2 = new ClientDto(
                2L,
                "John 2",
                "Doe 2",
                null,
                null,
                null
        );

        return List.of(clientDto1,clientDto2);
    }

    private ClientDto generateClient() {
        return new ClientDto(
                1L,
                "John",
                "Doe",
                null,
                null,
                null
        );
    }

    @Test
    void testGetClientById_ClientNotFoundExceptionHttp404() throws Exception {
        // given
        Long clientId = 1L;

        when(clientServiceFacade.getClientById(any())).thenThrow(ClientNotFoundException.class);

        // when & then
        mockMvc.perform((MockMvcRequestBuilders
                        .get(PATH + "/{clientId}", clientId)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clientServiceFacade,times(1)).getClientById(any());
    }

    @Test
    void testGetClientById_succeedHttp200() throws Exception {
        // given
        Long clientId = 1L;
        ClientDto clientDto = generateClient();

        when(clientServiceFacade.getClientById(any())).thenReturn(clientDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .get(PATH + "/{clientId}",clientId)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")));

        verify(clientServiceFacade,times(1)).getClientById(any());
    }

    @Test
    void testGetAllClients_succeedHttp200() throws Exception {
        // given
        when(clientServiceFacade.getAllClients()).thenReturn(List.of());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .get(PATH)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));

        verify(clientServiceFacade,times(1)).getAllClients();
    }

    @Test
    void testGetAllClientsList_succeedHttp200() throws Exception {
        // given
        List<ClientDto> clientDtoList = generateClientsList();

        when(clientServiceFacade.getAllClients()).thenReturn(clientDtoList);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get(PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("John 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.is("Doe 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("John 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.is("Doe 2")));

        verify(clientServiceFacade,times(1)).getAllClients();
    }

    @Test
    void testCreateClient_DataIntegrityViolationExceptionHttp400() throws Exception {
        // given
        ClientDto clientDto = generateClient();

        when(clientServiceFacade.createClient(any(ClientDto.class))).thenThrow(DataIntegrityViolationException.class);

        Gson gson = new Gson();
        String jsonContent = gson.toJson(clientDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        verify(clientServiceFacade,times(1)).createClient(any(ClientDto.class));
    }

    @Test
    void testCreateClient_succeedHttp201() throws Exception {
        // given
        ClientDto clientDto = generateClient();

        Gson gson = new Gson();
        String jsonContent = gson.toJson(clientDto);

        when(clientServiceFacade.createClient(any(ClientDto.class))).thenReturn(clientDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .post(PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(jsonContent))
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
               .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")));

        verify(clientServiceFacade, times(1)).createClient(any(ClientDto.class));
    }

    @Test
    void testDeleteClientById_ClientNotFoundHttp404() throws Exception {
        // given
        Long clientId = 1L;

        willThrow(ClientNotFoundException.class).given(clientServiceFacade).deleteClientById(any());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(PATH + "/{clientId}", clientId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clientServiceFacade, times(1)).deleteClientById(any());
    }


    @Test
    void testDeleteClientById_succeedHttp200() throws Exception {
        // given
        Long clientId = 1L;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .delete(PATH + "/{clientId}", clientId))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(clientServiceFacade, times(1)).deleteClientById(any());
    }

    @Test
    void testUpdateClient_DataIntegrityViolationExceptionHttp400() throws Exception {
        // given
        ClientDto clientDto = generateClient();

        Gson gson = new Gson();
        String gsonContent = gson.toJson(clientDto);

        when(clientServiceFacade.updateClient(any(ClientDto.class))).thenThrow(DataIntegrityViolationException.class);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testUpdateClient_succeedHttp200() throws Exception {
        // given
        ClientDto clientDto = generateClient();

        Gson gson = new Gson();
        String gsonContent = gson.toJson(clientDto);

        when(clientServiceFacade.updateClient(any(ClientDto.class))).thenReturn(clientDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(gsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("updated John")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("updated Doe")));
    }
}