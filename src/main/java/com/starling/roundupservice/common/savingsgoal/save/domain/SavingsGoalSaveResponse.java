package com.starling.roundupservice.common.savingsgoal.save.domain;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class SavingsGoalSaveResponse
{
    String savingsGoalUid;
    public boolean success;
    List<Map<String, String>> errors;
}
