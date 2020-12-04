package com.starling.roundupservice.common;

import com.starling.roundupservice.common.account.accountretrieval.Account;
import com.starling.roundupservice.common.savingsgoal.create.domain.SavingsGoalCreationRequest;
import com.starling.roundupservice.common.savingsgoal.Money;
import com.starling.roundupservice.common.savingsgoal.deposit.domain.SavingsGoalDepositRequest;
import com.starling.roundupservice.creation.RoundupCreationRequest;

public class RequestBuilder
{
    private static final String ROUND_UP_GOAL_NAME = "Round-me-up!";
    private static final String BASE_64_ENCODED_PHOTO = "aHR0cHM6Ly95b3V0dS5iZS9kUXc0dzlXZ1hjUQ==";

    public static SavingsGoalCreationRequest createRequest(final RoundupCreationRequest creationRequest, final Account account)
    {
        var money = Money.builder()
                .currency(account.getCurrency())
                .minorUnits(creationRequest.getGoal())
                .build();

        return SavingsGoalCreationRequest.builder()
                .name(ROUND_UP_GOAL_NAME)
                .currency(account.getCurrency())
                .target(money)
                .base64EncodedPhoto(BASE_64_ENCODED_PHOTO)
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
