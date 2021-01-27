package com.starling.roundupservice.features.retrieveroundup;

import com.starling.roundupservice.BaseTestIT;
import com.starling.roundupservice.MockedParameters;
import com.starling.roundupservice.retrieve.RetrieveRoundupResponse;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpHeaders;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RetrieveRoundupIT extends BaseTestIT
{
    private boolean requestsSavingsGoalRetrieval;
    private String accountUid;

    @ParameterizedTest(name = "{index}: {0}")
    @ArgumentsSource(RetrieveRoundupProvider.class)
    void retrieveRoundupGoal(final String testName, final MockedParameters mockedParameters)
    {
        this.mockedParameters = mockedParameters;
        switch (testName)
        {
            case "no_roundup":
                requestsSavingsGoalRetrieval = false;
                accountUid = "11111a11-fd4c-45d4-b509-1d6fbbdaf777";
                break;
            case "retrieve_roundup":
            case "unauthorised":
            case "bad_request":
            default:
                requestsSavingsGoalRetrieval = true;
                accountUid = "b2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
        }

        givenResponseForSavingsGoalRetrieval();

        thenRetrieveRoundupGoalRequestedAndResponseVerified();
        thenVerifyEmptyRequestToSavingsGoalRetrieval();
    }

    @SneakyThrows
    private void givenResponseForSavingsGoalRetrieval()
    {
        if (requestsSavingsGoalRetrieval)
        {
            server.enqueue(new MockResponse()
                    .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .setResponseCode(mockedParameters.getMockedStatusCodeFromSavingsGoalRetrieval().value())
                    .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromSavingsGoalRetrieval())));
        }
    }

    private void thenRetrieveRoundupGoalRequestedAndResponseVerified()
    {
        webTestClient.get().uri(String.format(contextPath + PATH_RETRIEVE_ROUNDUP, accountUid))
                .header("Authorization", "Bearer eyJhbGciOiJQUzI1NiIsInpp")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(mockedParameters.getExpectedStatusCodeFromStarlingRoundup())
                .expectBody(RetrieveRoundupResponse.class)
                .isEqualTo(loadResourceAsObject(mockedParameters.getExpectedResponseFromStarlingRoundup(), RetrieveRoundupResponse.class));
    }

    @SneakyThrows
    private void thenVerifyEmptyRequestToSavingsGoalRetrieval()
    {
        if (requestsSavingsGoalRetrieval)
        {
            var actualRequest = server.takeRequest();
            assert (actualRequest.getChunkSizes().isEmpty());
        }
    }
}
