package com.starling.roundup.common.savingsgoals.domain;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TopUpRequest {

  private final Money amount;

}
