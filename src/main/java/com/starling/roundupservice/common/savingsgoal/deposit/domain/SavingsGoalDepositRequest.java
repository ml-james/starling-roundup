package com.starling.roundupservice.common.savingsgoal.deposit.domain;

import com.starling.roundupservice.common.savingsgoal.Money;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SavingsGoalDepositRequest
{
    private final Money amount;
}
