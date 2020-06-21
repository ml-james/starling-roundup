package com.starling.roundupservice.common.account.roundup;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("dbo.roundup_account")
public class RoundupAccountMapping {

  @Id
  int roundupUid;
  String accountUid;
  String categoryUid;
  String accountUidCurrency;
  String savingsGoalUid;
  int maximumRoundup;
  int roundupFactor;
  String createdTime;

}
