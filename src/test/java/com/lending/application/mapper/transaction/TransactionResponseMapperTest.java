package com.lending.application.mapper.transaction;

import com.lending.application.domain.transaction.Transaction;
import com.lending.application.domain.transaction.TransactionMethodEnum;
import com.lending.application.domain.transaction.TransactionResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import transaction.TransactionResponseMapperImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {TransactionResponseMapperImpl.class})
class TransactionResponseMapperTest {
    @Autowired
    TransactionResponseMapper responseMapper;

    @Test
    void map_to_transaction_dto_should_return_transaction_response_dto() {
        // given
        Transaction transaction = new Transaction();
        transaction.setTransactionID(1L);
        transaction.setTransactionAmount(BigDecimal.TEN);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionMethodEnum(TransactionMethodEnum.DEPOSIT);

        // when
        TransactionResponseDTO retrievedTransactionResponseDTO = responseMapper.mapToTransactionDTO(transaction);

        // then
        assertEquals(transaction.getTransactionID(),
                retrievedTransactionResponseDTO.getTransactionId());
        assertEquals(transaction.getTransactionAmount(),
                retrievedTransactionResponseDTO.getTransactionAmount());
        assertEquals(transaction.getTransactionDate(),
                retrievedTransactionResponseDTO.getTransactionDate());
        assertEquals(transaction.getTransactionMethodEnum(),
                retrievedTransactionResponseDTO.getTransactionMethodEnum());
    }

    @Test
    void map_to_transaction_dto_list_should_return_list_of_transactions() {
        // given
        Transaction transaction1 = new Transaction();
        transaction1.setTransactionID(1L);
        transaction1.setTransactionAmount(BigDecimal.ONE);
        transaction1.setTransactionDate(LocalDate.now());
        transaction1.setTransactionMethodEnum(TransactionMethodEnum.DEPOSIT);

        Transaction transaction2 = new Transaction();
        transaction2.setTransactionID(2L);
        transaction2.setTransactionAmount(BigDecimal.TEN);
        transaction2.setTransactionDate(LocalDate.now());
        transaction2.setTransactionMethodEnum(TransactionMethodEnum.WITHDRAWAL);

        List<Transaction> transactionList = List.of(transaction1, transaction2);

        // when
        List<TransactionResponseDTO> retrievedTransactionResponseDTOList = responseMapper
                .mapToListTransactionDTO(transactionList);

        // then
        assertNotNull(retrievedTransactionResponseDTOList);
        assertEquals(2, retrievedTransactionResponseDTOList.size());

        assertEquals(transactionList.get(0).getTransactionID(),
                retrievedTransactionResponseDTOList.get(0).getTransactionId());
        assertEquals(transactionList.get(0).getTransactionAmount(),
                retrievedTransactionResponseDTOList.get(0).getTransactionAmount());
        assertEquals(transactionList.get(0).getTransactionDate(),
                retrievedTransactionResponseDTOList.get(0).getTransactionDate());
        assertEquals(transactionList.get(0).getTransactionMethodEnum(),
                retrievedTransactionResponseDTOList.get(0).getTransactionMethodEnum());

        assertEquals(transactionList.get(1).getTransactionID(),
                retrievedTransactionResponseDTOList.get(1).getTransactionId());
        assertEquals(transactionList.get(1).getTransactionAmount(),
                retrievedTransactionResponseDTOList.get(1).getTransactionAmount());
        assertEquals(transactionList.get(1).getTransactionDate(),
                retrievedTransactionResponseDTOList.get(1).getTransactionDate());
        assertEquals(transactionList.get(1).getTransactionMethodEnum(),
                retrievedTransactionResponseDTOList.get(1).getTransactionMethodEnum());
    }
}