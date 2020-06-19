package com.starling.roundupservice.common.savingsgoal.create;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

public class SavingsGoalCreationProvider {

  private final WebClient apiClient;

  public SavingsGoalCreationProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public SavingsGoalCreationResponse createSavingsGoal(final String currency) {



  }
}
