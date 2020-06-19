package com.starling.roundupservice.features.roundupcreation;

import com.starling.roundupservice.common.accounts.retrieval.AccountRetrievalService;
import com.starling.roundupservice.common.accounts.roundup.RoundupAccountService;
import com.starling.roundupservice.common.savingsgoal.create.SavingsGoalCreationService;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreationService {

  private final AccountRetrievalService accountRetrievalService;
  private final SavingsGoalCreationService savingsGoalCreationService;
  private final RoundupAccountService roundupAccountService;

  public String createRoundupGoal(final RoundupCreationRequest creationRequest) {

    if (roundupAccountService.retrieveRoundupAccount(creationRequest.getAccountUid()).isPresent()) {
      throw new DuplicateCreationRequestException();
    }

    var accountInformation = accountRetrievalService.getAccountInformation(creationRequest.getAccountUid());
    var savingsGoal = savingsGoalCreationService.createSavingsGoal(accountInformation.getCurrency());
    roundupAccountService.saveRoundupAccount(creationRequest, accountInformation, savingsGoal);

    return savingsGoal.getSavingsGoalUid();

  }
}
