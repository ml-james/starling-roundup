package com.starling.roundupservice.features.roundupcreation;

import com.fasterxml.jackson.databind.JsonNode;
import com.starling.roundupservice.BaseTestIT;
import com.starling.roundupservice.MockedParameters;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import com.starling.roundupservice.creation.RoundupCreationResponse;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.reactive.function.BodyInserters;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RoundupCreationIT extends BaseTestIT
{
    private boolean requestsAccountRetrieval;
    private boolean requestsSavingsGoalCreation;
    private String accountUid;

    @ParameterizedTest(name = "{index}: {0}")
    @ArgumentsSource(RoundupCreationProvider.class)
    @Rollback
    void createRoundupGoal(final String testName, final MockedParameters mockedParameters)
    {
        this.mockedParameters = mockedParameters;
        switch (testName)
        {
            case "duplicate_roundup":
                requestsAccountRetrieval = false;
                requestsSavingsGoalCreation = false;
                accountUid = "b2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
            case "unauthorised":
                requestsSavingsGoalCreation = false;
                requestsAccountRetrieval = true;
                accountUid = "55198b91-fd4c-45d4-b509-1d6fbbdaf777";
                break;
            case "bad_request":
                requestsSavingsGoalCreation = true;
                requestsAccountRetrieval = true;
                accountUid = "11111a11-fd4c-45d4-b509-1d6fbbdaf777";
                break;
            case "success":
            default:
                requestsSavingsGoalCreation = true;
                requestsAccountRetrieval = true;
                accountUid = "22222b22-fd4c-45d4-b509-1d6fbbdaf777";
                break;
        }

        givenResponseForAccountRetrieval();
        givenResponseForSavingsGoalDeposit();

        thenRoundupGoalCreationRequestedAndResponseVerified();
        thenVerifyEmptyRequestToAccountRetrieval();
        thenVerifyRequestToSavingsGoalCreation();
    }

    @SneakyThrows
    private void givenResponseForAccountRetrieval()
    {
        if (requestsAccountRetrieval)
        {
            server.enqueue(new MockResponse()
                    .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .setResponseCode(mockedParameters.getMockedStatusCodeAccountRetrieval().value())
                    .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromAccountRetrieval())));
        }
    }

    @SneakyThrows
    private void givenResponseForSavingsGoalDeposit()
    {
        if (requestsSavingsGoalCreation)
        {
            server.enqueue(new MockResponse()
                    .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .setResponseCode(mockedParameters.getMockedStatusCodeFromSavingsDepositCreation().value())
                    .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromSavingsDepositCreation())));
        }
    }

    private void thenRoundupGoalCreationRequestedAndResponseVerified()
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
    private void thenVerifyEmptyRequestToAccountRetrieval()
    {
        if (requestsAccountRetrieval)
        {
            var actualRequest = server.takeRequest();
            assert (actualRequest.getChunkSizes().isEmpty());
        }
    }

    @SneakyThrows
    private void thenVerifyRequestToSavingsGoalCreation()
    {
        if (requestsSavingsGoalCreation)
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
}
