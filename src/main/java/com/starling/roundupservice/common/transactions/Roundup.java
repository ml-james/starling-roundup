package com.starling.roundupservice.common.transactions;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class Roundup {

  int roundupAmount;
  String weekEnd;

}
