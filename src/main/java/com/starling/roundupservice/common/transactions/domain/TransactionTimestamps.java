package com.starling.roundupservice.common.transactions.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class TransactionTimestamps {

  private String minTransactionTimestamp;
  private String maxTransactionTimestamp;

}
