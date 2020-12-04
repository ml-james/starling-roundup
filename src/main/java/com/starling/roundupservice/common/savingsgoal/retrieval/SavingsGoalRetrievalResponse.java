package com.starling.roundupservice.common.savingsgoal.retrieval;

import com.starling.roundupservice.common.savingsgoal.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SavingsGoalRetrievalResponse
{
    private final String savingsGoalUid;
    private final String name;
    private final Money target;
    private final Money totalSaved;
    private final int percentageSaved;
}
