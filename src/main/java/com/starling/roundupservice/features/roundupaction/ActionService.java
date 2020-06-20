package com.starling.roundupservice.features.roundupaction;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.common.account.fundconfirmation.FundConfirmationService;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.account.roundup.RoundupResponseTransformer;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.action.State;
import com.starling.roundupservice.common.savingsgoal.deposit.SavingsGoalDepositService;
import com.starling.roundupservice.common.transactions.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionService {

  private final RoundupAccountService roundupAccountService;
  private final TransactionService transactionService;
  private final FundConfirmationService fundConfirmationService;
  private final SavingsGoalDepositService savingsGoalDepositService;
  private final RoundupStateService roundupStateService;

  public RoundupActionResponse performRoundup(final String accountUid) {

    var roundupAccount = getRoundupAccount(accountUid);

    if (!roundupStateService.isRoundupDue(roundupAccount.getRoundupUid())) {
      throw new NoRoundupRequiredException();
    }

    var roundUp = transactionService.getLatestRoundup(roundupAccount);

    if (fundConfirmationService.sufficientFunds(accountUid, roundUp)) {
      var transferUid = savingsGoalDepositService.deposit(roundupAccount, roundUp);
      roundupStateService.insertState(roundupAccount.getRoundupUid(), State.SUCCESSFUL, transferUid);
      return RoundupResponseTransformer.transform(State.SUCCESSFUL, transferUid, roundUp);
    } else {
      roundupStateService.insertState(roundupAccount.getRoundupUid(), State.INSUFFICIENT_FUNDS, null);
      return RoundupResponseTransformer.transform(State.INSUFFICIENT_FUNDS);
    }
  }

  private RoundupAccountMapping getRoundupAccount(final String accountUid) {
    var roundupAccount = roundupAccountService.retrieveRoundupAccount(accountUid);
    if (roundupAccount.isEmpty()) {
      throw new NoRoundupGoalFoundException();
    }
    return roundupAccount.get();
  }
}
