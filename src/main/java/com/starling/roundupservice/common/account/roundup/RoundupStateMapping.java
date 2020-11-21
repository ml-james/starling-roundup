package com.starling.roundupservice.common.account.roundup;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Data
@Table("dbo.roundup_state")
public class RoundupStateMapping
{

    @Id
    int roundupUid;
    String transferUid;
    String state;
    String weekEnd;

}
