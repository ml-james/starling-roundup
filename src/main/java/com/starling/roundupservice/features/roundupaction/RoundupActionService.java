package com.starling.roundupservice.features.roundupaction;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.common.account.fundconfirmation.FundConfirmationService;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.account.roundup.RoundupStateService;
import com.starling.roundupservice.action.State;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.deposit.DepositSavingsGoalService;
import com.starling.roundupservice.common.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundupActionService
{
    private final RoundupAccountService roundupAccountService;
    private final TransactionService transactionService;
    private final FundConfirmationService fundConfirmationService;
    private final DepositSavingsGoalService depositSavingsGoalService;
    private final RoundupStateService roundupStateService;

    public RoundupActionResponse performRoundup(final String accountUid, final String bearerToken)
    {
        var roundupAccount = getRoundupAccount(accountUid);
        var roundup = transactionService.getLatestRoundup(roundupAccount, bearerToken);

        return persistRoundupAction(accountUid, bearerToken, roundupAccount, roundup);
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

    private RoundupActionResponse persistRoundupAction(String accountUid, String bearerToken, RoundupAccountMapping roundupAccount, com.starling.roundupservice.common.transaction.Roundup roundup)
    {
        if (roundup.roundupAmount == 0)
        {
            roundupStateService.insertState(roundupAccount.getRoundupUid(), null, State.ZERO_ROUNDUP, roundup.getWeekEnd());
            return RoundupActionResponseTransformer.transform(State.ZERO_ROUNDUP);
        }
        else if (fundConfirmationService.sufficientFunds(accountUid, roundup.getRoundupAmount(), bearerToken))
        {
            var transferUid = depositSavingsGoalService.deposit(roundupAccount, roundup.getRoundupAmount(), bearerToken);
            roundupStateService.insertState(roundupAccount.getRoundupUid(), transferUid, State.TRANSFERRED, roundup.getWeekEnd());
            return RoundupActionResponseTransformer.transform(State.TRANSFERRED, transferUid, roundup.getRoundupAmount());
        }
        else
        {
            roundupStateService.insertState(roundupAccount.getRoundupUid(), null, State.INSUFFICIENT_FUNDS, roundup.getWeekEnd());
            return RoundupActionResponseTransformer.transform(State.INSUFFICIENT_FUNDS);
        }
    }
}
