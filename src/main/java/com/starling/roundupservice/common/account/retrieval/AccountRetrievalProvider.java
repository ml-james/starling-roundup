package com.starling.roundupservice.common.account.retrieval;

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
public class AccountRetrievalProvider {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

  private final WebClient apiClient;

  public AccountRetrievalProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public Accounts retrieveAccounts() {

    return apiClient.post()
        .uri("http://localhost:8080/api/v2/accounts/%s")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse ->
            Mono.error(new ClientException()))
        .onStatus(HttpStatus::is5xxServerError, clientResponse ->
            Mono.error(new ServerException()))
        .bodyToMono(Accounts.class)
        .timeout(DEFAULT_TIMEOUT)
        .blockOptional()
        .orElseThrow(GeneralException::new);

  }
}
