package com.starling.roundupservice.common;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.stereotype.Component;

@Component
public class ClockService {

    public DateTime getCurrentDateTime()
    {
        return new DateTime(DateTimeZone.UTC);
    }
}
