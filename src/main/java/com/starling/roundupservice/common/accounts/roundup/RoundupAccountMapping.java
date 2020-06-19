package com.starling.roundupservice.common.accounts.roundup;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("dbo.roundup_account")
public class RoundupAccountMapping {

  @Id
  String accountUid;
  String defaultCategoryUid;
  String savingsGoalUid;
  int maximumRoundup;
  int roundupFactor;
  String createdTime;

}
