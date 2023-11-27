package com.lending.application.webScraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankierScrapingTest {

    @InjectMocks
    private BankierScraping bankierScraping;

    @Mock
    private WebPageFetcher webPageFetcher;

    private Document prepareContent() {
        String htmlContent = "<html><body><div class='profilLast'>4,75%</div></body></html>";
        return Jsoup.parse(htmlContent);
    }

    @Test
    public void testIOExceptionHandling() throws IOException {
        // given
        when(webPageFetcher.connect(anyString())).thenThrow(new IOException());

        // when & then
        assertThrows(IOException.class, () -> bankierScraping.getInterestFromSource());
    }

    @Test
    void testGetInterest_shouldReturnInterest() throws IOException {
        // given
        Document document = prepareContent();

        when(webPageFetcher.connect(anyString())).thenReturn(document);

        // when
        BigDecimal expectedResult = bankierScraping.getInterestFromSource();

        System.out.println(expectedResult);

        // then
        assertEquals(BigDecimal.valueOf(4.75), expectedResult);
    }
}