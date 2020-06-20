package com.starling.roundupservice.features.roundupcreation;

import com.starling.roundupservice.common.account.retrieval.AccountRetrievalService;
import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
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
      throw new ClientException();
    }

    var account = accountRetrievalService.getAccountMetadata(creationRequest.getAccountUid());
    var savingsGoal = savingsGoalCreationService.createSavingsGoal(creationRequest, account.getCurrency());
    roundupAccountService.saveRoundupAccount(creationRequest, savingsGoal, account.getDefaultCategoryUid());

    return savingsGoal.getSavingsGoalUid();
  }
}
