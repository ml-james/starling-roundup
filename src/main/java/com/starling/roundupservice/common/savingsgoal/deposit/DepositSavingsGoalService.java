package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.starlingapi.StarlingApiRequestBuilder;
import com.starling.roundupservice.common.starlingapi.StarlingApiProvider;
import com.starling.roundupservice.common.starlingapi.StarlingApiUriBuilder;
import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.exception.ServerException;
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
        var uri = StarlingApiUriBuilder.buildSavingsDepositUri(roundupAccount.getAccountUid(), roundupAccount.getSavingsGoalUid());
        var savingsGoalDepositRequest = StarlingApiRequestBuilder.saveRequest(roundupAccount.getAccountUidCurrency(), roundupAmount);

        var depositResponse = starlingAPIProvider.queryStarlingAPI(uri,
                bearerToken,
                HttpMethod.PUT,
                savingsGoalDepositRequest,
                DepositSavingsGoalResponse.class);

        return depositResponse.transferUid;
    }
}
