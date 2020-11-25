package com.starling.roundupservice.common.account.fundconfirmation;

import com.starling.roundupservice.common.StarlingAPIProvider;
import com.starling.roundupservice.common.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FundConfirmationService
{
    private final StarlingAPIProvider starlingAPIProvider;

    public boolean sufficientFunds(final String accountUid, final int amount, final String bearerToken)
    {
        var uri = UriBuilder.buildFundConfirmationUri(accountUid, amount);

        var fundConfirmationResponse = starlingAPIProvider.queryStarlingAPI(uri, bearerToken, HttpMethod.POST, new Object(), FundConfirmationResponse.class);

        return fundConfirmationResponse.isRequestedAmountAvailableToSpend() &&
                !fundConfirmationResponse.isAccountWouldBeInOverdraftIfRequestedAmountSpent();
    }
}
