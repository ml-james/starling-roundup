package com.starling.roundupservice.features.roundupcreation;

import com.starling.roundupservice.common.account.accountretrieval.AccountRetrievalService;
import com.starling.roundupservice.common.account.roundup.service.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.exception.ServerException;
import com.starling.roundupservice.common.savingsgoal.create.SavingsGoalCreationService;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreationService
{
    private final RoundupAccountService roundupAccountService;
    private final SavingsGoalCreationService savingsGoalCreationService;
    private final AccountRetrievalService accountRetrievalService;

    public String createRoundupGoal(final RoundupCreationRequest creationRequest,
                                    final String accountUid,
                                    final String bearerToken)
    {
        if (roundupAccountService.retrieveRoundupAccount(accountUid).isPresent())
        {
            throw new ClientException("Create roundup account error: ", "roundup account already exists");
        }

        var account = accountRetrievalService.retrieveAccountDetails(accountUid, bearerToken);
        var savingsGoal = savingsGoalCreationService.createSavingsGoal(creationRequest, account, bearerToken);

        if (!savingsGoal.success)
        {
            throw new ServerException("Savings goal creation error: ", String.format(
                    "creating round up savings goal for account %s was unsuccessful, failed with the following errors: %s", accountUid, savingsGoal.getErrors()));
        }

        roundupAccountService.saveRoundupAccount(creationRequest, accountUid, account.getDefaultCategory(), account.getCurrency(), savingsGoal.getSavingsGoalUid());

        return savingsGoal.getSavingsGoalUid();
    }
}
