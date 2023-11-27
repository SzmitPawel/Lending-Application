package com.lending.application.webScraping;

import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Component
@NoArgsConstructor
public class WebPageFetcher {

    private final String[] BROWSER_AGENT = {
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36",
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Firefox/100.0.0",
      "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Edge/18.0.0",
      "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Safari/14.1.2",
      "Mozilla/5.0 (Linux; Android 11) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Mobile Safari/537.36",
      "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36",
    };

    private final String ACCEPT_LANGUAGE =
            "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7";

    private final String REFERRER = "https://www.google.com";

    private final int TIME_OUT = 5000;

    public Document connect(final String side) throws IOException {
        return Jsoup.connect(side)
                    .timeout(TIME_OUT)
                    .userAgent(randomBrowserAgent())
                    .referrer(REFERRER)
                    .header("Accept-Language",ACCEPT_LANGUAGE)
                    .get();
    }

    private String randomBrowserAgent() {
        Random random = new Random();
        return BROWSER_AGENT[random.nextInt(BROWSER_AGENT.length)];
    }
}