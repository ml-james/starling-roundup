package com.starling.roundupservice.common.account.fundconfirmation;

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
public class FundConfirmationProvider {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

  private final WebClient apiClient;

  public FundConfirmationProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public FundConfirmationResponse retrieveFundConfirmation(final String accountUid, final int amount) {

    return apiClient.post()
        .uri(String.format("http://localhost:8080/api/v2/accounts/%s/confirmation-of-funds?=targetAmountInMinorUnits=%s", accountUid, amount))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse ->
            Mono.error(new ClientException("Fund confirmation provider: ", "client error")))
        .onStatus(HttpStatus::is5xxServerError, clientResponse ->
            Mono.error(new ServerException("Fund confirmation provider: ", "server error")))
        .bodyToMono(FundConfirmationResponse.class)
        .timeout(DEFAULT_TIMEOUT)
        .blockOptional()
        .orElseThrow(GeneralException::new);

  }
}
