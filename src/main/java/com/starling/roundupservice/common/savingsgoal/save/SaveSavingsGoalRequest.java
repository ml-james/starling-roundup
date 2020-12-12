package com.starling.roundupservice.common.savingsgoal.save;

import com.starling.roundupservice.common.Money;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class SaveSavingsGoalRequest
{
    String name;
    String currency;
    Money target;
    String base64EncodedPhoto;
}
