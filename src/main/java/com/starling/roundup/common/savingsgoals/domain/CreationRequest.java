package com.starling.roundup.common.savingsgoals.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class CreationRequest {

  private final String name;
  private final String currency;
  private final Money target;
  private final String base64EncodedPhoto;

}
