package com.lending.application.controller;

import com.google.gson.Gson;
import com.lending.application.domain.dto.ClientDto;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.facade.ClientServiceFacade;
import com.lending.application.service.client.ClientService;
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
    MockMvc mockMvc;
    @MockBean
    ClientService clientService;
    @MockBean
    ClientServiceFacade clientServiceFacade;

    @Test
    void testGetClientById_shouldThrowClientNotFoundExceptionAndHttp404() throws  Exception {
        // given
        when(clientService.getClientById(any())).thenThrow(new ClientNotFoundException());

        // when & then
        mockMvc.perform((MockMvcRequestBuilders
                    .get("/lending/client/{clientId}", 1L)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void testGetClientById_shouldReturnOneClientAndHttp200() throws Exception {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "Client",
                "Last Name",
                null,
                null,
                null
        );
        when(clientService.getClientById(1L)).thenReturn(clientDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/lending/client/{clientId}",1L)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Client")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Last Name")));
    }

    @Test
    void testShouldReturnEmptyClientListAndHttp200() throws Exception {
        // given
        when(clientService.getAllClients()).thenReturn(List.of());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/lending/client")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void testGetAllClients_shouldReturnListOfClientsAndHttp200() throws Exception {
        // given
        ClientDto clientDto1 = new ClientDto(
                1L,
                "Client 1",
                "Last Name 1",
                null,
                null,
                null
        );
        ClientDto clientDto2 = new ClientDto(
                2L,
                "Client 2",
                "Last Name 2",
                null,
                null,
                null
        );

        List<ClientDto> clientDtoList = List.of(clientDto1,clientDto2);

        when(clientService.getAllClients()).thenReturn(clientDtoList);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/lending/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Client 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.is("Last Name 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientId", Matchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Client 2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.is("Last Name 2")));
    }

    @Test
    void testAddNewClient_shouldReturnHttp201() throws Exception {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "Client 1",
                "Last name",
                null,
                null,
                null
        );

        Gson gson = new Gson();
        String jsonContent = gson.toJson(clientDto);

        when(clientServiceFacade.CreateNewClient(any(ClientDto.class))).thenReturn(clientDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/lending/client")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(jsonContent))
               .andExpect(MockMvcResultMatchers.status().isCreated())
               .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("Client 1")))
               .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Last name")));

        verify(clientServiceFacade, times(1)).CreateNewClient(any(ClientDto.class));
    }

    @Test
    void testAddNewClient_shouldReturnHttp400() throws Exception {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "Client 1",
                null,
                null,
                null,
                null
        );

        Gson gson = new Gson();
        String gsonContent = gson.toJson(clientDto);

        willThrow(new DataIntegrityViolationException("")).given(clientService).createClient(any());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/lending/client")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(gsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void testDeleteClientById_shouldReturnHttp200() throws Exception {
        // given & when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/lending/client/{clientId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(clientService, times(1)).deleteClientById(any());
    }

    @Test
    void testDeleteClientById_shouldReturnAndHttp404() throws Exception {
        // given
        willThrow(new ClientNotFoundException()).given(clientService).deleteClientById(any());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .delete("/lending/client/{clientId}", 1L))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(clientService, times(1)).deleteClientById(any());

    }

    @Test
    void testUpdateClient_shouldReturnUpdatedClientAndHttp200() throws Exception {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "updated client",
                "updated last name",
                null,
                null,
                null
        );

        Gson gson = new Gson();
        String gsonContent = gson.toJson(clientDto);

        when(clientService.updateClient(any(ClientDto.class))).thenReturn(clientDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/lending/client")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(gsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("updated client")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("updated last name")));
        verify(clientService, times(1)).updateClient(any(ClientDto.class));
    }

    @Test
    void testUpdateClient_shouldThrowClientNotFoundExceptionAndHttp404() throws Exception {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "updated client",
                "updated last name",
                null,
                null,
                null
        );

        Gson gson = new Gson();
        String gsonContent = gson.toJson(clientDto);

        when(clientService.updateClient(any(ClientDto.class))).thenThrow(new ClientNotFoundException());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                    .put("/lending/client")
                    .contentType(MediaType.APPLICATION_JSON)
                    .characterEncoding(StandardCharsets.UTF_8)
                    .content(gsonContent))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        verify(clientService, times(1)).updateClient(any(ClientDto.class));
    }

    @Test
    void testUpdateClient_DataIntegrityViolationExceptionAndHttp400() throws Exception {
        // given
        ClientDto clientDto = new ClientDto(
                1L,
                "updated client",
                null,
                null,
                null,
                null
        );

        Gson gson = new Gson();
        String gsonContent = gson.toJson(clientDto);

        when(clientService.updateClient(any(ClientDto.class))).thenThrow(new DataIntegrityViolationException(""));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/lending/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(gsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(clientService, times(1)).updateClient(any(ClientDto.class));
    }
}