package com.starling.roundupservice.common.savingsgoal.create.domain;

import com.starling.roundupservice.common.savingsgoal.deposit.domain.Money;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class SavingsGoalCreationRequest
{
    String name;
    String currency;
    Money target;
    String base64EncodedPhoto;
}
