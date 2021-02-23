package com.starling.roundupservice.features.roundupaction;

import com.starling.roundupservice.BaseTestIT;
import com.starling.roundupservice.MockedParameters;
import com.starling.roundupservice.action.RoundupActionResponse;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.http.HttpHeaders;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RoundupActionIT extends BaseTestIT
{
    private boolean requestsTransactionsProvider;
    private boolean requestsFundConfirmationProvider;
    private boolean requestsDepositSavingsGoal;
    private String accountUid;

    @ParameterizedTest(name = "{index}: {0}")
    @ArgumentsSource(RoundupActionProvider.class)
    void roundupAction(final String testName, final MockedParameters mockedParameters)
    {
        this.mockedParameters = mockedParameters;
        switch (testName)
        {
            case "roundup_action":
                requestsTransactionsProvider = true;
                requestsFundConfirmationProvider = true;
                requestsDepositSavingsGoal = true;
                accountUid = "d2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
            case "insufficient_funds":
                requestsTransactionsProvider = true;
                requestsFundConfirmationProvider = true;
                requestsDepositSavingsGoal = false;
                accountUid = "b2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
            case "zero_roundup":
                requestsTransactionsProvider = true;
                requestsFundConfirmationProvider = false;
                requestsDepositSavingsGoal = false;
                accountUid = "c2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
            case "not_due":
                requestsTransactionsProvider = false;
                requestsFundConfirmationProvider = false;
                requestsDepositSavingsGoal = false;
                accountUid = "b2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
            case "unauthorised":
                requestsTransactionsProvider = true;
                requestsFundConfirmationProvider = false;
                requestsDepositSavingsGoal = false;
                accountUid = "a2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
            case "bad_request":
            default:
                requestsTransactionsProvider = true;
                requestsFundConfirmationProvider = true;
                requestsDepositSavingsGoal = true;
                accountUid = "a2191626-c67c-4a4b-aef9-3b1b80b65fdc";
                break;
        }

        givenResponseForRetrieveTransactions();
        givenResponseForFundConfirmation();
        givenResponseForDepositSavingsGoal();

        thenRoundupActionRequestedAndResponseVerified();
        thenVerifyEmptyRequestToRetrieveTransactions();
        thenVerifyEmptyRequestToFundConfirmation();
        thenVerifyRequestToDepositSavingsGoal();
    }

    @SneakyThrows
    private void givenResponseForRetrieveTransactions()
    {
        if (requestsTransactionsProvider)
        {
            server.enqueue(new MockResponse()
                    .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .setResponseCode(mockedParameters.getMockedStatusCodeFromTransactionsProvider().value())
                    .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromTransactionsProvider())));
        }
    }

    @SneakyThrows
    private void givenResponseForFundConfirmation()
    {
        if (requestsFundConfirmationProvider)
        {
            server.enqueue(new MockResponse()
                    .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .setResponseCode(mockedParameters.getMockedStatusCodeFromFundConfirmation().value())
                    .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromFundConfirmation())));
        }
    }

    @SneakyThrows
    private void givenResponseForDepositSavingsGoal()
    {
        if (requestsDepositSavingsGoal)
        {
            server.enqueue(new MockResponse()
                    .setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE)
                    .setResponseCode(mockedParameters.getMockedStatusCodeFromDepositSavingsGoal().value())
                    .setBody(loadResourceAsString(mockedParameters.getMockedResponseFromDepositSavingsGoal())));
        }
    }

    private void thenRoundupActionRequestedAndResponseVerified()
    {
        webTestClient.post().uri(String.format(contextPath + PATH_ROUNDUP_ACTION, accountUid))
                .contentType(APPLICATION_JSON)
                .header("Authorization", "Bearer eyJhbGciOiJQUzI1NiIsInpp")
                .exchange()
                .expectStatus().isEqualTo(mockedParameters.getExpectedStatusCodeFromStarlingRoundup())
                .expectBody(RoundupActionResponse.class)
                .isEqualTo(loadResourceAsObject(mockedParameters.getExpectedResponseFromStarlingRoundup(), RoundupActionResponse.class));
    }

    @SneakyThrows
    private void thenVerifyEmptyRequestToRetrieveTransactions()
    {
        if (requestsTransactionsProvider)
        {
            var actualRequest = server.takeRequest();
            assert (actualRequest.getChunkSizes().isEmpty());
        }
    }

    @SneakyThrows
    private void thenVerifyEmptyRequestToFundConfirmation()
    {
        if (requestsFundConfirmationProvider)
        {
            var actualRequest = server.takeRequest();
            assert (actualRequest.getChunkSizes().isEmpty());
        }
    }

    @SneakyThrows
    private void thenVerifyRequestToDepositSavingsGoal()
    {
        if (requestsDepositSavingsGoal)
        {
            var actualRequest = server.takeRequest();

            var actual = jsonMapper.readTree(actualRequest.getBody().readUtf8());
            var expected = jsonMapper.readTree(loadResourceAsString(mockedParameters.getExpectedRequestToDepositSavingsGoal()));

            assertEquals(
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(expected),
                    jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(actual),
                    false
            );
        }
    }
}
