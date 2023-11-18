package com.paigegoldhagen.astral;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * For calculating the server time (UTC).
 */
public class TimeHandler {
    /**
     * Get the zone ID of UTC and get the current time in UTC.
     *
     * @return    the current LocalTime in UTC
     */
    public static LocalTime getUtcTime() {
        ZoneId zoneId = ZoneId.of("UTC");
        return LocalTime.now(zoneId).truncatedTo(ChronoUnit.MINUTES);
    }
}
