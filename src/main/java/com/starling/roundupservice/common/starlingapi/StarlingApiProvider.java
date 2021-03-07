package com.starling.roundupservice.common.starlingapi;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.GeneralException;
import com.starling.roundupservice.common.exception.ServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

@Component
@SuppressWarnings("unchecked")
public class StarlingApiProvider extends BaseWebClient
{
    public <T> T queryStarlingAPI(final String uri,
                                  final String bearerToken,
                                  final HttpMethod method,
                                  final Object requestObject,
                                  final Class<T> returnType)
    {

        var response = getWebClient(bearerToken)
                .method(method)
                .uri(uri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(requestObject == null ? BodyInserters.empty() : BodyInserters.fromValue(requestObject))
                .exchange()
                .flatMap(clientResponse ->
                {
                    if (clientResponse.statusCode().is2xxSuccessful())
                    {
                        return clientResponse.bodyToMono(returnType);
                    }
                    else if (clientResponse.statusCode().is4xxClientError())
                    {
                        return clientResponse.bodyToMono(ClientErrorResponse.class);
                    }
                    else
                    {
                        return Mono.error(new ServerException("Starling API error: ", String.format(" there was an error returning %s", returnType.getName())));
                    }
                })
                .timeout(DEFAULT_TIMEOUT)
                .blockOptional()
                .orElseThrow(GeneralException::new);

        if (response instanceof ClientErrorResponse)
        {
            throw new ClientException("Starling API error: ", String.format(" there was a problem returning %s due to the following errors %s", returnType.getName(), ((ClientErrorResponse) response).errors));
        }
        else
        {
            return (T) response;
        }
    }
}
