package com.lending.application.domain.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ClientRequestDTO {
    @Schema(description = "Client name", example = "John")
    private String name;
    @Schema(description = "Client last name", example = "Doe")
    private String lastName;
    @Schema(description = "Client residential address.", example = "123 Main St, Apt 4, New York, NY 10001")
    private String address;
    @Schema(description = "Client email address.", example = "example@mail.com")
    private String emailAddress;
    @Schema(description = "Client phone number.", example = "555-555-555")
    private String phoneNumber;
}
