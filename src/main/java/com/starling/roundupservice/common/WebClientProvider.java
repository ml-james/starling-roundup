package com.starling.roundupservice.common;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebClientProvider
{
    private static final String BEARER_TOKEN = "Bearer ";

    private WebClient webClient;
    private String bearerToken;

    public WebClient getWebClient(final String bearerToken)
    {
        if (webClient == null || !this.bearerToken.equals(bearerToken))
        {
            if (validBearerToken(bearerToken))
            {
                this.bearerToken = bearerToken.replace(BEARER_TOKEN, "");
                this.webClient = WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
                        .defaultHeaders(header -> header.setBearerAuth(this.bearerToken))
                        .build();
            }
            else
            {
                throw new RuntimeException(String.format("Invalid bearer token %s", bearerToken));
            }
        }
        return webClient;
    }

    private boolean validBearerToken(final String bearerToken)
    {
        Pattern pattern = Pattern.compile(BEARER_TOKEN);
        Matcher matcher = pattern.matcher(bearerToken);
        return matcher.find();
    }
}
