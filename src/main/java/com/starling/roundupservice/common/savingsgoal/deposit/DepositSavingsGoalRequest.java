package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.Money;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DepositSavingsGoalRequest
{
    private final Money amount;
}
