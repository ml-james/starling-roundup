package com.starling.roundupservice.features.saveroundup;

import com.fasterxml.jackson.databind.JsonNode;
import com.starling.roundupservice.BaseTestIT;
import com.starling.roundupservice.MockedParameters;
import com.starling.roundupservice.save.SaveRoundupRequest;
import com.starling.roundupservice.save.SaveRoundupResponse;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.BodyInserters;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SaveRoundupIT extends BaseTestIT
{
    private boolean requestsAccountRetrieval;
    private boolean requestsSavingsGoalSave;
    private String accountUid;

    @ParameterizedTest(name = "{index}: {0}")
    @ArgumentsSource(SaveRoundupProvider.class)
    void saveRoundupGoal(final String testName, final MockedParameters mockedParameters)
    {
        this.mockedParameters = mockedParameters;
        switch (testName)
        {
            case "update_roundup":
                requestsAccountRetrieval = false;
                requestsSavingsGoalSave = true;
                accountUid = "b2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
            case "unauthorised":
                requestsSavingsGoalSave = false;
                requestsAccountRetrieval = true;
                accountUid = "55198b91-fd4c-45d4-b509-1d6fbbdaf777";
                break;
            case "bad_request":
                requestsSavingsGoalSave = true;
                requestsAccountRetrieval = true;
                accountUid = "11111a11-fd4c-45d4-b509-1d6fbbdaf777";
                break;
            case "create_roundup":
            default:
                requestsSavingsGoalSave = true;
                requestsAccountRetrieval = true;
                accountUid = "22222b22-fd4c-45d4-b509-1d6fbbdaf777";
                break;
        }

        givenResponseForAccountRetrieval();
        givenResponseForSavingsGoalSave();

        thenSaveRoundupGoalRequestedAndResponseVerified();
        thenVerifyEmptyRequestToAccountRetrieval();
        thenVerifyRequestToSavingsGoalSave();
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
    private void givenResponseForSavingsGoalSave()
    {
        if (requestsSavingsGoalSave)
        {
            server.enqueue(new MockResponse()
                    .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .setResponseCode(mockedParameters.getMockedStatusCodeFromSavingsDepositSave().value())
                    .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromSavingsDepositSave())));
        }
    }

    private void thenSaveRoundupGoalRequestedAndResponseVerified()
    {
        webTestClient.put().uri(String.format(contextPath + PATH_SAVE_ROUNDUP, accountUid))
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJQUzI1NiIsInpp")
                .accept(APPLICATION_JSON)
                .body(BodyInserters.fromValue(loadResourceAsObject(mockedParameters.getRequestToStarlingRoundup(), SaveRoundupRequest.class)))
                .exchange()
                .expectStatus().isEqualTo(mockedParameters.getExpectedStatusCodeFromStarlingRoundup())
                .expectBody(SaveRoundupResponse.class)
                .isEqualTo(loadResourceAsObject(mockedParameters.getExpectedResponseFromStarlingRoundup(), SaveRoundupResponse.class));
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
    private void thenVerifyRequestToSavingsGoalSave()
    {
        if (requestsSavingsGoalSave)
        {
            var actualRequest = server.takeRequest();

            JsonNode actual = jsonMapper.readTree(actualRequest.getBody().readUtf8());
            JsonNode expected = jsonMapper.readTree(loadResourceAsString(mockedParameters.getExpectedRequestToSavingsDepositSave()));

            assertEquals(
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected),
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                    false
            );
        }
    }
}
