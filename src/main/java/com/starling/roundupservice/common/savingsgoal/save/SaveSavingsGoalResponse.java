package com.starling.roundupservice.common.savingsgoal.save;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
public class SaveSavingsGoalResponse
{
    String savingsGoalUid;
    public boolean success;
    List<Map<String, String>> errors;
}
