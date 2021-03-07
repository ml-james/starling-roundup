package com.starling.roundupservice.common.savingsgoal.deposit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class DepositSavingsGoalResponse
{
    public String transferUid;
    public boolean success;
    List<Map<String, String>> errors;
}
