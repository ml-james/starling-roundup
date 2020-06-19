package com.starling.roundupservice.creation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor
@JsonInclude
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoundupCreationRequest {

  @JsonProperty("accountUid")
  String accountUid;
  @JsonProperty("roundupMaximum")
  int roundupMaximum;
  @JsonProperty("roundupFactor")
  int roundupFactor;
}
