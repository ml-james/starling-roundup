package com.starling.roundupservice.common.account.roundup;

import com.starling.roundupservice.action.State;
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

    public void insertState(final String roundupUid, final String transactionId, final State state, final String weekEnd)
    {
        roundupStateRepository.save(roundupUid, transactionId, state.toString(), weekEnd);
    }
}
