package com.starling.roundupservice.features.roundupretrieval;

import com.starling.roundupservice.common.account.roundup.service.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.retrieval.SavingsGoalRetrievalService;
import com.starling.roundupservice.retrieval.RoundupRetrievalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetrievalService
{
    private final RoundupAccountService roundupAccountService;
    private final SavingsGoalRetrievalService savingsGoalRetrievalService;

    public RoundupRetrievalResponse retrieveRoundup(final String accountUid, final String bearerToken)
    {
        var roundupAccount = roundupAccountService.retrieveRoundupAccount(accountUid);
        if (roundupAccount.isEmpty())
        {
            throw new ClientException("Roundup retrieval exception: ", String.format(" there is no associated roundup retrieval for the requested account %s.", accountUid));
        }
        else
        {
            var savingsGoalUid = roundupAccount.get().getSavingsGoalUid();
            var savingsGoalAccount = savingsGoalRetrievalService.retrieveSavingsGoal(accountUid, savingsGoalUid, bearerToken);
            return RoundupRetrievalResponse.builder()
                    .roundupSavingsGoalUid(savingsGoalUid)
                    .roundupValue(savingsGoalAccount.getTotalSaved().getMinorUnits())
                    .currency(savingsGoalAccount.getTotalSaved().getCurrency())
                    .roundupMaximum(roundupAccount.get().getMaximumRoundup())
                    .roundupFactor(roundupAccount.get().getRoundupFactor())
                    .completionPercentage(savingsGoalAccount.getPercentageSaved())
                    .build();
        }
    }

}
