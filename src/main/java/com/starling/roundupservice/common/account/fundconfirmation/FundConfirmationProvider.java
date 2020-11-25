package com.starling.roundupservice.common.account.fundconfirmation;

import com.starling.roundupservice.common.WebClientProvider;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.GeneralException;
import com.starling.roundupservice.common.exception.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@Slf4j
public class FundConfirmationProvider extends WebClientProvider
{
    public FundConfirmationResponse retrieveFundConfirmation(final String accountUid, final int amount, final String bearerToken)
    {
        return getWebClient(bearerToken).post()
                .uri(String.format("/accounts/%s/confirmation-of-funds?=targetAmountInMinorUnits=%s", accountUid, amount))
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
