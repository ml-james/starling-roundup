package com.starling.roundupservice;

import lombok.Builder;
import lombok.Data;
import okhttp3.Headers;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class MockedParameters
{
    private String requestToStarlingRoundup;
    private String expectedResponseFromStarlingRoundup;
    private HttpStatus expectedStatusCodeFromStarlingRoundup;

    private String mockedResponseFromAccountRetrieval;
    private HttpStatus mockedStatusCodeAccountRetrieval;
    private Headers responseHeaders;

    private String expectedRequestToSavingsDepositCreation;
    private String mockedResponseFromSavingsDepositCreation;
    private HttpStatus mockedStatusCodeFromSavingsDepositCreation;
    private Headers secondResponseHeaders;
}

