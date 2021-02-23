package com.starling.roundupservice.common.account.accountretrieval;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.starlingapi.StarlingApiProvider;
import com.starling.roundupservice.common.starlingapi.StarlingApiUriBuilder;
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
        final var uri = StarlingApiUriBuilder.buildAccountRetrievalUri();

        final var accounts = starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.GET,
                null,
                RetrieveAccountResponse.class);

        final var accountDetails = accounts.getAccounts().stream()
                .filter(account -> account.getAccountUid().equals(accountUid))
                .findFirst();

        if (accountDetails.isEmpty())
        {
            throw new ClientException("Savings goal creation error: ", String.format(" there is no account with the requested accountUid: %s", accountUid));
        }

        return accountDetails.get();
    }
}
