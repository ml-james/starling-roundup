package com.starling.roundupservice.features.roundupcreation;

import com.starling.roundupservice.common.account.roundup.service.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
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

    public String createRoundupGoal(final RoundupCreationRequest creationRequest,
                                    final String accountUid,
                                    final String defaultCategoryUid,
                                    final String currency,
                                    final String bearerToken)
    {
        if (roundupAccountService.retrieveRoundupAccount(accountUid).isPresent())
        {
            throw new ClientException("Create roundup account error: ", "roundup account already exists");
        }

        var savingsGoal = savingsGoalCreationService.createSavingsGoal(creationRequest, currency, accountUid, bearerToken);
        roundupAccountService.saveRoundupAccount(creationRequest, accountUid, savingsGoal.getSavingsGoalUid(), defaultCategoryUid);

        return savingsGoal.getSavingsGoalUid();
    }
}
