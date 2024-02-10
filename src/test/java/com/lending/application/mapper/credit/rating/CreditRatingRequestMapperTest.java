package com.lending.application.mapper.credit.rating;

import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.domain.credit.rating.CreditRatingRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rating.CreditRatingRequestMapperImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {CreditRatingRequestMapperImpl.class})
class CreditRatingRequestMapperTest {
    @Autowired
    CreditRatingRequestMapper requestMapper;

    @Test
    void map_to_credit_rating_should_return_credit_rating() {
        // given
        CreditRatingRequestDTO requestDTO = new CreditRatingRequestDTO();
        requestDTO.setCreditRatingEnum(CreditRatingEnum.FOUR);
        requestDTO.setDateOfRating(LocalDate.now());

        // when
        CreditRating retrievedCreditRatingRequest = requestMapper.mapToCreditRating(requestDTO);

        // then
        assertNotNull(retrievedCreditRatingRequest);
        assertEquals(requestDTO.getCreditRatingEnum(), retrievedCreditRatingRequest.getCreditRating());
        assertEquals(requestDTO.getDateOfRating(), retrievedCreditRatingRequest.getDateOfRating());
    }
}