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
    private Headers accountRetrievalResponseHeaders;

    private String expectedRequestToSavingsDepositSave;
    private String mockedResponseFromSavingsDepositSave;
    private HttpStatus mockedStatusCodeFromSavingsDepositSave;
    private Headers savingsDepositSaveResponseHeaders;

    private String expectedRequestToSavingsGoalRetrieval;
    private String mockedResponseFromSavingsGoalRetrieval;
    private HttpStatus mockedStatusCodeFromSavingsGoalRetrieval;
    private Headers savingsGoalRetrievalResponseHeaders;

}

