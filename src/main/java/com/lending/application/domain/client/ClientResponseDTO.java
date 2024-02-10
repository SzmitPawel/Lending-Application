package com.lending.application.domain.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClientResponseDTO {
    @Schema(description = "Client id.", example = "1")
    private Long clientId;
    @Schema(description = "Client first name.", example = "John")
    private String name;
    @Schema(description = "Client last name.", example = "Doe")
    private String lastName;
    @Schema(description = "Client residential address.", example = "123 Main St, Apt 4, New York, NY 10001")
    private String address;
    @Schema(description = "Client email.", example = "example@mail.com")
    private String emailAddress;
    @Schema(description = "Client phone number.", example = "555-555-555")
    private String phoneNumber;
}