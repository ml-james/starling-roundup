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
        var roundUp = transactionService.getLatestRoundup(roundupAccount, bearerToken);

        // TODO: let's deal with roundup 0 edge case
        if (fundConfirmationService.sufficientFunds(accountUid, roundUp.getRoundupAmount(), bearerToken))
        {
            var transferUid = depositSavingsGoalService.deposit(roundupAccount, roundUp.getRoundupAmount(), bearerToken);
            roundupStateService.insertState(roundupAccount.getRoundupUid(), transferUid, State.TRANSFERRED, roundUp.getWeekEnd());
            return RoundupActionResponseTransformer.transform(State.TRANSFERRED, transferUid, roundUp.getRoundupAmount());
        }
        else
        {
            roundupStateService.insertState(roundupAccount.getRoundupUid(), null, State.INSUFFICIENT_FUNDS, roundUp.getWeekEnd());
            return RoundupActionResponseTransformer.transform(State.INSUFFICIENT_FUNDS);
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
