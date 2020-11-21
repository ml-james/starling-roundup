package com.starling.roundupservice.common.savingsgoal.deposit;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Builder(toBuilder = true)
@RequiredArgsConstructor
@Getter
public class Money
{

    private final String currency;
    private final int minorUnits;

}
