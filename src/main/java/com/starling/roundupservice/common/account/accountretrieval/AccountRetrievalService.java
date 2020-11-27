package com.starling.roundupservice.common.account.accountretrieval;

import com.starling.roundupservice.common.StarlingAPIProvider;
import com.starling.roundupservice.common.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRetrievalService
{
    private final StarlingAPIProvider starlingAPIProvider;

    public Account retrieveAccountDetails(final String accountUid, final String bearerToken)
    {
        var uri = UriBuilder.buildAccountRetrievalUri();

        var accounts = starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.GET,
                null,
                AccountRetrievalResponse.class);

        return accounts.getAccounts().stream()
                .filter(account -> account.getAccountUid().equals(accountUid))
                .findAny()
                .orElseThrow();
    }
}
