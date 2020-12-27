package com.starling.roundupservice.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoundupActionResponse
{
  @JsonProperty("roundupState")
  State roundupState;
  @JsonProperty("transferUid")
  String transferUid;
  @JsonProperty("amount")
  int amount;
  @JsonProperty("error")
  Map<String, String> error;
}
