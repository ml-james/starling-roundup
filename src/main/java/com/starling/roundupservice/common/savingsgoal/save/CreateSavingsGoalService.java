package com.starling.roundupservice.common.savingsgoal.save;

import com.starling.roundupservice.common.StarlingApiRequestBuilder;
import com.starling.roundupservice.common.StarlingApiProvider;
import com.starling.roundupservice.common.UriBuilder;
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
        final var uri = UriBuilder.buildSavingsGoalCreationUri(account.getAccountUid());

        return starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.PUT,
                savingsGoalCreationRequest,
                SaveSavingsGoalResponse.class);
    }
}
