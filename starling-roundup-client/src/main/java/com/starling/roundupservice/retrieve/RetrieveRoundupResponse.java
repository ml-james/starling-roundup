package com.starling.roundupservice.retrieve;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Map;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RetrieveRoundupResponse
{
    String roundupSavingsGoalUid;
    int roundupValue;
    int roundupGoal;
    String currency;
    int roundupMaximum;
    int roundupFactor;
    int savedPercentage;
    Map<String, String> errors;
}
