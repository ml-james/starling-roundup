package com.starling.roundupservice.common.account.accountretrieval;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class Account
{
    private final String accountUid;
    private final String accountType;
    private final String defaultCategoryId;
    private final String currency;
    private final String createdAt;
    private final String name;
}
