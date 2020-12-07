package com.starling.roundupservice.features.saveroundup;

import com.starling.roundupservice.common.account.accountretrieval.RetrieveAccountService;
import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.service.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.save.CreateSavingsGoalService;
import com.starling.roundupservice.common.savingsgoal.save.UpdateSavingsGoalService;
import com.starling.roundupservice.common.savingsgoal.save.domain.SavingsGoalSaveResponse;
import com.starling.roundupservice.save.SaveRoundupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SaveRoundupService
{
    private final RoundupAccountService roundupAccountService;
    private final CreateSavingsGoalService createSavingsGoalService;
    private final UpdateSavingsGoalService updateSavingsGoalService;
    private final RetrieveAccountService retrieveAccountService;

    public String createRoundupGoal(final SaveRoundupRequest saveRequest,
                                    final String accountUid,
                                    final String bearerToken)
    {
        var roundupAccountMapping = roundupAccountService.retrieveRoundupAccount(accountUid);
        if (roundupAccountMapping.isPresent())
        {
            var savingsGoalSaveResponse = updateSavingsGoalService.updateSavingsGoal(saveRequest, roundupAccountMapping.get(), bearerToken);
            return savingsGoalSaveResponse.getSavingsGoalUid();
        }

        var account = retrieveAccountService.retrieveAccountDetails(accountUid, bearerToken);
        var savingsGoal = createSavingsGoalService.createSavingsGoal(saveRequest, account, bearerToken);

        if (!savingsGoal.success)
        {
            throw new ClientException("Savings goal creation error: ", String.format(
                    "creating round up savings goal for account %s was unsuccessful, failed with the following errors: %s", accountUid, savingsGoal.getErrors()));
        }

        roundupAccountService.saveRoundupAccount(saveRequest, accountUid, account.getDefaultCategory(), account.getCurrency(), savingsGoal.getSavingsGoalUid());

        return savingsGoal.getSavingsGoalUid();
    }
}
