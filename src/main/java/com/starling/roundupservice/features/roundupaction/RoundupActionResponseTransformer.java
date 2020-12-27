package com.starling.roundupservice.features.roundupaction;

import com.starling.roundupservice.action.RoundupActionResponse;
import com.starling.roundupservice.action.State;

public class RoundupActionResponseTransformer
{

    public static RoundupActionResponse transform(final State state)
    {
        return RoundupActionResponse.builder()
                .roundupState(state)
                .build();
    }

    public static RoundupActionResponse transform(final State state, final String transferUid, final int amount)
    {
        return RoundupActionResponse.builder()
                .roundupState(state)
                .transferUid(transferUid)
                .amount(amount)
                .build();
    }
}
