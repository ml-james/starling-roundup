package com.starling.roundupservice.common;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.GeneralException;
import com.starling.roundupservice.common.exception.ServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@Component
public class StarlingAPIProvider extends BaseWebClient
{
    public <T> T queryStarlingAPI(final String uri,
                                  final String bearerToken,
                                  final HttpMethod method,
                                  final Object requestObject,
                                  final Class<T> returnType)
    {
        return getWebClient(bearerToken)
                .method(method)
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(requestObject == null ? BodyInserters.empty() : BodyInserters.fromValue(requestObject))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
                        Mono.error(new ClientException("Client error: ", String.format("there was an error returning %s", returnType.getName()))))
                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
                        Mono.error(new ServerException("Server error: ", String.format("there was an error returning %s", returnType.getName()))))
                .bodyToMono(returnType)
                .timeout(DEFAULT_TIMEOUT)
                .blockOptional()
                .orElseThrow(GeneralException::new);
    }
}
