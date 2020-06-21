package com.starling.roundupservice.features.roundupcreation;

import com.starling.roundupservice.common.account.roundup.RoundupAccountService;
import com.starling.roundupservice.common.exception.ClientException;
import com.starling.roundupservice.common.savingsgoal.create.SavingsGoalCreationService;
import com.starling.roundupservice.creation.RoundupCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreationService {

  private final SavingsGoalCreationService savingsGoalCreationService;
  private final RoundupAccountService roundupAccountService;

  public String createRoundupGoal(final RoundupCreationRequest creationRequest, final String accountUid, final String defaultCategoryUid,
      final String currency) {

    if (roundupAccountService.retrieveRoundupAccount(accountUid).isPresent()) {
      throw new ClientException("Create roundup account error: ", "roundup account already exists");
    }

    var savingsGoal = savingsGoalCreationService.createSavingsGoal(creationRequest, currency);
    roundupAccountService.saveRoundupAccount(creationRequest, accountUid, savingsGoal.getSavingsGoalUid(), defaultCategoryUid);

    return savingsGoal.getSavingsGoalUid();
  }
}
