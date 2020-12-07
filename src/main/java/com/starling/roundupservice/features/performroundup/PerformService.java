package com.starling.roundupservice.features.performroundup;

import com.starling.roundupservice.perform.PerformRoundupResponse;
import com.starling.roundupservice.common.account.fundconfirmation.FundConfirmationService;
import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.service.RoundupAccountService;
import com.starling.roundupservice.common.account.roundup.service.RoundupStateService;
import com.starling.roundupservice.perform.State;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.deposit.DepositSavingsGoalService;
import com.starling.roundupservice.common.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PerformService
{
    private final RoundupAccountService roundupAccountService;
    private final TransactionService transactionService;
    private final FundConfirmationService fundConfirmationService;
    private final DepositSavingsGoalService depositSavingsGoalService;
    private final RoundupStateService roundupStateService;

    public PerformRoundupResponse performRoundup(final String accountUid, final String bearerToken)
    {
        var roundupAccount = getRoundupAccount(accountUid);
        var roundUp = transactionService.getLatestRoundup(roundupAccount, bearerToken);

        if (fundConfirmationService.sufficientFunds(accountUid, roundUp.getRoundupAmount(), bearerToken))
        {
            var transferUid = depositSavingsGoalService.deposit(roundupAccount, roundUp.getRoundupAmount(), bearerToken);
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
