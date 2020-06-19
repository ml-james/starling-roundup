package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.transactions.domain.FeedItems;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class SavingsGoalDepositProvider {

  private final WebClient apiClient;

  public SavingsGoalDepositProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public FeedItems retrieveTransactionsInWindow(final String accountUid, final int amount) {

  }

}
