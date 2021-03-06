package com.starling.roundupservice.common.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder(toBuilder = true)
@AllArgsConstructor
@Getter
public class Roundup
{
    public int roundupAmount;
    public String weekEnd;
}
