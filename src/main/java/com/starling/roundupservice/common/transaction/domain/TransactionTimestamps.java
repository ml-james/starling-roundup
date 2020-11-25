package com.starling.roundupservice.common.transaction.domain;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class TransactionTimestamps
{
    public String minTransactionTimestamp;
    public String maxTransactionTimestamp;
}
