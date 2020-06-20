package com.starling.roundupservice.common.savingsgoal.deposit;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class SavingsGoalDepositResponse {

  String transferUid;
  boolean success;
  List<Map<String, String>> errors;

}
