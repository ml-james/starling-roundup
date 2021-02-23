package com.starling.roundupservice.features.saveroundup;

import com.starling.roundupservice.common.account.accountretrieval.RetrieveAccountService;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.save.CreateSavingsGoalService;
import com.starling.roundupservice.common.savingsgoal.save.UpdateSavingsGoalService;
import com.starling.roundupservice.save.SaveRoundupRequest;
import com.starling.roundupservice.save.SaveRoundupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveRoundupService
{
    private final RoundupAccountService roundupAccountService;
    private final CreateSavingsGoalService createSavingsGoalService;
    private final UpdateSavingsGoalService updateSavingsGoalService;
    private final RetrieveAccountService retrieveAccountService;

    public SaveRoundupResponse saveRoundupGoal(final SaveRoundupRequest saveRequest,
                                  final String accountUid,
                                  final String bearerToken)
    {
        var roundupAccountMapping = roundupAccountService.retrieveRoundupAccount(accountUid);
        if (roundupAccountMapping.isPresent())
        {
            return updateSavingsGoalService.updateSavingsGoal(saveRequest, roundupAccountMapping.get(), bearerToken);
        }

        var account = retrieveAccountService.retrieveAccountDetails(accountUid, bearerToken);
        var savingsGoal = createSavingsGoalService.createSavingsGoal(saveRequest, account, bearerToken);

        roundupAccountService.saveRoundupAccount(saveRequest, accountUid, account.getDefaultCategory(), account.getCurrency(), savingsGoal.getSavingsGoalUid());

        return SaveRoundupResponse.builder()
                .roundupSavingsGoalUid(savingsGoal.getSavingsGoalUid())
                .roundupGoal(saveRequest.getGoal())
                .roundupMaximum(saveRequest.getRoundupMaximum())
                .roundupFactor(saveRequest.getRoundupFactor())
                .build();
    }
}
