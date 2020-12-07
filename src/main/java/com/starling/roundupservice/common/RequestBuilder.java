package com.starling.roundupservice.common;

import com.starling.roundupservice.common.savingsgoal.deposit.domain.DepositSavingsGoalRequest;
import com.starling.roundupservice.common.savingsgoal.save.domain.SavingsGoalSaveRequest;
import com.starling.roundupservice.save.SaveRoundupRequest;

public class RequestBuilder
{
    private static final String ROUND_UP_GOAL_NAME = "Round-me-up!";
    private static final String BASE_64_ENCODED_PHOTO = "aHR0cHM6Ly95b3V0dS5iZS9kUXc0dzlXZ1hjUQ==";

    public static SavingsGoalSaveRequest saveRequest(final SaveRoundupRequest saveRequest, final String currency)
    {
        var money = Money.builder()
                .currency(currency)
                .minorUnits(saveRequest.getGoal())
                .build();

        return SavingsGoalSaveRequest.builder()
                .name(ROUND_UP_GOAL_NAME)
                .currency(currency)
                .target(money)
                .base64EncodedPhoto(BASE_64_ENCODED_PHOTO)
                .build();
    }

    public static DepositSavingsGoalRequest saveRequest(final String currency, final int goal)
    {
        var money = Money.builder()
                .currency(currency)
                .minorUnits(goal)
                .build();

        return new DepositSavingsGoalRequest(money);
    }
}
