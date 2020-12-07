package com.starling.roundupservice.save;

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
public class SaveRoundupRequest
{
  @JsonProperty("goal")
  int goal;
  @JsonProperty("roundupMaximum")
  int roundupMaximum;
  @JsonProperty("roundupFactor")
  int roundupFactor;
}
