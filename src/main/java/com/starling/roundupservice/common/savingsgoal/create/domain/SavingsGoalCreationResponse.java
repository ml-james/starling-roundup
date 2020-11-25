package com.starling.roundupservice.common.savingsgoal.create.domain;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
public class SavingsGoalCreationResponse
{
    String savingsGoalUid;
    boolean success;
    List<Map<String, String>> errors;
}
