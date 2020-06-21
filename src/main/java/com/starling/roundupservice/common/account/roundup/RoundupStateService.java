package com.starling.roundupservice.common.account.roundup;

import com.starling.roundupservice.action.State;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundupStateService {

  private final RoundupStateRepository roundupStateRepository;

  public boolean isRoundupDue(final int roundupUid, final String weekEnd) {
    var roundupStateMapping = roundupStateRepository.findByRoundupUidAndWeekEnd(roundupUid, weekEnd);

    return roundupStateMapping.isEmpty();
  }

  public void insertState(final int roundupUid, final State state, final String transactionId, final String weekEnd) {
    var roundupStateMapping = RoundupStateMapping.builder()
        .roundupId(roundupUid)
        .state(state.toString())
        .transferUid(transactionId)
        .weekEnd(weekEnd)
        .build();
    roundupStateRepository.save(roundupStateMapping);
  }
}
