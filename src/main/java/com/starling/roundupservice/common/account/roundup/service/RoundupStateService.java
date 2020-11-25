package com.starling.roundupservice.common.account.roundup.service;

import com.starling.roundupservice.action.State;
import com.starling.roundupservice.common.account.roundup.repository.RoundupStateRepository;
import com.starling.roundupservice.common.account.roundup.domain.RoundupStateMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundupStateService
{
    private final RoundupStateRepository roundupStateRepository;

    public boolean isRoundupDue(final int roundupUid, final String weekEnd)
    {
        var roundupStateMapping = roundupStateRepository.findByRoundupUidAndWeekEnd(roundupUid, weekEnd);

        return roundupStateMapping.isEmpty();
    }

    public void insertState(final int roundupUid, final State state, final String transactionId, final String weekEnd)
    {
        var roundupStateMapping = RoundupStateMapping.builder()
                .roundupUid(roundupUid)
                .state(state.toString())
                .transferUid(transactionId)
                .weekEnd(weekEnd)
                .build();
        roundupStateRepository.save(roundupStateMapping);
    }
}
