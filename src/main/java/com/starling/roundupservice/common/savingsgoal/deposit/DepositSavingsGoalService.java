package com.starling.roundupservice.common.savingsgoal.deposit;

import com.starling.roundupservice.common.RequestBuilder;
import com.starling.roundupservice.common.StarlingAPIProvider;
import com.starling.roundupservice.common.UriBuilder;
import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import com.starling.roundupservice.common.exception.ServerException;
import com.starling.roundupservice.common.savingsgoal.deposit.domain.DepositSavingsGoalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DepositSavingsGoalService
{
    private final StarlingAPIProvider starlingAPIProvider;

    public String deposit(final RoundupAccountMapping roundupAccount, final int roundupAmount, final String bearerToken)
    {
        var uri = UriBuilder.buildSavingsDepositUri(roundupAccount.getAccountUid(), roundupAccount.getSavingsGoalUid());
        var savingsGoalDepositRequest = RequestBuilder.saveRequest(roundupAccount.getAccountUidCurrency(), roundupAmount);

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
