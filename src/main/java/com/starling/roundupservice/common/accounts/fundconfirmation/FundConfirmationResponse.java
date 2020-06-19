package com.starling.roundupservice.common.accounts.fundconfirmation;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class FundConfirmationResponse {

  private final boolean requestedAmountAvailableToSpend;
  private final boolean accountWouldBeInOverdraftIfRequestedAmountSpent;

}
