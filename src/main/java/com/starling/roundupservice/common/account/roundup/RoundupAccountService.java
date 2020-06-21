package com.starling.roundupservice.common.account.roundup;

import com.starling.roundupservice.creation.RoundupCreationRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundupAccountService {

  private final RoundupAccountRepository roundupAccountRepository;

  public Optional<RoundupAccountMapping> retrieveRoundupAccount(final String accountUid) {

    return roundupAccountRepository.findById(accountUid);
  }

  public void saveRoundupAccount(final RoundupCreationRequest creationRequest, final String accountUid, final String savingsGoalUid,
      final String categoryUid) {

    var roundupAccountMapping = RoundupAccountMapping.builder()
        .accountUid(accountUid)
        .categoryUid(categoryUid)
        .savingsGoalUid(savingsGoalUid)
        .maximumRoundup(creationRequest.getRoundupMaximum())
        .roundupFactor(creationRequest.getRoundupFactor())
        .build();

    roundupAccountRepository.save(roundupAccountMapping);

  }
}
