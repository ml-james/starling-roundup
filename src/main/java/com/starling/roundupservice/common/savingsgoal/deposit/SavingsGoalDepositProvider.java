package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.GeneralException;
import com.starling.roundupservice.common.exception.ServerException;
import java.time.Duration;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

public class SavingsGoalDepositProvider {

  private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

  private final WebClient apiClient;

  public SavingsGoalDepositProvider() {
    this.apiClient = WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
        .build();
  }

  public SavingsGoalDepositResponse depositToSavingsGoal(final RoundupAccountMapping roundupAccount, final int roundupAmount) {

    var transferId = generateTransferId();
    var money = Money.builder()
        .currency(roundupAccount.getCurrency())
        .minorUnits(roundupAmount)
        .build();

    return apiClient.post()
        .uri(String.format("http://localhost:8080/api/v2/account/%s/savings-goals/%s/add-money/%s", roundupAccount.getAccountUid(),
            roundupAccount.getSavingsGoalUid(), transferId))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(BodyInserters.fromValue(new SavingsGoalDepositRequest(money)))
        .retrieve()
        .onStatus(HttpStatus::is4xxClientError, clientResponse ->
            Mono.error(new ClientException()))
        .onStatus(HttpStatus::is5xxServerError, clientResponse ->
            Mono.error(new ServerException()))
        .bodyToMono(SavingsGoalDepositResponse.class)
        .timeout(DEFAULT_TIMEOUT)
        .blockOptional()
        .orElseThrow(GeneralException::new);

  }

  private String generateTransferId() {

    return RandomStringUtils.random(32, true, true);
  }
}
