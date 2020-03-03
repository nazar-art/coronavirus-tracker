package net.lelyak.utils;

import lombok.experimental.UtilityClass;
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
@UtilityClass
public class Clock {
    private LocalDateTime dateTime;

    
    public LocalDateTime getCurrentDateTime() {
        return (dateTime == null ? LocalDateTime.now() : dateTime);
    }

    public void setDateTime(LocalDateTime date) {
        log.info("Set current date for application to: {}", date);
        Clock.dateTime = date;
    }

    public void resetDateTime() {
        Clock.dateTime = LocalDateTime.now();
        log.info("Reset date for the application to {}", getCurrentDateTime());
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