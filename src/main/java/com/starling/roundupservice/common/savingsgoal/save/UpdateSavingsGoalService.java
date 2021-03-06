package com.starling.roundupservice.common.savingsgoal.save;

import com.starling.roundupservice.common.account.roundup.RoundupAccountMapping;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.starlingapi.StarlingApiProvider;
import com.starling.roundupservice.common.starlingapi.StarlingApiRequestBuilder;
import com.starling.roundupservice.common.starlingapi.StarlingApiUriBuilder;
import com.starling.roundupservice.save.SaveRoundupRequest;
import com.starling.roundupservice.save.SaveRoundupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateSavingsGoalService
{
    private final StarlingApiProvider starlingAPIProvider;
    private final RoundupAccountService roundupAccountService;

    public SaveRoundupResponse updateSavingsGoal(final SaveRoundupRequest saveRequest, final RoundupAccountMapping account, final String bearerToken)
    {
        final var savingsGoalUpdateRequest = StarlingApiRequestBuilder.saveRequest(saveRequest, account.getAccountUidCurrency());
        final var uri = StarlingApiUriBuilder.buildSavingsGoalUpdateUri(account.getAccountUid(), account.getSavingsGoalUid());

        starlingAPIProvider.queryStarlingAPI(
                uri,
                bearerToken,
                HttpMethod.PUT,
                savingsGoalUpdateRequest,
                SaveSavingsGoalResponse.class);

        roundupAccountService.updateRoundupAccount(account.getAccountUid(),
                saveRequest.getGoal(),
                saveRequest.getRoundupMaximum(),
                saveRequest.getRoundupFactor());

        return SaveRoundupResponse.builder()
                .roundupSavingsGoalUid(account.getSavingsGoalUid())
                .roundupGoal(saveRequest.getGoal())
                .roundupFactor(saveRequest.getRoundupFactor())
                .roundupMaximum(saveRequest.getRoundupMaximum())
                .build();
    }
}
