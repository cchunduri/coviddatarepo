package com.chaitu.dashboard.carona.utils;

import com.chaitu.dashboard.carona.exception.NetworkException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;

@Slf4j
@Component
public class HtmlUtils {

    @Value("${api.timeout}")
    private int timeout;

    @Retryable(
            value = { IOException.class, UnknownHostException.class },
            maxAttempts = 2,
            backoff = @Backoff(delay = 3000))
    public Optional<Document> parseHtmlByUrl(String url) {
        try {
            return Optional.ofNullable(Jsoup.parse(new URL(url), timeout));
        } catch (UnknownHostException unknownHostException) {
            log.error("Unable to connect website", unknownHostException);
        } catch (IOException e) {
            log.error("Got Exception", e);
        }
        return Optional.empty();
    }
}
