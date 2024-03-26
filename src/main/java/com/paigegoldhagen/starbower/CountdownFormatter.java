package com.paigegoldhagen.starbower;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Building a countdown string for a countdown label.
 */
public class CountdownFormatter {
    /**
     * Get the amount of days until a Festival date, calculate the amount of hours until a Festival date,
     * and build a countdown string using the Festival ongoing boolean and the day/hour amounts.
     *
     * @param festivalDate      the date a Festival will start or end
     * @param utcDate           the current date in UTC
     * @param festivalOngoing   the Festival ongoing boolean
     *
     * @return                  a formatted countdown string
     */
    public static String buildCountdownString(LocalDateTime festivalDate, LocalDateTime utcDate, Boolean festivalOngoing) {
        long daysUntilFestival = ChronoUnit.DAYS.between(utcDate, festivalDate);
        long hoursUntilFestival = calculateHoursUntilFestival(utcDate, festivalDate, daysUntilFestival);
        return getCountdownString(festivalOngoing, daysUntilFestival, hoursUntilFestival);
    }

    /**
     * Build a countdown string based on the Festival ongoing boolean and the days/hours until the Festival date.
     *
     * @param festivalOngoing       the Festival ongoing boolean
     * @param daysUntilFestival     the amount of days until a Festival date
     * @param hoursUntilFestival    the remaining amount of hours until a Festival date
     *
     * @return                      a formatted countdown string
     */
    private static String getCountdownString(Boolean festivalOngoing, long daysUntilFestival, long hoursUntilFestival) {
        StringBuilder countdownString = new StringBuilder();

        if (festivalOngoing) {
            countdownString.append("Ending in ");
        }
        else {
            countdownString.append("Starting in ");
        }

        if (hoursUntilFestival == 0) {
            countdownString.append("less than an hour");
        }
        else {
            if (daysUntilFestival != 0) {
                countdownString.append(daysUntilFestival).append(" day");

                if (daysUntilFestival > 1) {
                    countdownString.append("s");
                }
                countdownString.append(" and ");
            }
            countdownString.append(hoursUntilFestival).append(" hour");

            if (hoursUntilFestival > 1) {
                countdownString.append("s");
            }
        }
        return String.valueOf(countdownString);
    }

    /**
     * Get the total hours until a Festival date, calculate the days as hours,
     * and get the remaining hours until a Festival date.
     *
     * @param utcDate           the current date in UTC
     * @param festivalDate      the date a Festival will start or end
     * @param daysUntilFestival the amount of days until a Festival date
     *
     * @return                  the remaining amount of hours until a Festival date
     */
    private static Long calculateHoursUntilFestival(LocalDateTime utcDate, LocalDateTime festivalDate, Long daysUntilFestival) {
        long totalHoursUntilFestival = ChronoUnit.HOURS.between(utcDate, festivalDate);
        long daysAsHours = daysUntilFestival * 24;
        return totalHoursUntilFestival - daysAsHours;
    }
}