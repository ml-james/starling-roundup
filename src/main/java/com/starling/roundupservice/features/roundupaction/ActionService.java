package com.starling.roundupservice.features.roundupaction;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.common.account.fundconfirmation.FundConfirmationService;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.account.roundup.RoundupResponseTransformer;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.action.State;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.deposit.SavingsGoalDepositService;
import com.starling.roundupservice.common.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActionService
{

    private final RoundupAccountService roundupAccountService;
    private final TransactionService transactionService;
    private final FundConfirmationService fundConfirmationService;
    private final SavingsGoalDepositService savingsGoalDepositService;
    private final RoundupStateService roundupStateService;

    public RoundupActionResponse performRoundup(final String accountUid)
    {

        var roundupAccount = getRoundupAccount(accountUid);
        var roundUp = transactionService.getLatestRoundup(roundupAccount);

        if (fundConfirmationService.sufficientFunds(accountUid, roundUp.getRoundupAmount()))
        {
            var transferUid = savingsGoalDepositService.deposit(roundupAccount, roundUp.getRoundupAmount());
            roundupStateService.insertState(roundupAccount.getRoundupUid(), State.TRANSFERRED, transferUid, roundUp.getWeekEnd());
            return RoundupResponseTransformer.transform(State.TRANSFERRED, transferUid, roundUp.getRoundupAmount());
        }
        else
        {
            roundupStateService.insertState(roundupAccount.getRoundupUid(), State.INSUFFICIENT_FUNDS, null, roundUp.getWeekEnd());
            return RoundupResponseTransformer.transform(State.INSUFFICIENT_FUNDS);
        }
    }

    private RoundupAccountMapping getRoundupAccount(final String accountUid)
    {
        var roundupAccount = roundupAccountService.retrieveRoundupAccount(accountUid);
        if (roundupAccount.isEmpty())
        {
            throw new ClientException("Retrieve roundup account error: ", String.format("no roundup account exists for account %s", accountUid));
        }
        return roundupAccount.get();
    }
}
