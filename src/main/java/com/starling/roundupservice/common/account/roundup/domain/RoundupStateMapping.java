package com.starling.roundupservice.common.account.roundup.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Getter
@Table("dbo.roundup_state")
public class RoundupStateMapping
{
    @Id
    String roundupUid;
    String transferUid;
    String state;
    String weekEnd;
}
