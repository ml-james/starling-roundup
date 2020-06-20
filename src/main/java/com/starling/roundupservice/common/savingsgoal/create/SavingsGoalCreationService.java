package com.starling.roundupservice.common.savingsgoal.create;

import com.starling.roundupservice.common.savingsgoal.deposit.Money;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingsGoalCreationService {

  private final SavingsGoalCreationProvider savingsGoalCreationProvider;

  public SavingsGoalCreationResponse createSavingsGoal(final RoundupCreationRequest creationRequest, final String accountUidCurrency) {

    var money = Money.builder()
        .currency(creationRequest.getCurrency())
        .minorUnits(creationRequest.getGoal())
        .build();

    var savingsGoalRequest = SavingsGoalCreationRequest.builder()
        .currency(accountUidCurrency)
        .target(money)
        .base64EncodedPhoto(creationRequest.getBase64EncodedPhoto())
        .build();

    return savingsGoalCreationProvider.createSavingsGoal(savingsGoalRequest);

  }
}
