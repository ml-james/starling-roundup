package com.starling.roundupservice.common.savingsgoal.create;

import com.starling.roundupservice.common.WebClientProvider;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.GeneralException;
import com.starling.roundupservice.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class SavingsGoalCreationProvider extends WebClientProvider
{
    public SavingsGoalCreationResponse createSavingsGoal(final SavingsGoalCreationRequest savingsGoalRequest,
                                                         final String accountUid,
                                                         final String bearerToken)
    {
        return getWebClient(bearerToken).put()
                .uri(String.format("account/%s/savings-goals", accountUid))
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
