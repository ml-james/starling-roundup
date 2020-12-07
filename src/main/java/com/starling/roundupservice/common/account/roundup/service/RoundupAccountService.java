package com.starling.roundupservice.common.account.roundup.service;

import com.starling.roundupservice.common.account.roundup.repository.RoundupAccountRepository;
import com.starling.roundupservice.common.account.roundup.domain.RoundupAccountMapping;
import com.starling.roundupservice.save.SaveRoundupRequest;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
        roundupAccountRepository.save(roundupUid.toString(), accountUid, categoryUid, accountUidCurrency, savingsGoalUid, creationRequest.getRoundupMaximum(), creationRequest.getRoundupFactor());
    }
}
