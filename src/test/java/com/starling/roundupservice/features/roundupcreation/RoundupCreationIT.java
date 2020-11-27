package com.starling.roundupservice.features.roundupcreation;

import com.fasterxml.jackson.databind.JsonNode;
import com.starling.roundupservice.BaseTestIT;
import com.starling.roundupservice.MockedParameters;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import com.starling.roundupservice.creation.RoundupCreationResponse;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.BodyInserters;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RoundupCreationIT extends BaseTestIT
{
    @ParameterizedTest(name = "{index}: {0}")
    @ArgumentsSource(RoundupCreationProvider.class)
    void createRoundupGoal(final String testName, final MockedParameters mockedParameters)
    {
        this.mockedParameters = mockedParameters;

        givenResponseForAccountRetrieval();
        givenResponseForSavingsGoalDeposit();

        thenRoundupGoalCreationRequestedAndResponseVerified("55198b91-fd4c-45d4-b509-1d6fbbdaf777");
        thenVerifyRequestToAccountRetrieval();
        thenVerifyRequestToSavingsGoalCreation();
    }

    @SneakyThrows
    private void givenResponseForAccountRetrieval()
    {
        server.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .setResponseCode(mockedParameters.getMockedStatusCodeAccountRetrieval().value())
                .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromAccountRetrieval())));
    }

    @SneakyThrows
    private void givenResponseForSavingsGoalDeposit()
    {
        server.enqueue(new MockResponse()
                .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .setResponseCode(mockedParameters.getMockedStatusCodeFromSavingsDepositCreation().value())
                .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromSavingsDepositCreation())));
    }

    private void thenRoundupGoalCreationRequestedAndResponseVerified(final String accountUid)
    {
        webTestClient.put().uri(String.format(contextPath + PATH_ROUNDUP_CREATION, accountUid))
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJQUzI1NiIsInpp")
                .accept(APPLICATION_JSON)
                .body(BodyInserters.fromValue(loadResourceAsObject(mockedParameters.getRequestToStarlingRoundup(), RoundupCreationRequest.class)))
                .exchange()
                .expectStatus().isEqualTo(mockedParameters.getExpectedStatusCodeFromStarlingRoundup())
                .expectBody(RoundupCreationResponse.class)
                .isEqualTo(loadResourceAsObject(mockedParameters.getExpectedResponseFromStarlingRoundup(), RoundupCreationResponse.class));
    }

    @SneakyThrows
    private void thenVerifyRequestToAccountRetrieval()
    {
        var actualRequest = server.takeRequest();

        JsonNode actual = jsonMapper.readTree(actualRequest.getBody().readUtf8());
        JsonNode expected = jsonMapper.readTree(loadResourceAsString(mockedParameters.getExpectedRequestToAccountRetrieval()));

        Assertions.assertEquals(expected, actual);
    }

    @SneakyThrows
    private void thenVerifyRequestToSavingsGoalCreation()
    {
        var actualRequest = server.takeRequest();

        JsonNode actual = jsonMapper.readTree(actualRequest.getBody().readUtf8());
        JsonNode expected = jsonMapper.readTree(loadResourceAsString(mockedParameters.getExpectedRequestToSavingsDepositCreation()));

        assertEquals(
                jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected),
                jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                false
        );
    }
}
