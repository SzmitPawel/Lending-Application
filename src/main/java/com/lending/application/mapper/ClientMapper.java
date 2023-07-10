package com.lending.application.mapper;

import com.lending.application.domain.Client;
import com.lending.application.domain.dto.ClientDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientMapper {
    CreditRatingMapper creditRatingMapper;
    AccountMapper accountMapper;
    LoanMapper loanMapper;

    public ClientDto mapToClientDto(final Client client) {
        return new ClientDto(
                client.getClientID(),
                client.getName(),
                client.getLastName(),
                client.getAddress(),
                client.getEmailAddress(),
                client.getPhoneNumber(),
                accountMapper.mapToAccountDto(client.getAccount()),
                creditRatingMapper.mapToCreditRatingDto(client.getCreditRating()),
                loanMapper.mapToListLoanDto(client.getLoanList())
        );
    }

    public Client mapToClient(final ClientDto clientDto) {
        return new Client(
                clientDto.getClientID(),
                clientDto.getName(),
                clientDto.getLastName(),
                clientDto.getAddress(),
                clientDto.getEmailAddress(),
                clientDto.getPhoneNumber(),
                accountMapper.mapToAccount(clientDto.getAccountDto()),
                creditRatingMapper.mapToCreditRating(clientDto.getCreditRatingDto()),
                loanMapper.mapToLoanList(clientDto.getLoanList())
        );
    }
}
