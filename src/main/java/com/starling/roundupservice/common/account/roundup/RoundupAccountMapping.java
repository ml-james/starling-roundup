package com.starling.roundupservice.common.account.roundup;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Getter
@Table("dbo.roundup_account")
public class RoundupAccountMapping
{
    @Id
    String accountUid;
    String roundupUid;
    String categoryUid;
    String accountUidCurrency;
    String savingsGoalUid;
    int roundupGoal;
    int maximumRoundup;
    int roundupFactor;
}
