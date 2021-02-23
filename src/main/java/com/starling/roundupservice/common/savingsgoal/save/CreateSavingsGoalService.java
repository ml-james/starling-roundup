package com.starling.roundupservice.common.savingsgoal.save;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.starlingapi.StarlingApiRequestBuilder;
import com.starling.roundupservice.common.starlingapi.StarlingApiProvider;
import com.starling.roundupservice.common.starlingapi.StarlingApiUriBuilder;
import com.starling.roundupservice.common.account.accountretrieval.Account;
import com.starling.roundupservice.save.SaveRoundupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateSavingsGoalService
{
    private final StarlingApiProvider starlingAPIProvider;

    public SaveSavingsGoalResponse createSavingsGoal(final SaveRoundupRequest creationRequest, final Account account, final String bearerToken)
    {
        final var savingsGoalCreationRequest = StarlingApiRequestBuilder.saveRequest(creationRequest, account.getCurrency());
        final var uri = StarlingApiUriBuilder.buildSavingsGoalCreationUri(account.getAccountUid());

        final var savingsGoal = starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.PUT,
                savingsGoalCreationRequest,
                SaveSavingsGoalResponse.class);

        if (!savingsGoal.success)
        {
            throw new ClientException("Savings goal creation error: ", String.format(
                    "creating round up savings goal for account %s was unsuccessful, failed with the following errors: %s", account.getAccountUid(), savingsGoal.getErrors()));
        }

        return savingsGoal;

    }
}
