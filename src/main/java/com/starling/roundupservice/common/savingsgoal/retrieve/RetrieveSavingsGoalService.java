package com.starling.roundupservice.common.savingsgoal.retrieve;

import com.starling.roundupservice.common.StarlingApiProvider;
import com.starling.roundupservice.common.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetrieveSavingsGoalService
{
    private final StarlingApiProvider starlingAPIProvider;

    public RetrieveSavingsGoalResponse retrieveSavingsGoal(final String accountUid, final String savingsGoalUid, final String bearerToken)
    {
        var uri = UriBuilder.buildSavingsGoalRetrievalUri(accountUid, savingsGoalUid);
        return starlingAPIProvider.queryStarlingAPI(uri, bearerToken, HttpMethod.GET, null, RetrieveSavingsGoalResponse.class);
    }

}
