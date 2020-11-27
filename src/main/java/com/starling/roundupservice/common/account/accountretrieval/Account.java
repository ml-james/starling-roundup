package com.starling.roundupservice.common.account.accountretrieval;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class Account
{
    final String accountUid;
    final String accountType;
    final String defaultCategory;
    final String currency;
    final String createdAt;
    final String name;
}
