package com.lending.application.mapper;

import com.lending.application.domain.penalty.Penalty;
import com.lending.application.domain.penalty.PenaltyDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class PenaltyMapperTest {
    @Autowired
    PenaltyMapper penaltyMapper;

    @Test
    void TestMapToPenaltyDto() {
        // given
        Penalty penalty = new Penalty(22, LocalDate.now());
        penalty.setPenaltyId(1L);

        // when
        PenaltyDto retrievedPenaltyDto = penaltyMapper.mapToPenaltyDto(penalty);

        // then
        assertEquals(1L, retrievedPenaltyDto.getPenaltyId());
        assertEquals(22, retrievedPenaltyDto.getPenaltyPercentage());
        assertEquals(LocalDate.now(), retrievedPenaltyDto.getPenaltyDate());
    }

    @Test
    void TestMapToPenalty() {
        // given
        PenaltyDto penaltyDto = new PenaltyDto(1L, 22, LocalDate.now());

        // when
        Penalty retrievedPenalty = penaltyMapper.mapToPenalty(penaltyDto);

        // then
        assertEquals(1L, retrievedPenalty.getPenaltyId());
        assertEquals(22, retrievedPenalty.getPenaltyPercentage());
        assertEquals(LocalDate.now(), retrievedPenalty.getPenaltyDate());
    }
}