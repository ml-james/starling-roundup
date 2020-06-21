package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingsGoalDepositService {

  private final SavingsGoalDepositProvider savingsGoalDepositProvider;

  public String deposit(final RoundupAccountMapping roundupAccount, final int roundupAmount) {

    var depositResponse = savingsGoalDepositProvider.depositToSavingsGoal(roundupAccount, roundupAmount);

    if (!depositResponse.success) {
      throw new ServerException("Deposit to savings account: ", String
          .format("depositing to roundup goal %s for account %s was unsuccessful", roundupAccount.getSavingsGoalUid(),
              roundupAccount.getAccountUid()));
    }
    return depositResponse.transferUid;
  }
}
