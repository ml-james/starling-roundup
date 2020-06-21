package com.starling.roundupservice.common.transactions;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.GeneralException;
import com.starling.roundupservice.common.exception.ServerException;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class TransactionProvider {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

  private final WebClient apiClient;

  public TransactionProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public FeedItems retrieveTransactionsInWindow(final RoundupAccountMapping account, final TransactionTimestamps transactionTimestamps) {

    return apiClient.post()
        .uri(String.format(
            "http://localhost:8080/api/v2/account/%s/category/%s/category/transactions-between?minTransactionTimestamp=%s&?maxTransactionTimestamp=%s",
            account.getAccountUid(), account.getCategoryUid(), transactionTimestamps.getMinTransactionTimestamp(),
            transactionTimestamps.getMaxTransactionTimestamp()))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse ->
            Mono.error(new ClientException("Transaction provider: ", "client error")))
        .onStatus(HttpStatus::is5xxServerError, clientResponse ->
            Mono.error(new ServerException("Transaction provider: ", "server error")))
        .bodyToMono(FeedItems.class)
        .timeout(DEFAULT_TIMEOUT)
        .blockOptional()
        .orElseThrow(GeneralException::new);
  }
}
