package com.starling.roundupservice.common.savingsgoal.deposit;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SavingsGoalDepositRequest {

  private final Money amount;

}
