package com.starling.roundupservice.common.savingsgoal.retrieve;

import com.starling.roundupservice.common.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RetrieveSavingsGoalResponse
{
    private final String savingsGoalUid;
    private final String name;
    private final Money target;
    private final Money totalSaved;
    private final int savedPercentage;
}
