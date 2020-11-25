package com.starling.roundupservice.common.account.accountretrieval;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class AccountRetrievalResponse
{
    private final List<Account> accounts;
}
