package com.starling.roundupservice.features.roundupaction;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.action.Status;
import com.starling.roundupservice.common.accounts.fundconfirmation.FundConfirmationService;
import com.starling.roundupservice.common.accounts.roundup.RoundupAccountService;
import com.starling.roundupservice.common.savingsgoal.deposit.SavingsGoalDepositService;
import com.starling.roundupservice.common.transactions.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionService {

  private final RoundupAccountService roundupAccountService;
  private final TransactionsService transactionsService;
  private final FundConfirmationService fundConfirmationService;
  private final SavingsGoalDepositService savingsGoalDepositService;

  public RoundupActionResponse performRoundup(final String accountUid) {

    var roundupAccount = roundupAccountService.retrieveRoundupAccount(accountUid);
    if (roundupAccount.isEmpty()) {
      throw new NoRoundupGoalFoundException();
    }

    var roundupValue = transactionsService.getRoundupValue(roundupAccount.get());
    if (fundConfirmationService.sufficientFunds(accountUid, roundupValue)) {
      savingsGoalDepositService.deposit(roundupValue, roundupAccount.get().getSavingsGoalUid());
    }

  }
}
