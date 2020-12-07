package com.starling.roundupservice.common.account.roundup.service;

import com.starling.roundupservice.perform.State;
import com.starling.roundupservice.common.account.roundup.repository.RoundupStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundupStateService
{
    private final RoundupStateRepository roundupStateRepository;

    public boolean isRoundupDue(final String roundupUid, final String weekEnd)
    {
        var roundupStateMapping = roundupStateRepository.findByRoundupUidAndWeekEnd(roundupUid, weekEnd);

        return roundupStateMapping.isEmpty();
    }

    public void insertState(final String roundupUid, final State state, final String transactionId, final String weekEnd)
    {
        roundupStateRepository.save(roundupUid, state.toString(), transactionId, weekEnd);
    }
}
