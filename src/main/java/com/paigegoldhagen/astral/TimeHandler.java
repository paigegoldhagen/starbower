package com.paigegoldhagen.astral;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * For calculating time-based variables and scheduling Runnable tasks.
 */
public class TimeHandler {
    /**
     * Get the current time for the system.
     *
     * @return the current LocalTime in HH:mm
     */
    public static LocalTime getCurrentTime() {
        return LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Get the system's timezone and get the offset amount used to calculate local start times.
     *
     * @return the zone offset amount from the system's timezone
     */
    public static ZoneOffset getTimeZoneOffset() {
        ZoneId systemTimeZone = ZoneId.systemDefault();
        return ZonedDateTime.now(systemTimeZone).getOffset();
    }

    /**
     * Schedule the Runnable prepareNotification method from the Notifications class
     * to run every second.
     */
    public static void scheduleTaskEverySecond() {
        Runnable prepareNotification = Notifications.prepareNotification();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(prepareNotification, 0, 1, TimeUnit.SECONDS);
    }
}

