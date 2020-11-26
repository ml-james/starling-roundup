package com.starling.roundupservice.features.roundupcreation;

import com.starling.roundupservice.MockedParameters;
import okhttp3.Headers;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class RoundupCreationProvider implements ArgumentsProvider
{
    private final String delimeter = "/";

    public Stream<? extends Arguments> provideArguments(ExtensionContext extensioncontext) {
        return Stream.of(
                Arguments.of("Success Path", getMockedParameters("features/roundupcreation/successPath", HttpStatus.OK, HttpStatus.OK, HttpStatus.OK)),
                Arguments.of("Bad Request to Starling API", getMockedParameters("features/roundupcreation/failurePath", HttpStatus.BAD_REQUEST, HttpStatus.OK, HttpStatus.BAD_REQUEST)),
                Arguments.of("Failed Authorization Path", getMockedParameters("features/roundupcreation/unauthorisedPath", HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN, null))
                );
    }

    private MockedParameters getMockedParameters(String path, HttpStatus starlingRoundupStatus, HttpStatus accountRetrievalStatus, HttpStatus savingsGoalCreationStatus)
    {
        return MockedParameters.builder()
                .expectedStatusCodeFromStarlingRoundup(starlingRoundupStatus)
                .requestToStarlingRoundup(String.join(delimeter, path, "createRoundup_request.json"))
                .expectedResponseFromStarlingRoundup(String.join(delimeter, path, "createRoundup_response.json"))

                .expectedRequestToAccountRetrieval(String.join(delimeter, path, "accountRetrieval_request.json"))
                .mockedResponseFromAccountRetrieval(String.join(delimeter, path, "accountRetrieval_response.json"))
                .responseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeAccountRetrieval(accountRetrievalStatus)

                .expectedRequestToSavingsDepositCreation(String.join(delimeter, path, "createSavingsGoal_request.json"))
                .mockedResponseFromSavingsDepositCreation(String.join(delimeter, path, "createSavingsGoal_response.json"))
                .secondResponseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeFromSavingsDepositCreation(savingsGoalCreationStatus)
                .build();

    }
}
