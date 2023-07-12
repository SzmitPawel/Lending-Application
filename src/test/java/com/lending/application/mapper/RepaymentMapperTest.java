package com.lending.application.mapper;

import com.lending.application.domain.Repayment;
import com.lending.application.domain.dto.RepaymentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RepaymentMapperTest {
    @Autowired
    RepaymentMapper repaymentMapper;

    @Test
    void TestMapToRepaymentDto() {
        // given
        Repayment repayment = new Repayment(new BigDecimal(1), LocalDate.now());
        repayment.setRepaymentId(1L);

        // when
        RepaymentDto retrievedRepaymentDto = repaymentMapper.mapToRepaymentDto(repayment);

        // then
        assertEquals(1L, retrievedRepaymentDto.getRepaymentId());
        assertEquals(new BigDecimal(1), retrievedRepaymentDto.getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepaymentDto.getRepaymentDate());
    }

    @Test
    void TestMapToRepayment() {
        // given
        RepaymentDto repaymentDto = new RepaymentDto(
                1L,
                new BigDecimal(1),
                LocalDate.now()
        );

        // when
        Repayment retrievedRepayment = repaymentMapper.mapToRepayment(repaymentDto);

        // then
        assertEquals(1L, retrievedRepayment.getRepaymentId());
        assertEquals(new BigDecimal(1), retrievedRepayment.getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepayment.getRepaymentDate());
    }

    @Test
    void TestMapToRepaymentDtoList() {
        // given
        Repayment repayment1 = new Repayment(new BigDecimal(1), LocalDate.now());
        repayment1.setRepaymentId(1L);
        Repayment repayment2 = new Repayment(new BigDecimal(2), LocalDate.now());
        repayment2.setRepaymentId(2L);

        List<Repayment> repaymentList = List.of(repayment1,repayment2);

        // when
        List<RepaymentDto> retrievedRepaymentDtoList = repaymentMapper.mapToRepaymentDtoList(repaymentList);

        // then
        assertEquals(2, retrievedRepaymentDtoList.size());
        assertEquals(1L, retrievedRepaymentDtoList.get(0).getRepaymentId());
        assertEquals(new BigDecimal(1), retrievedRepaymentDtoList.get(0).getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepaymentDtoList.get(0).getRepaymentDate());
        assertEquals(2L, retrievedRepaymentDtoList.get(1).getRepaymentId());
        assertEquals(new BigDecimal(2), retrievedRepaymentDtoList.get(1).getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepaymentDtoList.get(1).getRepaymentDate());
    }

    @Test
    void TestMapToRepaymentList() {
        // given
        RepaymentDto repaymentDto1 = new RepaymentDto(
                1L,
                new BigDecimal(1),
                LocalDate.now()
        );
        RepaymentDto repaymentDto2 = new RepaymentDto(
                2L,
                new BigDecimal(2),
                LocalDate.now()
        );
        List<RepaymentDto> repaymentDtoList = List.of(repaymentDto1,repaymentDto2);

        // when
        List<Repayment> retrievedRepaymentList = repaymentMapper.mapToRepaymentList(repaymentDtoList);

        // then
        assertEquals(2, retrievedRepaymentList.size());
        assertEquals(1L, retrievedRepaymentList.get(0).getRepaymentId());
        assertEquals(new BigDecimal(1), retrievedRepaymentList.get(0).getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepaymentList.get(0).getRepaymentDate());
        assertEquals(2L, retrievedRepaymentList.get(1).getRepaymentId());
        assertEquals(new BigDecimal(2), retrievedRepaymentList.get(1).getRepaymentAmount());
        assertEquals(LocalDate.now(), retrievedRepaymentList.get(1).getRepaymentDate());
    }

}