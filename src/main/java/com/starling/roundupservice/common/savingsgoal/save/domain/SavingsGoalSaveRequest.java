package com.starling.roundupservice.common.savingsgoal.save.domain;

import com.starling.roundupservice.common.Money;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class SavingsGoalSaveRequest
{
    String name;
    String currency;
    Money target;
    String base64EncodedPhoto;
}
