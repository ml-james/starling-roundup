package com.starling.roundupservice.creation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoundupCreationRequest {

  @JsonProperty("goalName")
  String goalName;
  @JsonProperty("currency")
  String currency;
  @JsonProperty("goal")
  int goal;
  @JsonProperty("roundupMaximum")
  int roundupMaximum;
  @JsonProperty("roundupFactor")
  int roundupFactor;
  @JsonProperty("base64EncodedPhoto")
  String base64EncodedPhoto;
}
