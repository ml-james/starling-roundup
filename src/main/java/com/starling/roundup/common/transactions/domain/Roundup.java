package com.starling.roundup.common.transactions.domain;

import com.starling.roundup.common.savingsgoals.domain.Money;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
public class Roundup {

  private final String goalCategoryUid;
  private final Money amount;

}
