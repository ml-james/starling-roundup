package com.starling.roundupservice.features.retrieveroundup;

import com.starling.roundupservice.MockedParameters;
import okhttp3.Headers;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RetrieveRoundupProvider implements ArgumentsProvider
{
    private final String delimeter = "/";

    public Stream<? extends Arguments> provideArguments(ExtensionContext extensioncontext) {
        return Stream.of(
                Arguments.of("retrieve_roundup", getMockedParameters("features/retrieveroundup/retrieveRoundupPath", HttpStatus.OK, HttpStatus.OK)),
                Arguments.of("no_roundup", getMockedParameters("features/retrieveroundup/noRoundupPath", HttpStatus.BAD_REQUEST, null)),
                Arguments.of("unauthorised", getMockedParameters("features/retrieveroundup/unauthorisedPath", HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN)),
                Arguments.of("bad_request", getMockedParameters("features/retrieveroundup/failurePath", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST))
                );
    }

    private MockedParameters getMockedParameters(final String path, final HttpStatus starlingRoundupStatus, final HttpStatus savingsGoalRetrievalStatus)
    {
        return MockedParameters.builder()
                .expectedStatusCodeFromStarlingRoundup(starlingRoundupStatus)
                .requestToStarlingRoundup(String.join(delimeter, path, "retrieveRoundup_request.json"))
                .expectedResponseFromStarlingRoundup(String.join(delimeter, path, "retrieveRoundup_response.json"))

                .expectedRequestToSavingsGoalRetrieval(String.join(delimeter, path, "retrieveSavingsGoal_request.json"))
                .mockedResponseFromSavingsGoalRetrieval(String.join(delimeter, path, "retrieveSavingsGoal_response.json"))
                .savingsGoalRetrievalResponseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeFromSavingsGoalRetrieval(savingsGoalRetrievalStatus)
                .build();
    }
}
