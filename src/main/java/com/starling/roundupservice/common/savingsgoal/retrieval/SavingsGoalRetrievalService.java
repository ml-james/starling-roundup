package com.starling.roundupservice.common.savingsgoal.retrieval;

import com.starling.roundupservice.common.StarlingAPIProvider;
import com.starling.roundupservice.common.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingsGoalRetrievalService
{
    private final StarlingAPIProvider starlingAPIProvider;

    public SavingsGoalRetrievalResponse retrieveSavingsGoal(final String accountUid, final String savingsGoalUid, final String bearerToken)
    {
        var uri = UriBuilder.buildSavingsGoalRetrievalUri(accountUid, savingsGoalUid);
        return starlingAPIProvider.queryStarlingAPI(uri, bearerToken, HttpMethod.GET, null, SavingsGoalRetrievalResponse.class);
    }

}
