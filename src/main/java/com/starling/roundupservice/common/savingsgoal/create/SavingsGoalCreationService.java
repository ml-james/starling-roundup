package com.starling.roundupservice.common.savingsgoal.create;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingsGoalCreationService {

  private final SavingsGoalCreationProvider savingsGoalCreationProvider;

  public SavingsGoalCreationResponse createSavingsGoal(final String currency) {

    return savingsGoalCreationProvider.createSavingsGoal(currency);

  }
}
