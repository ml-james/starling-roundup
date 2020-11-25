package com.starling.roundupservice.common.savingsgoal.create;

import com.starling.roundupservice.common.RequestBuilder;
import com.starling.roundupservice.common.StarlingAPIProvider;
import com.starling.roundupservice.common.UriBuilder;
import com.starling.roundupservice.common.savingsgoal.create.domain.SavingsGoalCreationResponse;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SavingsGoalCreationService
{
    private final StarlingAPIProvider starlingAPIProvider;

    public SavingsGoalCreationResponse createSavingsGoal(final RoundupCreationRequest creationRequest, final String currency, final String accountUid, final String bearerToken)
    {
        final var savingsGoalCreationRequest = RequestBuilder.createRequest(creationRequest, currency);
        final var uri = UriBuilder.createSavingsGoalCreationUri(accountUid);

        return starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.PUT,
                savingsGoalCreationRequest,
                SavingsGoalCreationResponse.class);
    }
}
