package com.starling.roundupservice.common.savingsgoal.save;

import com.starling.roundupservice.common.RequestBuilder;
import com.starling.roundupservice.common.StarlingAPIProvider;
import com.starling.roundupservice.common.UriBuilder;
import com.starling.roundupservice.common.account.accountretrieval.Account;
import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import com.starling.roundupservice.common.savingsgoal.save.domain.SavingsGoalSaveResponse;
import com.starling.roundupservice.save.SaveRoundupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateSavingsGoalService
{
    private final StarlingAPIProvider starlingAPIProvider;

    public SavingsGoalSaveResponse updateSavingsGoal(final SaveRoundupRequest creationRequest, final RoundupAccountMapping account, final String bearerToken)
    {
        final var savingsGoalCreationRequest = RequestBuilder.saveRequest(creationRequest, account.getAccountUidCurrency());
        final var uri = UriBuilder.buildSavingsGoalUpdateUri(account.getAccountUid(), account.getSavingsGoalUid());

        return starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.PUT,
                savingsGoalCreationRequest,
                SavingsGoalSaveResponse.class);
    }
}
