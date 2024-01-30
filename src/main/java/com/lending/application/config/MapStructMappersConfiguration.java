package com.lending.application.config;

import account.AccountMapperImpl;
import client.ClientRequestMapperImpl;
import client.ClientResponseMapperImpl;

import com.lending.application.mapper.account.AccountMapper;
import com.lending.application.mapper.client.ClientRequestMapper;
import com.lending.application.mapper.client.ClientResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    public AccountMapper accountMapper() {
        return new AccountMapperImpl();
    }
}