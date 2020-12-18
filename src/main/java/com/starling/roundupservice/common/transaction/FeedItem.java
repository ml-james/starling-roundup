package com.starling.roundupservice.common.transaction;

import com.starling.roundupservice.common.Money;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class FeedItem
{
    private final String feedItemUid;
    private final String categoryUid;
    private final Money amount;
    private final Money sourceAmount;
    private final String direction;
    private final String updatedAt;
    private final String transactionTime;
    private final String settlementTime;
    private final String source;
    private final String status;
    private final String counterPartyType;
    private final String counterPartyUid;
    private final String counterPartyName;
    private final String counterPartySubEntityUid;
    private final String counterPartySubEntityName;
    private final String counterPartySubEntityIdentifier;
    private final String counterPartySubEntitySubIdentifier;
    private final String reference;
    private final String country;
    private final String spendingCategory;
}
