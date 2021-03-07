package com.starling.roundupservice.common.account.roundup;

import com.starling.roundupservice.save.SaveRoundupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RoundupAccountService
{
    private final RoundupAccountRepository roundupAccountRepository;

    public Optional<RoundupAccountMapping> retrieveRoundupAccount(final String accountUid)
    {
        return roundupAccountRepository.findById(accountUid);
    }

    public void saveRoundupAccount(final SaveRoundupRequest creationRequest,
                                   final String accountUid,
                                   final String categoryUid,
                                   final String accountUidCurrency,
                                   final String savingsGoalUid)
    {
        var roundupUid = UUID.randomUUID();
        roundupAccountRepository.save(roundupUid.toString(), accountUid, categoryUid, accountUidCurrency, savingsGoalUid, creationRequest.getGoal(), creationRequest.getRoundupMaximum(), creationRequest.getRoundupFactor());
    }

    public void updateRoundupAccount(final String accountUid, final int roundupGoal, final int roundupMaximum, final int roundupFactor)
    {
        roundupAccountRepository.updateRoundupAccount(accountUid, roundupGoal, roundupMaximum, roundupFactor);
    }
}
