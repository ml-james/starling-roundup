package com.starling.roundupservice.features.saveroundup;

import com.starling.roundupservice.MockedParameters;
import okhttp3.Headers;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class SaveRoundupProvider implements ArgumentsProvider
{
    private final String delimeter = "/";

    public Stream<? extends Arguments> provideArguments(ExtensionContext extensioncontext) {
        return Stream.of(
                Arguments.of("create_roundup", getMockedParameters("features/saveroundup/createRoundupPath", HttpStatus.OK, HttpStatus.OK, HttpStatus.OK)),
                Arguments.of("bad_request", getMockedParameters("features/saveroundup/failurePath", HttpStatus.BAD_REQUEST, HttpStatus.OK, HttpStatus.BAD_REQUEST)),
                Arguments.of("unauthorised", getMockedParameters("features/saveroundup/unauthorisedPath", HttpStatus.BAD_REQUEST, HttpStatus.FORBIDDEN, null)),
                Arguments.of("update_roundup", getMockedParameters("features/saveroundup/updateRoundupPath", HttpStatus.OK, null, HttpStatus.OK))
                );
    }

    private MockedParameters getMockedParameters(final String path, final HttpStatus starlingRoundupStatus, final HttpStatus accountRetrievalStatus, final HttpStatus savingsGoalSaveStatus)
    {
        return MockedParameters.builder()
                .expectedStatusCodeFromStarlingRoundup(starlingRoundupStatus)
                .requestToStarlingRoundup(String.join(delimeter, path, "saveRoundup_request.json"))
                .expectedResponseFromStarlingRoundup(String.join(delimeter, path, "saveRoundup_response.json"))

                .mockedResponseFromAccountRetrieval(String.join(delimeter, path, "accountRetrieval_response.json"))
                .accountRetrievalResponseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeAccountRetrieval(accountRetrievalStatus)

                .expectedRequestToSavingsDepositSave(String.join(delimeter, path, "saveSavingsGoal_request.json"))
                .mockedResponseFromSavingsDepositSave(String.join(delimeter, path, "saveSavingsGoal_response.json"))
                .savingsDepositSaveResponseHeaders(Headers.of(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_VALUE))
                .mockedStatusCodeFromSavingsDepositSave(savingsGoalSaveStatus)
                .build();
    }
}
