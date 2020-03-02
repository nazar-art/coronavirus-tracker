package net.lelyak.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Class for controlling DateTime for the application.
 *
 * @author Nazar_Lelyak.
 */
@Slf4j
public final class Clock {
    private static LocalDateTime dateTime;

    private Clock() {
    }


    public static LocalDateTime getCurrentDateTime() {
        return (dateTime == null ? LocalDateTime.now() : dateTime);
    }

    public static void setDateTime(LocalDateTime date) {
        log.info("Set current date for application to: {}", date);
        Clock.dateTime = date;
    }

    public static void resetDateTime() {
        log.info("Reset date for the application");
        Clock.dateTime = LocalDateTime.now();
    }

    /**
     * Different formats for current DateTimes.
     */
    public static LocalDate getCurrentDate() {
        return getCurrentDateTime().toLocalDate();
    }

    public static LocalTime getCurrentTime() {
        return getCurrentDateTime().toLocalTime();
    }

    public static DayOfWeek getCurrentDay() {
        return getCurrentDateTime().getDayOfWeek();
    }
}