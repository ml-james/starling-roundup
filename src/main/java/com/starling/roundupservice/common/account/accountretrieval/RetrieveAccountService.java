package com.starling.roundupservice.common.account.accountretrieval;

import com.starling.roundupservice.common.StarlingApiProvider;
import com.starling.roundupservice.common.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetrieveAccountService
{
    private final StarlingApiProvider starlingAPIProvider;

    public Account retrieveAccountDetails(final String accountUid, final String bearerToken)
    {
        var uri = UriBuilder.buildAccountRetrievalUri();

        var accounts = starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.GET,
                null,
                RetrieveAccountResponse.class);

        return accounts.getAccounts().stream()
                .filter(account -> account.getAccountUid().equals(accountUid))
                .findAny()
                .orElseThrow();
    }
}
