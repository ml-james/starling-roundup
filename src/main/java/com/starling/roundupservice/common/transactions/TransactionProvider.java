package com.starling.roundupservice.common.transactions;

import com.starling.roundupservice.common.transactions.domain.FeedItems;
import com.starling.roundupservice.common.transactions.domain.TransactionTimestamps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class TransactionProvider {

  private final WebClient apiClient;

  public TransactionProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public FeedItems retrieveTransactionsInWindow(final String accountUid, final TransactionTimestamps transactionTimestamps) {

  }

}
