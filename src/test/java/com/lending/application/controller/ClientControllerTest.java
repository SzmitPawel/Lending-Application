package com.lending.application.controller;

import com.google.gson.Gson;
import com.lending.application.domain.client.Client;
import com.lending.application.domain.client.ClientRequestDTO;
import com.lending.application.domain.client.ClientResponseDTO;
import com.lending.application.exception.ClientNotFoundException;
import com.lending.application.facade.ClientServiceFacade;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
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

    private List<ClientResponseDTO> generateClientsResponseList() {
        ClientResponseDTO clientResponseDTO1 = new ClientResponseDTO();
        clientResponseDTO1.setClientId(1L);
        clientResponseDTO1.setName("John_1");
        clientResponseDTO1.setLastName("Doe_1");

        ClientResponseDTO clientResponseDTO2 = new ClientResponseDTO();
        clientResponseDTO2.setClientId(2L);
        clientResponseDTO2.setName("John_2");
        clientResponseDTO2.setLastName("Doe_2");

        return List.of(clientResponseDTO1, clientResponseDTO2);
    }

    private ClientRequestDTO generateClientRequest() {
        ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
        clientRequestDTO.setName("John");
        clientRequestDTO.setLastName("Doe");

        return clientRequestDTO;
    }

    private ClientResponseDTO generateClientResponse() {
        ClientResponseDTO clientResponseDTO = new ClientResponseDTO();
        clientResponseDTO.setClientId(1L);
        clientResponseDTO.setName("John");
        clientResponseDTO.setLastName("Doe");

        return clientResponseDTO;
    }

    @Nested
    class GetClient {
        @Test
        void get_client_by_id_should_return_http_status_not_found_404_if_client_not_found()
                throws Exception {
            // given
            Long clientId = 999L;

            when(clientServiceFacade.getClientById(any())).thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform((MockMvcRequestBuilders
                            .get(PATH + "/{clientId}", clientId)))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void get_client_by_id_should_return_http_status_bad_request_400_if_param_client_id_is_below_min()
                throws Exception {
            // given
            Long clientId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .get(PATH + "/{clientId}", clientId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void get_client_by_id_should_return_http_status_ok_200_and_client_response_dto_if_succeed()
                throws Exception {
            // given
            Long clientId = 1L;
            ClientResponseDTO clientResponseDTO = generateClientResponse();

            when(clientServiceFacade.getClientById(any())).thenReturn(clientResponseDTO);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .get(PATH + "/{clientId}", clientId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")));
        }

        @Test
        void get_all_clients_should_return_http_status_ok_200_and_empty_list_if_succeed()
                throws Exception {
            // given
            when(clientServiceFacade.getAllClients()).thenReturn(List.of());

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .get(PATH + "/all")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(0)));
        }

        @Test
        void get_all_clients_list_should_return_http_status_ok_200_and_list_client_response_dto_if_succeed()
                throws Exception {
            // given
            List<ClientResponseDTO> clientResponseDTOList = generateClientsResponseList();

            when(clientServiceFacade.getAllClients()).thenReturn(clientResponseDTOList);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .get(PATH + "/all")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("John_1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName", Matchers.is("Doe_1")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].clientId", Matchers.is(2)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("John_2")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName", Matchers.is("Doe_2")));
        }
    }

    @Nested
    class CreateClient {
        @Test
        void create_client_should_return_http_status_bad_request_400_if_value_is_null()
                throws Exception {
            // given
            ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
            clientRequestDTO.setName("John");
            clientRequestDTO.setLastName(null);

            when(clientServiceFacade.createClient(any(ClientRequestDTO.class)))
                    .thenThrow(DataIntegrityViolationException.class);

            Gson gson = new Gson();
            String jsonContent = gson.toJson(clientRequestDTO);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(jsonContent))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void create_client_should_return_http_status_ok_200_and_client_response_dto_if_succeed()
                throws Exception {
            // given
            ClientRequestDTO clientRequestDTO = generateClientRequest();
            ClientResponseDTO clientResponseDTO = generateClientResponse();

            Gson gson = new Gson();
            String jsonContent = gson.toJson(clientRequestDTO);

            when(clientServiceFacade.createClient(any(ClientRequestDTO.class)))
                    .thenReturn(clientResponseDTO);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .post(PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(jsonContent))
                    .andExpect(MockMvcResultMatchers.status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")));
        }
    }

    @Nested
    class DeleteClient {
        @Test
        void delete_client_by_id_should_return_http_status_not_found_404_if_client_not_found()
                throws Exception {
            // given
            Long clientId = 999L;

            willThrow(ClientNotFoundException.class).given(clientServiceFacade).deleteClientById(any());

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete(PATH + "/{clientId}", clientId))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void delete_client_by_id_should_return_http_status_bad_request_400_if_param_is_below_min()
                throws Exception {
            // given
            Long clientId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete(PATH + "/{clientId}", clientId))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void delete_client_by_id_should_return_http_status_ok_200_if_succeed()
                throws Exception {
            // given
            Long clientId = 1L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .delete(PATH + "/{clientId}", clientId))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    class UpdateClient {
        @Test
        void update_client_should_return_http_status_bad_request_400_if_variable_is_below_min()
                throws Exception {
            // given
            Long clientId = 0L;

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .put(PATH + "/{clientId}", clientId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());
        }

        @Test
        void update_client_should_return_http_status_not_found_404_if_client_not_found()
                throws Exception {
            //given
            Long clientId = 999L;
            ClientRequestDTO requestDTO = new ClientRequestDTO();
            requestDTO.setName("John");
            requestDTO.setLastName("Doe");

            Gson gson = new Gson();
            String gsonContent = gson.toJson(requestDTO);

            when(clientServiceFacade.updateClient(anyLong(), any(ClientRequestDTO.class)))
                    .thenThrow(ClientNotFoundException.class);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .put(PATH + "/{clientId}", clientId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(gsonContent))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());
        }

        @Test
        void update_client_should_return_status_ok_200_and_client_response_dto_if_clinet_succeed()
                throws Exception {
            // given
            Long clientId = 1L;

            ClientResponseDTO clientResponseDTO = generateClientResponse();
            ClientRequestDTO clientRequestDTO = new ClientRequestDTO();
            clientRequestDTO.setName("John");
            clientRequestDTO.setLastName("Doe");

            Gson gson = new Gson();
            String gsonContent = gson.toJson(clientRequestDTO);

            when(clientServiceFacade.updateClient(anyLong(), any(ClientRequestDTO.class)))
                    .thenReturn(clientResponseDTO);

            // when & then
            mockMvc.perform(MockMvcRequestBuilders
                            .put(PATH + "/{clientId}", clientId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .content(gsonContent))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.clientId", Matchers.is(1)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.is("John")))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", Matchers.is("Doe")));
        }
    }
}