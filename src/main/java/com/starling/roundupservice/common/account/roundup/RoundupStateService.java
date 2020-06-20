package com.starling.roundupservice.common.account.roundup;

import com.starling.roundupservice.action.State;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundupStateService {

  private static final int WEEK_IN_DAYS = 7;

  private final RoundupStateRepository roundupStateRepository;

  public boolean isRoundupDue(final int roundupUid) {
    var roundupStateMapping = roundupStateRepository.findById(roundupUid);
    if (roundupStateMapping.isEmpty()) {
      return true;
    }
    var lastSuccessfulRun = roundupStateMapping.get().getCompletedTime();
    return isMoreThanOneWeek(lastSuccessfulRun);
  }

  private boolean isMoreThanOneWeek(final String lastSuccessfulRun) {
    var lastRunDateTime = LocalDateTime.parse(lastSuccessfulRun);
    var daysBetween = Days.daysBetween(new LocalDateTime(), lastRunDateTime);
    return daysBetween.size() >= WEEK_IN_DAYS;
  }

  public void insertState(final int roundupUid, final State state, final String transactionId) {
    var roundupStateMapping = RoundupStateMapping.builder()
        .roundupId(roundupUid)
        .state(state.toString())
        .transferUid(transactionId)
        .completedTime(new DateTime().toString())
        .build();
    roundupStateRepository.save(roundupStateMapping);
  }
}
