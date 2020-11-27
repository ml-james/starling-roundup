package com.starling.roundupservice.common.savingsgoal.create.domain;

import java.util.List;
import java.util.Map;

import com.starling.roundupservice.common.ClientErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@Getter
public class SavingsGoalCreationResponse
{
    String savingsGoalUid;
    public boolean success;
    List<Map<String, String>> errors;
}
