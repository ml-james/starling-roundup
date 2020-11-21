package com.starling.roundupservice.common.savingsgoal.create;

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
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Component
@Slf4j
public class SavingsGoalCreationProvider
{

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

    private final WebClient apiClient;

    public SavingsGoalCreationProvider()
    {
        this.apiClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
                .build();
    }

    public SavingsGoalCreationResponse createSavingsGoal(final SavingsGoalCreationRequest savingsGoalRequest)
    {

        return apiClient.post()
                .uri("http://localhost:8080/api/v2/account/%s/savings-goals")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(savingsGoalRequest))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ClientException("Savings goal creation provider: ", "client error")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new ServerException("Savings goal creation provider: ", "server error")))
                .bodyToMono(SavingsGoalCreationResponse.class)
                .timeout(DEFAULT_TIMEOUT)
                .blockOptional()
                .orElseThrow(GeneralException::new);
    }
}
