package com.starling.roundupservice.common.savingsgoal.deposit;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TopUpRequest {

  private final Money amount;

}
