package com.starling.roundupservice.features.roundupaction;

import com.starling.roundupservice.MockedParameters;
import okhttp3.Headers;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RoundupActionProvider implements ArgumentsProvider
{
    private final String delimeter = "/";

    public Stream<? extends Arguments> provideArguments(ExtensionContext extensioncontext) {
        return Stream.of(
//                Arguments.of("roundup_action", getMockedParameters("features/roundupaction/roundupSussPath", HttpStatus.OK, HttpStatus.OK, HttpStatus.OK)),
                Arguments.of("insufficient_funds", getMockedParameters("features/roundupaction/insufficientFundsPath", HttpStatus.OK, HttpStatus.OK, HttpStatus.OK, null)),
                Arguments.of("not_due", getMockedParameters("features/roundupaction/roundupNotDuePath", HttpStatus.BAD_REQUEST, null, null, null)),
                Arguments.of("unauthorised", getMockedParameters("features/roundupaction/unauthorisedPath", HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN, null, null))
//                Arguments.of("bad_request", getMockedParameters("features/roundupaction/failurePath", HttpStatus.OK, null, HttpStatus.OK))
                );
    }

    private MockedParameters getMockedParameters(final String path, final HttpStatus starlingRoundupStatus, final HttpStatus transactionsProviderStatus, final HttpStatus sufficientFundsStatus, final HttpStatus depositSavingsGoalStatus)
    {
        return MockedParameters.builder()
                .expectedStatusCodeFromStarlingRoundup(starlingRoundupStatus)
                .expectedResponseFromStarlingRoundup(String.join(delimeter, path, "roundupAction_response.json"))

                .mockedResponseFromTransactionsProvider(String.join(delimeter, path, "getTransactions_response.json"))
                .transactionsProviderResponseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeFromTransactionsProvider(transactionsProviderStatus)

                .mockedResponseFromFundConfirmation(String.join(delimeter, path, "fundConfirmation_response.json"))
                .fundConfirmationResponseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeFromFundConfirmation(sufficientFundsStatus)

                .expectedRequestToDepositSavingsGoal(String.join(delimeter, path, "depositSavingsGoal_request.json"))
                .mockedResponseFromDepositSavingsGoal(String.join(delimeter, path, "depositSavingsGoal_response.json"))
                .depositSavingsGoalResponseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeFromDepositSavingsGoal(depositSavingsGoalStatus)

                .build();
    }
}
