package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DepositSavingsGoalRequest
{
    private final Money amount;
}
