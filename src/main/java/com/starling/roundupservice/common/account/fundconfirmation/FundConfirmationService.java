package com.starling.roundupservice.common.account.fundconfirmation;

import com.starling.roundupservice.common.starlingapi.StarlingApiProvider;
import com.starling.roundupservice.common.starlingapi.StarlingApiUriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FundConfirmationService
{
    private final StarlingApiProvider starlingAPIProvider;

    public boolean sufficientFunds(final String accountUid, final int amount, final String bearerToken)
    {
        var uri = StarlingApiUriBuilder.buildFundConfirmationUri(accountUid, amount);

        var fundConfirmationResponse = starlingAPIProvider.queryStarlingAPI(uri,
                bearerToken,
                HttpMethod.GET,
                null,
                FundConfirmationResponse.class);

        return fundConfirmationResponse.isRequestedAmountAvailableToSpend() &&
                !fundConfirmationResponse.isAccountWouldBeInOverdraftIfRequestedAmountSpent();
    }
}
