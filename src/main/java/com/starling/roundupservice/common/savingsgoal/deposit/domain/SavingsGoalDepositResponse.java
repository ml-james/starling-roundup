package com.starling.roundupservice.common.savingsgoal.deposit.domain;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SavingsGoalDepositResponse
{
    public String transferUid;
    public boolean success;
    List<Map<String, String>> errors;
}
