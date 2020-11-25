package com.starling.roundupservice.common.account.fundconfirmation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FundConfirmationService
{
    private final FundConfirmationProvider fundConfirmationProvider;

    public boolean sufficientFunds(final String accountUid, final int amount, final String bearerToken)
    {
        var fundConfirmationResponse = fundConfirmationProvider.retrieveFundConfirmation(accountUid, amount, bearerToken);

        return fundConfirmationResponse.isRequestedAmountAvailableToSpend() &&
                !fundConfirmationResponse.isAccountWouldBeInOverdraftIfRequestedAmountSpent();
    }
}
