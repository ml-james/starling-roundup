package com.starling.roundupservice.features.retrieveroundup;

import com.starling.roundupservice.common.account.roundup.service.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.retrieve.RetrieveSavingsGoalService;
import com.starling.roundupservice.retrieve.RetrieveRoundupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetrievalService
{
    private final RoundupAccountService roundupAccountService;
    private final RetrieveSavingsGoalService retrieveSavingsGoalService;

    public RetrieveRoundupResponse retrieveRoundup(final String accountUid, final String bearerToken)
    {
        var roundupAccount = roundupAccountService.retrieveRoundupAccount(accountUid);
        if (roundupAccount.isEmpty())
        {
            throw new ClientException("Roundup retrieval exception: ", String.format(" there is no associated roundup retrieval for the requested account %s.", accountUid));
        }
        else
        {
            var savingsGoalUid = roundupAccount.get().getSavingsGoalUid();
            var savingsGoalAccount = retrieveSavingsGoalService.retrieveSavingsGoal(accountUid, savingsGoalUid, bearerToken);
            return RetrieveRoundupResponse.builder()
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
