package com.starling.roundup.common.accounts.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class FundsConfirmationResponse {

  private final boolean requestedAmountAvailableToSpend;
  private final boolean accountWouldBeInOverdraftIfRequestedAmountSpent;

}
