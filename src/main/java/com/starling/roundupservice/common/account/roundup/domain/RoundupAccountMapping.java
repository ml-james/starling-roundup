package com.starling.roundupservice.common.account.roundup.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("dbo.roundup_account")
public class RoundupAccountMapping
{
    @Id
    String accountUid;
    String roundupUid;
    String categoryUid;
    String accountUidCurrency;
    String savingsGoalUid;
    int maximumRoundup;
    int roundupFactor;
}
