package com.starling.roundupservice.common.transaction;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

@Component
public class ClockService {

    public DateTime getCurrentDateTime()
    {
        return new DateTime();
    }
}
