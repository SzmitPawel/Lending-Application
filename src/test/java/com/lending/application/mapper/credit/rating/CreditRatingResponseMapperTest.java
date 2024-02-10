package com.lending.application.mapper.credit.rating;

import com.lending.application.domain.credit.rating.CreditRating;
import com.lending.application.domain.credit.rating.CreditRatingEnum;
import com.lending.application.domain.credit.rating.CreditRatingResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rating.CreditRatingResponseMapperImpl;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {CreditRatingResponseMapperImpl.class})
public class CreditRatingResponseMapperTest {
    @Autowired
    CreditRatingResponseMapper responseMapper;


    @Test
    void map_to_credit_rating_dto_should_return_credit_rating_response_dto() {
        // given
        CreditRating creditRating = new CreditRating();
        creditRating.setRatingId(1L);
        creditRating.setCreditRating(CreditRatingEnum.ONE);
        creditRating.setDateOfRating(LocalDate.now());

        // when
        CreditRatingResponseDTO responseDTO = responseMapper.mapToCreditRatingDTO(creditRating);

        // then
        assertNotNull(responseDTO);
        assertEquals(creditRating.getRatingId(), responseDTO.getRatingId());
        assertEquals(creditRating.getCreditRating(), responseDTO.getCreditRating());
        assertEquals(creditRating.getDateOfRating(), responseDTO.getDateOfRating());
    }
}