package com.starling.roundupservice.common.savingsgoal.create;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SavingsGoalCreationResponse {

  String savingsGoalUid;
  boolean success;
  List<Map<String, String>> errors;

}
