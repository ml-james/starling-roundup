package com.starling.roundupservice.common.savingsgoal.deposit;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DepositSavingsGoalResponse
{
    public String transferUid;
    public boolean success;
    List<Map<String, String>> errors;
}