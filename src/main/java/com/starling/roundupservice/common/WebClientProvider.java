package com.starling.roundupservice.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebClientProvider
{
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    private static final String BEARER_TOKEN = "Bearer ";

    private WebClient webClient;
    private String bearerToken;
    @Value("${starling.baseurl}")
    private String baseUrl;

    public WebClient getWebClient(final String bearerToken)
    {
        if (webClient == null || !this.bearerToken.equals(bearerToken))
        {
            if (validBearerToken(bearerToken))
            {
                this.bearerToken = bearerToken.replace(BEARER_TOKEN, "");
                this.webClient = WebClient.builder()
                        .clientConnector(new ReactorClientHttpConnector(HttpClient.create().wiretap(true)))
                        .baseUrl(baseUrl)
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
