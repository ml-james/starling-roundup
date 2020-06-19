package com.starling.roundupservice.common.accounts.roundup;

import com.starling.roundupservice.common.accounts.retrieval.Account;
import com.starling.roundupservice.common.savingsgoal.create.SavingsGoalCreationResponse;
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

  public void saveRoundupAccount(final RoundupCreationRequest creationRequest, final Account accountInformation, final SavingsGoalCreationResponse savingsGoalCreationResponse) {

    var roundupAccountMapping = RoundupAccountMapping.builder()
        .accountUid(creationRequest.getAccountUid())
        .defaultCategoryUid(accountInformation.getDefaultCategoryUid())
        .savingsGoalUid(savingsGoalCreationResponse.getSavingsGoalUid())
        .maximumRoundup(creationRequest.getRoundupMaximum())
        .roundupFactor(creationRequest.getRoundupFactor())
        .build();

    roundupAccountRepository.save(roundupAccountMapping);

  }
}
