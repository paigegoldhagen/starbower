package com.paigegoldhagen.astral;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * For creating a dynamic set of GUI components for a festival.
 */
public class FestivalHandler {
    /**
     * Create a title label and a countdown label based on the current or upcoming festival
     * and use the components to create a new FestivalComponents class instance.
     *
     * @param festivalList              a list of festivals with a name, start date, and end date
     * @param uniqueEventSubgroups      a list of unique event subgroup names
     *
     * @return                          a new FestivalComponents instance with a title label and a countdown label
     */
    public static FestivalComponents getFestivalComponents(List<Festival> festivalList, List<String> uniqueEventSubgroups) {
        LocalDateTime utcDate = getUtcDate();

        JLabel titleLabel = null;
        JLabel countdownLabel = null;

        for (Festival festival : festivalList) {
            String festivalName = festival.getName();
            LocalDateTime startDate = formatDate(festival.getStartDate());
            boolean festivalOngoing = isFestivalOngoing(festival, utcDate);

            if (!festivalOngoing && isFestivalUpNext(festivalList, utcDate, startDate)) {
                titleLabel = createTitleLabel("Next festival: " + festivalName);
                countdownLabel = createCountdownLabel(utcDate, startDate, false);
            }
            else if (festivalOngoing) {
                titleLabel = createTitleLabel(festivalName);
                countdownLabel = createCountdownLabel(utcDate, formatDate(festival.getEndDate()), true);
                uniqueEventSubgroups.add(festivalName);
            }
        }

        return new FestivalComponents(titleLabel, countdownLabel);
    }

    /**
     * Get the zone ID of UTC and get the current date in UTC.
     *
     * @return      the current date in UTC
     */
    private static LocalDateTime getUtcDate() {
        ZoneId utcZoneId = ZoneId.of("UTC");
        return LocalDateTime.now(utcZoneId).truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * Format and parse a date string.
     *
     * @param date      a date string
     * @return          the LocalDateTime of the formatted date string
     */
    private static LocalDateTime formatDate(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, dateFormatter);
    }

    /**
     * Determine if a festival is ongoing based on the festival start/end dates and the current UTC date.
     *
     * @param festival      the individual festival data
     * @param utcDate       the current date in UTC
     *
     * @return              the boolean result from checking against the current date
     */
    private static Boolean isFestivalOngoing(Festival festival, LocalDateTime utcDate) {
        LocalDateTime startDate = formatDate(festival.getStartDate());
        LocalDateTime endDate = formatDate(festival.getEndDate());

        return utcDate.isAfter(startDate) && utcDate.isBefore(endDate) || utcDate.equals(startDate) || utcDate.equals(endDate);
    }

    /**
     * Determine if a festival is coming up next based on the nearest festival date.
     *
     * @param festivalList          a list of festivals with a name, start date, and end date
     * @param utcDate               the current date in UTC
     * @param festivalStartDate     the individual festival start date
     *
     * @return                      the boolean result from checking the festival start date against the nearest festival date
     */
    private static Boolean isFestivalUpNext(List<Festival> festivalList, LocalDateTime utcDate, LocalDateTime festivalStartDate) {
        LocalDateTime nearestFestivalDate = getNearestFestivalDate(festivalList, utcDate);
        return festivalStartDate.equals(nearestFestivalDate);
    }

    /**
     * Get the nearest festival start date after the current date.
     *
     * @param festivalList      a list of festivals with a name, start date, and end date
     * @param utcDate           the current date in UTC
     *
     * @return                  the LocalDateTime of the nearest festival
     */
    private static LocalDateTime getNearestFestivalDate(List<Festival> festivalList, LocalDateTime utcDate) {
        List<LocalDateTime> festivalStartDates = getFestivalStartDates(festivalList, utcDate);
        LocalDateTime nearestFestivalDate = calculateNearestFestivalDate(utcDate, festivalStartDates);

        int nearestFestivalDateIndex = festivalStartDates.indexOf(nearestFestivalDate);

        if (utcDate.isAfter(nearestFestivalDate)) {
            nearestFestivalDateIndex += 1;

            if (nearestFestivalDateIndex > festivalStartDates.indexOf(festivalStartDates.getLast())) {
                nearestFestivalDateIndex = 0;
            }
        }

        return festivalStartDates.get(nearestFestivalDateIndex);
    }

