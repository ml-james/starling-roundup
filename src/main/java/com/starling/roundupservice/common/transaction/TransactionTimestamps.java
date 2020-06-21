package com.starling.roundupservice.common.transaction;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class TransactionTimestamps {

  String minTransactionTimestamp;
  String maxTransactionTimestamp;

}
