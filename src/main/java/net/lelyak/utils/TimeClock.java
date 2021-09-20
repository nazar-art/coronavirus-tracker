package net.lelyak.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Class for controlling DateTime for the application.
 *
 * @author Nazar Lelyak.
 */
@Slf4j
@UtilityClass
public class TimeClock {

    public static final ZoneId TIME_ZONE_UTC = ZoneId.of("UTC");

    private ZonedDateTime dateTime;

    public ZonedDateTime getCurrentDateTime() {
        return (dateTime == null ? ZonedDateTime.now(TIME_ZONE_UTC) : dateTime);
    }

    public void setDateTime(ZonedDateTime date) {
        log.info("Set current date for application to: {}", date);
        TimeClock.dateTime = date;
    }

    public void resetDateTime() {
        log.info("Reset date for the application");
        TimeClock.dateTime = ZonedDateTime.now(TIME_ZONE_UTC);
    }

    /**
     * Different formats for current DateTimes.
     */
    public LocalDate getCurrentDate() {
        return getCurrentDateTime().toLocalDate();
    }

    public LocalTime getCurrentTime() {
        return getCurrentDateTime().toLocalTime();
    }

    public DayOfWeek getCurrentDay() {
        return getCurrentDateTime().getDayOfWeek();
    }
}