package com.lending.application.webScraping;

import java.io.IOException;
import java.math.BigDecimal;

public interface WebScrapingStrategy {
    BigDecimal getInterestFromSource() throws IOException;
}
