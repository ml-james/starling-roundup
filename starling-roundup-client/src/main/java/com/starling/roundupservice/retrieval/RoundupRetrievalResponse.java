package com.starling.roundupservice.retrieval;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Map;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoundupRetrievalResponse
{
    @JsonProperty("roundupSavingsGoalUid")
    String roundupSavingsGoalUid;
    @JsonProperty("roundupValue")
    int roundupValue;
    @JsonProperty("roundupGoal")
    int roundupGoal;
    @JsonProperty("currency")
    String currency;
    @JsonProperty("roundupMaximum")
    int roundupMaximum;
    @JsonProperty("roundupFactor")
    int roundupFactor;
    @JsonProperty("completionPercentage")
    int completionPercentage;
    @JsonProperty("errors")
    Map<String, String> errors;
}
