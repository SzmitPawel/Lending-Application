package com.lending.application.config;

import client.ClientRequestMapperImpl;
import client.ClientResponseMapperImpl;
import com.lending.application.mapper.client.ClientRequestMapper;
import com.lending.application.mapper.client.ClientResponseMapper;
import com.lending.application.mapper.credit.rating.CreditRatingRequestMapper;
import com.lending.application.mapper.credit.rating.CreditRatingResponseMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rating.CreditRatingRequestMapperImpl;
import rating.CreditRatingResponseMapperImpl;

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
    public CreditRatingRequestMapper creditRatingRequestMapper() {
        return new CreditRatingRequestMapperImpl();
    }

    @Bean
    CreditRatingResponseMapper creditRatingResponseMapper() {
        return new CreditRatingResponseMapperImpl();
    }
}