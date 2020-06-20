package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingsGoalDepositService {

  private final SavingsGoalDepositProvider savingsGoalDepositProvider;

  public String deposit(final RoundupAccountMapping roundupAccount, final int roundupAmount) {

    var depositResponse = savingsGoalDepositProvider.depositToSavingsGoal(roundupAccount, roundupAmount);

    if (!depositResponse.success) {
      throw new DepositUnsuccessfulException();
    }
    return depositResponse.transferUid;
  }
}
