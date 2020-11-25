package com.starling.roundupservice.common;

import com.starling.roundupservice.common.savingsgoal.create.domain.SavingsGoalCreationRequest;
import com.starling.roundupservice.common.savingsgoal.deposit.domain.Money;
import com.starling.roundupservice.common.savingsgoal.deposit.domain.SavingsGoalDepositRequest;
import com.starling.roundupservice.creation.RoundupCreationRequest;

public class RequestBuilder
{
    public static SavingsGoalCreationRequest createRequest(final RoundupCreationRequest creationRequest, final String accountUidCurrency)
    {
        var money = Money.builder()
                .currency(creationRequest.getCurrency())
                .minorUnits(creationRequest.getGoal())
                .build();

        return SavingsGoalCreationRequest.builder()
                .name(creationRequest.getGoalName())
                .currency(accountUidCurrency)
                .target(money)
                .base64EncodedPhoto(creationRequest.getBase64EncodedPhoto())
                .build();
    }

    public static SavingsGoalDepositRequest createRequest(final String currency, final int goal)
    {
        var money = Money.builder()
                .currency(currency)
                .minorUnits(goal)
                .build();

        return new SavingsGoalDepositRequest(money);
    }
}
