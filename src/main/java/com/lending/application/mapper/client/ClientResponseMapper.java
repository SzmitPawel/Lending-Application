package com.lending.application.mapper.client;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.client.ClientResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(implementationPackage = "client")
public interface ClientResponseMapper {
    ClientResponseDTO mapToClientDto(final Client client);

    List<ClientResponseDTO> mapToClientDtoList(final List<Client> clientList);
}