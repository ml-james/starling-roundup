package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.WebClientProvider;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.GeneralException;
import com.starling.roundupservice.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class SavingsGoalDepositProvider extends WebClientProvider
{
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

    public SavingsGoalDepositResponse depositToSavingsGoal(final RoundupAccountMapping roundupAccount,
                                                           final int roundupAmount,
                                                           final String bearerToken)
    {
        var transferId = generateTransferId();
        var money = Money.builder()
                .currency(roundupAccount.getAccountUidCurrency())
                .minorUnits(roundupAmount)
                .build();

        return getWebClient(bearerToken).post()
                .uri(String.format("/account/%s/savings-goals/%s/add-money/%s",
                        roundupAccount.getAccountUid(),
                        roundupAccount.getSavingsGoalUid(),
                        transferId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(new SavingsGoalDepositRequest(money)))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ClientException("Savings goal deposit provider: ", "client error")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new ServerException("Savings goal deposit provider: ", "server error")))
                .bodyToMono(SavingsGoalDepositResponse.class)
                .timeout(DEFAULT_TIMEOUT)
                .blockOptional()
                .orElseThrow(GeneralException::new);
    }

    private String generateTransferId()
    {
        return RandomStringUtils.random(32, true, true);
    }
}
