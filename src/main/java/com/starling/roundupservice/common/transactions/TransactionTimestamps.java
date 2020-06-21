package com.starling.roundupservice.common.transactions;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@Getter
public class TransactionTimestamps {

  String minTransactionTimestamp;
  String maxTransactionTimestamp;

}
