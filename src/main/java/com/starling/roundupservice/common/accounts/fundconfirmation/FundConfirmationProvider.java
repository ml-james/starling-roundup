package com.starling.roundupservice.common.accounts.fundconfirmation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class FundConfirmationProvider {

  private final WebClient apiClient;

  public FundConfirmationProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public FundConfirmationResponse retrieveFundConfirmation(final String accountUid, final int amount) {

  }

}
