package com.lending.application.webScraping;

import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankierScraping implements WebScrapingStrategy {

    private final WebPageFetcher webPageFetcher;

    private final String BANKIER =
            "https://www.bankier.pl/gospodarka/wskazniki-makroekonomiczne/referencyjna-pol";

    private final String PAGE_CLASS = "profilLast";

    @Override
    public BigDecimal getInterestFromSource() throws IOException {
        Element element = webPageFetcher.connect(BANKIER).getElementsByClass(PAGE_CLASS).first();
        String numericString = element.text().replace(",",".").replace("%","");

        return new BigDecimal(numericString);
    }
}