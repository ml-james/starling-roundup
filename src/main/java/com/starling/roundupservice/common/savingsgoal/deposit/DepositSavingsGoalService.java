package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.StarlingApiRequestBuilder;
import com.starling.roundupservice.common.StarlingApiProvider;
import com.starling.roundupservice.common.UriBuilder;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositSavingsGoalService
{
    private final StarlingApiProvider starlingAPIProvider;

    public String deposit(final RoundupAccountMapping roundupAccount, final int roundupAmount, final String bearerToken)
    {
        var uri = UriBuilder.buildSavingsDepositUri(roundupAccount.getAccountUid(), roundupAccount.getSavingsGoalUid());
        var savingsGoalDepositRequest = StarlingApiRequestBuilder.saveRequest(roundupAccount.getAccountUidCurrency(), roundupAmount);

        var depositResponse = starlingAPIProvider.queryStarlingAPI(uri,
                bearerToken,
                HttpMethod.POST,
                savingsGoalDepositRequest,
                DepositSavingsGoalResponse.class);

        if (!depositResponse.success)
        {
            throw new ServerException("Deposit to savings account: ", String.format(
                    "depositing to roundup goal %s for account %s was unsuccessful, failed with the following errors: %s", roundupAccount.getSavingsGoalUid(),
                    roundupAccount.getAccountUid(), depositResponse.getErrors()));
        }

        return depositResponse.transferUid;
    }
}