    /**
     * Get the formatted start date for each festival and add the dates to a list.
     *
     * @param festivalList      a list of festivals with a name, start date, and end date
     * @return                  a list of LocalDateTime start dates
     */
    private static List<LocalDateTime> getFestivalStartDates(List<Festival> festivalList, LocalDateTime utcDate) {
        List<LocalDateTime> festivalStartDates = new ArrayList<>();

        for (Festival festival : festivalList) {
            LocalDateTime startDate = formatDate(festival.getStartDate());
            if (startDate.isAfter(utcDate)) {
                festivalStartDates.add(startDate);
            }
        }

        return festivalStartDates;
    }

    /**
     * Get the difference between a start date and the current date and compare the results of the date differences.
     *
     * @param utcDate               the current date in UTC
     * @param festivalStartDates    a list of LocalDateTime start dates
     *
     * @return                      the LocalDateTime of the nearest festival
     */
    private static LocalDateTime calculateNearestFestivalDate(LocalDateTime utcDate, List<LocalDateTime> festivalStartDates) {
        return Collections.min(festivalStartDates, (dateComparisonA, dateComparisonB) -> {
            long dateDifferenceA = Math.abs(dateComparisonA.getDayOfYear() - utcDate.getDayOfYear());
            long dateDifferenceB = Math.abs(dateComparisonB.getDayOfYear() - utcDate.getDayOfYear());
            return Long.compare(dateDifferenceA, dateDifferenceB);
        });
    }

    /**
     * Create a title label and set the text style.
     *
     * @param titleString       the string to set as the label text
     * @return                  a new title JLabel
     */
    private static JLabel createTitleLabel(String titleString) {
        JLabel titleLabel = new JLabel(titleString);
        titleLabel.putClientProperty("FlatLaf.styleClass", "large");
        return titleLabel;
    }

    /**
     * Get a countdown string based on the days and hours until a festival starts or ends
     * and create a countdown label using the countdown string.
     *
     * @param utcDate               the current date in UTC
     * @param festivalDate          the start or end date of a festival
     * @param festivalOngoing       the boolean determining if the individual festival is ongoing
     *
     * @return                      a new countdown JLabel
     */
    private static JLabel createCountdownLabel(LocalDateTime utcDate, LocalDateTime festivalDate, Boolean festivalOngoing) {
        long daysUntilFestival = ChronoUnit.DAYS.between(utcDate, festivalDate);
        long hoursUntilFestival = calculateHoursUntilFestival(utcDate, festivalDate, daysUntilFestival);
        String countdownString = getCountdownString(festivalOngoing, daysUntilFestival, hoursUntilFestival);

        return new JLabel(countdownString);
    }

    /**
     * Get the remaining hours until a festival starts or ends.
     *
     * @param utcDate               the current date in UTC
     * @param festivalDate          the start or end date of a festival
     * @param daysUntilFestival     the number of days between the current date and a festival date
     *
     * @return                      the remaining hours as a long number
     */
    private static Long calculateHoursUntilFestival(LocalDateTime utcDate, LocalDateTime festivalDate, Long daysUntilFestival) {
        long totalHoursUntilFestival = ChronoUnit.HOURS.between(utcDate, festivalDate);
        long daysAsHours = daysUntilFestival * 24;
        return totalHoursUntilFestival - daysAsHours;
    }

    /**
     * Build a countdown string based on if the festival is ongoing
     * and the number of days and hours until the festival starts or ends.
     *
     * @param festivalOngoing       the boolean determining if the individual festival is ongoing
     * @param daysUntilFestival     the number of days between the current date and a festival date
     * @param hoursUntilFestival    the remaining hours until a festival starts or ends
     *
     * @return                      a string with the days and/or hours until a festival starts or ends
     */
    private static String getCountdownString(Boolean festivalOngoing, Long daysUntilFestival, Long hoursUntilFestival) {
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
}
