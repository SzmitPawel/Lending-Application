package com.lending.application.mapper.client;

import com.lending.application.domain.client.Client;
import com.lending.application.domain.client.ClientRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        implementationPackage = "client",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ClientRequestMapper {
    Client mapToClient(ClientRequestDTO clientRequestDTO);

    void updateClientFromDto(ClientRequestDTO clientRequestDTO, @MappingTarget Client client);
}
