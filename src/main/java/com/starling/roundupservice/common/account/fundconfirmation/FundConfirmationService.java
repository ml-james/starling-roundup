package com.starling.roundupservice.common.account.fundconfirmation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FundConfirmationService {

  private final FundConfirmationProvider fundConfirmationProvider;

  public boolean sufficientFunds(final String accountUid, final int amount) {

    var fundConfirmationResponse = fundConfirmationProvider.retrieveFundConfirmation(accountUid, amount);

    return fundConfirmationResponse.isRequestedAmountAvailableToSpend() &&
        !fundConfirmationResponse.isAccountWouldBeInOverdraftIfRequestedAmountSpent();

  }
}
