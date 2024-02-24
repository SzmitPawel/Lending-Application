package com.lending.application.config;

import client.ClientRequestMapperImpl;
import client.ClientResponseMapperImpl;
import com.lending.application.mapper.client.ClientRequestMapper;
import com.lending.application.mapper.client.ClientResponseMapper;
import com.lending.application.mapper.credit.rating.CreditRatingResponseMapper;
import com.lending.application.mapper.loan.LoanResponseMapper;
import com.lending.application.mapper.transaction.TransactionResponseMapper;
import loan.LoanResponseMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rating.CreditRatingResponseMapperImpl;
import transaction.TransactionResponseMapperImpl;

@Configuration
public class MapStructMappersConfiguration {
    @Bean
    public ClientRequestMapper clientRequestMapper() {
        return new ClientRequestMapperImpl();
    }

    @Bean
    public ClientResponseMapper clientResponseMapper() {
        return new ClientResponseMapperImpl();
    }

    @Bean
    CreditRatingResponseMapper creditRatingResponseMapper() {
        return new CreditRatingResponseMapperImpl();
    }

    @Bean
    LoanResponseMapper loanResponseMapper() {
        return new LoanResponseMapperImpl();
    }

    @Bean
    TransactionResponseMapper transactionResponseMapper() {
        return new TransactionResponseMapperImpl();
    }
}