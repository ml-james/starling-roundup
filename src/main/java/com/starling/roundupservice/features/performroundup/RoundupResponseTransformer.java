package com.starling.roundupservice.features.performroundup;

import com.starling.roundupservice.perform.PerformRoundupResponse;
import com.starling.roundupservice.perform.State;

public class RoundupResponseTransformer
{

    public static PerformRoundupResponse transform(final State state)
    {
        return PerformRoundupResponse.builder()
                .roundUpState(state)
                .build();
    }

    public static PerformRoundupResponse transform(final State state, final String transferUid, final int amount)
    {
        return PerformRoundupResponse.builder()
                .roundUpState(state)
                .transferUid(transferUid)
                .amount(amount)
                .build();
    }
}
